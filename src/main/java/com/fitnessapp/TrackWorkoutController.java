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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrackWorkoutController {

    @FXML
    private ChoiceBox<String> workoutSelect;

    @FXML
    private Button loadWorkoutButton;

    @FXML
    private ListView<String> exerciseList;

    @FXML
    private TableView<ExerciseSet> setTable;

    @FXML
    private TableColumn<ExerciseSet, Integer> setCol;

    @FXML
    private TableColumn<ExerciseSet, Integer> repsCol;

    @FXML
    private TableColumn<ExerciseSet, Float> weightCol;

    @FXML
    private Button addSetButton;

    @FXML
    private Button removeSetButton;

    @FXML
    private Button saveWorkoutButton;

    private final WorkoutDao workoutDao = new WorkoutDao();

    // Current loaded workout
    private Workout currentWorkout;

    // Map workout names to workout objects
    private Map<String, Workout> workoutMap;

    // Observable lists
    private ObservableList<String> workoutNames;
    private ObservableList<String> exerciseNames;

    /**
     * Initialize the controller.
     */
    @FXML
    public void initialize() {
        // Initialize collections
        workoutMap = new HashMap<>();
        workoutNames = FXCollections.observableArrayList();
        exerciseNames = FXCollections.observableArrayList();

        workoutSelect.setItems(workoutNames);
        exerciseList.setItems(exerciseNames);

        // Set up table columns
        setCol.setCellValueFactory(cellData -> {
            int index = setTable.getItems().indexOf(cellData.getValue()) + 1;
            return new javafx.beans.property.SimpleIntegerProperty(index).asObject();
        });
        repsCol.setCellValueFactory(new PropertyValueFactory<>("reps"));
        weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));

        // Make table editable
        setTable.setEditable(true);
        repsCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        weightCol.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));

        // Listen for exercise selection
        exerciseList.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.intValue() >= 0 && currentWorkout != null) {
                loadSelectedExercise(newVal.intValue());
            }
        });

        // Wire up buttons
        loadWorkoutButton.setOnAction(e -> handleLoadWorkout());
        addSetButton.setOnAction(e -> handleAddSet());
        removeSetButton.setOnAction(e -> handleRemoveSet());
        saveWorkoutButton.setOnAction(e -> handleSaveWorkout());

        // Load workouts when screen loads
        loadUserWorkouts();
    }

    /**
     * Load all workouts for the current user.
     */
    private void loadUserWorkouts() {
        User currentUser = Session.getCurrentUser();
        if (currentUser == null) {
            return;
        }

        try {
            List<Workout> workouts = workoutDao.findByUserId(currentUser.getId());

            workoutNames.clear();
            workoutMap.clear();

            for (Workout workout : workouts) {
                String displayName = workout.getNotes() + " (" + workout.getDate() + ")";
                workoutNames.add(displayName);
                workoutMap.put(displayName, workout);
            }

            if (workoutNames.isEmpty()) {
                showAlert("No saved workouts found. Create a workout first!");
            }

        } catch (SQLException e) {
            showAlert("Error loading workouts: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handle loading the selected workout.
     */
    private void handleLoadWorkout() {
        String selectedName = workoutSelect.getValue();
        if (selectedName == null) {
            showAlert("Please select a workout first!");
            return;
        }

        currentWorkout = workoutMap.get(selectedName);
        if (currentWorkout != null) {
            // Load exercises into list
            exerciseNames.clear();
            for (Exercise exercise : currentWorkout.getExercises()) {
                exerciseNames.add(exercise.getName());
            }

            // Clear the set table
            setTable.getItems().clear();
        }
    }

    /**
     * Load the selected exercise's sets.
     */
    private void loadSelectedExercise(int index) {
        if (index >= 0 && index < currentWorkout.getExercises().size()) {
            Exercise exercise = currentWorkout.getExercises().get(index);
            setTable.setItems(FXCollections.observableArrayList(exercise.getSetList()));
        }
    }

    /**
     * Handle adding a new set to the selected exercise.
     */
    private void handleAddSet() {
        if (currentWorkout == null) {
            showAlert("Please load a workout first!");
            return;
        }

        int selectedIndex = exerciseList.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            // Create a default set
            ExerciseSet set = new ExerciseSet(10, 0);
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
        int selectedExerciseIndex = exerciseList.getSelectionModel().getSelectedIndex();
        int selectedSetIndex = setTable.getSelectionModel().getSelectedIndex();

        if (selectedExerciseIndex >= 0 && selectedSetIndex >= 0) {
            Exercise exercise = currentWorkout.getExercises().get(selectedExerciseIndex);
            exercise.getSetList().remove(selectedSetIndex);
            setTable.getItems().remove(selectedSetIndex);
        }
    }

    /**
     * Handle saving the workout changes back to the database.
     */
    private void handleSaveWorkout() {
        if (currentWorkout == null) {
            showAlert("No workout loaded!");
            return;
        }

        try {
            // For simplicity, we'll delete and re-insert the workout
            // A more sophisticated approach would update in place
            workoutDao.delete(currentWorkout.getId());
            workoutDao.insert(currentWorkout, Session.getCurrentUser().getId());

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Workout saved successfully!");
            alert.showAndWait();

        } catch (SQLException e) {
            showAlert("Error saving workout: " + e.getMessage());
            e.printStackTrace();
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
        Parent mainMenuParent = FXMLLoader.load(getClass().getResource("/fxml/mainmenu.fxml"));
        Scene mainMenuScene = new Scene(mainMenuParent);

        // Get the current stage (window) and set the new scene
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(mainMenuScene);
        stage.setTitle("Main Menu");
        stage.show();
    }
}
