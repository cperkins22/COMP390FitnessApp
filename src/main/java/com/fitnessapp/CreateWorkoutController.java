package com.fitnessapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.sql.SQLException;

public class CreateWorkoutController {

    @FXML
    private TextField workoutNameField;

    @FXML
    private ListView<String> exerciseList;

    @FXML
    private TextArea exerciseDescriptionArea;

    @FXML
    private TableView<ExerciseSet> setTable;

    @FXML
    private TableColumn<ExerciseSet, Integer> repsCol;

    @FXML
    private TableColumn<ExerciseSet, Float> weightCol;

    @FXML
    private Button addExerciseButton;

    @FXML
    private Button removeExerciseButton;

    @FXML
    private Button addSetButton;

    @FXML
    private Button removeSetButton;

    @FXML
    private Button saveWorkoutButton;

    private final WorkoutDao workoutDao = new WorkoutDao();

    // The workout being created
    private Workout currentWorkout;

    // Observable list for exercises (just names for display)
    private ObservableList<String> exerciseNames;

    /**
     * Initialize the controller.
     */
    @FXML
    public void initialize() {
        // Create a new workout
        currentWorkout = new Workout();

        // Initialize the exercise list
        exerciseNames = FXCollections.observableArrayList();
        exerciseList.setItems(exerciseNames);

        // Set up the table columns
        repsCol.setCellValueFactory(new PropertyValueFactory<>("reps"));
        weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));

        // Make the table editable
        setTable.setEditable(true);
        repsCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        weightCol.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));

        // Listen for exercise selection changes
        exerciseList.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.intValue() >= 0) {
                loadSelectedExercise(newVal.intValue());
            }
        });

        // Wire up buttons
        addExerciseButton.setOnAction(e -> handleAddExercise());
        removeExerciseButton.setOnAction(e -> handleRemoveExercise());
        addSetButton.setOnAction(e -> handleAddSet());
        removeSetButton.setOnAction(e -> handleRemoveSet());
        saveWorkoutButton.setOnAction(e -> handleSaveWorkout());
    }

    /**
     * Handle adding a new exercise to the workout.
     */
    private void handleAddExercise() {
        // Prompt user for exercise name
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Exercise");
        dialog.setHeaderText("Enter exercise name");
        dialog.setContentText("Exercise name:");

        dialog.showAndWait().ifPresent(name -> {
            if (!name.trim().isEmpty()) {
                // Create a new exercise
                Exercise exercise = new Exercise(name.trim(), "");
                currentWorkout.addExercise(exercise);
                exerciseNames.add(name.trim());

                // Select the new exercise
                exerciseList.getSelectionModel().select(exerciseNames.size() - 1);
            }
        });
    }

    /**
     * Handle removing the selected exercise.
     */
    private void handleRemoveExercise() {
        int selectedIndex = exerciseList.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            currentWorkout.getExercises().remove(selectedIndex);
            exerciseNames.remove(selectedIndex);
            exerciseDescriptionArea.clear();
            setTable.getItems().clear();
        }
    }

    /**
     * Handle adding a set to the selected exercise.
     */
    private void handleAddSet() {
        int selectedIndex = exerciseList.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            // Create a default set
            ExerciseSet set = new ExerciseSet(10, 0);  // Default 10 reps, 0 weight
            Exercise exercise = currentWorkout.getExercises().get(selectedIndex);
            exercise.addSet(set);

            // Refresh the table
            setTable.getItems().add(set);
        } else {
            showAlert("Please select an exercise first!");
        }
    }

    /**
     * Handle removing the selected set.
     */
    private void handleRemoveSet() {
        int selectedIndex = exerciseList.getSelectionModel().getSelectedIndex();
        int selectedSetIndex = setTable.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0 && selectedSetIndex >= 0) {
            Exercise exercise = currentWorkout.getExercises().get(selectedIndex);
            exercise.getSetList().remove(selectedSetIndex);
            setTable.getItems().remove(selectedSetIndex);
        }
    }

    /**
     * Load the selected exercise details.
     */
    private void loadSelectedExercise(int index) {
        if (index >= 0 && index < currentWorkout.getExercises().size()) {
            Exercise exercise = currentWorkout.getExercises().get(index);

            // Load description
            exerciseDescriptionArea.setText(exercise.getDescription());

            // Load sets into table
            setTable.setItems(FXCollections.observableArrayList(exercise.getSetList()));
        }
    }

    /**
     * Save description changes when user types in the description area.
     */
    @FXML
    private void handleDescriptionChanged() {
        int selectedIndex = exerciseList.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Exercise exercise = currentWorkout.getExercises().get(selectedIndex);
            exercise.setDescription(exerciseDescriptionArea.getText());
        }
    }

    /**
     * Handle saving the workout to the database.
     */
    private void handleSaveWorkout() {
        User currentUser = Session.getCurrentUser();
        if (currentUser == null) {
            showAlert("No user logged in!");
            return;
        }

        // Validate workout name
        String workoutName = workoutNameField.getText().trim();
        if (workoutName.isEmpty()) {
            showAlert("Please enter a workout name!");
            return;
        }

        // Set workout notes to the name (or you could add a notes field)
        currentWorkout.setNotes(workoutName);

        // Check if there are any exercises
        if (currentWorkout.getExercises().isEmpty()) {
            showAlert("Please add at least one exercise!");
            return;
        }

        try {
            // Save to database
            workoutDao.insert(currentWorkout, currentUser.getId());

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Workout saved successfully!");
            alert.showAndWait();

            // Go back to workout intermediate screen
            handleBackButton(null);

        } catch (SQLException e) {
            showAlert("Error saving workout: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            showAlert("Error navigating back: " + e.getMessage());
        }
    }

    /**
     * Show an alert dialog.
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        // Load the main menu FXML
        Parent workoutIntermediateParent = FXMLLoader.load(getClass().getResource("/fxml/workout_intermediate.fxml"));
        Scene workoutIntermediateScene = new Scene(workoutIntermediateParent);

        // Get the current stage (window) and set the new scene
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(workoutIntermediateScene);
        stage.setTitle("Workouts");
        stage.show();
    }
}
