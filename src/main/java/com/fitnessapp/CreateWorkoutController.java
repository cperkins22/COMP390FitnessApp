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
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

/**
 * A controller class for the CreateWorkout screen.
 * Handles adding exercises, sets, descriptions, and saving the workout plan for the current user.
 */
public class CreateWorkoutController {

    /** Field for entering the workout name. */
    @FXML private TextField workoutNameField;

    /** List of exercises currently included in the workout. */
    @FXML private ListView<Exercise> exerciseListView;

    /** Table displaying all sets for the selected exercise. */
    @FXML private TableView<ExerciseSet> setTable;

    /** Column for displaying reps of a set. */
    @FXML private TableColumn<ExerciseSet, Integer> repsCol;

    /** Column for displaying weight of a set. */
    @FXML private TableColumn<ExerciseSet, Float> weightCol;

    /** Field for entering a description for the selected exercise. */
    @FXML private TextArea exerciseDescriptionArea;

    /** Workout currently being created. */
    private Workout currentWorkout;

    /** Observable list backing the exercise ListView. */
    private ObservableList<Exercise> exerciseObservableList;


    /**
     * Initializes the screen, configures cell factories, selection listeners,
     * and prepares a new workout.
     */
    @FXML
    public void initialize() {
        currentWorkout = new Workout();
        exerciseObservableList = FXCollections.observableArrayList();
        exerciseListView.setItems(exerciseObservableList);

        repsCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getReps()).asObject()
        );

        weightCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleFloatProperty(data.getValue().getWeight()).asObject()
        );

        exerciseListView.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) {
                setTable.setItems(FXCollections.observableArrayList(selected.getSetList()));
            }
        });

        exerciseListView.setCellFactory(TextFieldListCell.forListView(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Exercise exercise) {
                return exercise.getName();
            }

            @Override
            public Exercise fromString(String newName) {
                Exercise selected = exerciseListView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    selected.setName(newName);
                }
                return selected;
            }
        }));

        exerciseListView.setEditable(true);

        exerciseDescriptionArea.textProperty().addListener((obs, old, newVal) -> {
            Exercise selected = exerciseListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.setDescription(newVal);
            }
        });

        exerciseListView.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) {
                exerciseDescriptionArea.setText(selected.getDescription());
                setTable.setItems(FXCollections.observableArrayList(selected.getSetList()));
            }
        });

        setTable.setEditable(true);

        repsCol.setCellFactory(TextFieldTableCell.forTableColumn(new javafx.util.converter.IntegerStringConverter()));
        weightCol.setCellFactory(TextFieldTableCell.forTableColumn(new javafx.util.converter.FloatStringConverter()));

        repsCol.setOnEditCommit(event -> {
            ExerciseSet set = event.getRowValue();
            set.setReps(event.getNewValue());
        });

        weightCol.setOnEditCommit(event -> {
            ExerciseSet set = event.getRowValue();
            set.setWeight(event.getNewValue());
        });
    }

    /**
     * Adds a new exercise with a default name and selects it.
     */
    @FXML
    private void addExercise() {
        Exercise newExercise = new Exercise("New Exercise", "");
        exerciseObservableList.add(newExercise);
        currentWorkout.getExercises().add(newExercise);
        exerciseListView.getSelectionModel().select(newExercise);
    }

    /**
     * Removes the selected exercise from the workout.
     */
    @FXML
    private void removeExercise() {
        Exercise selected = exerciseListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            exerciseObservableList.remove(selected);
            currentWorkout.getExercises().remove(selected);
            setTable.setItems(null);
        }
    }

    /**
     * Adds a default set (10 reps, 0 weight) to the selected exercise.
     */
    @FXML
    private void addSet() {
        Exercise selectedExercise = exerciseListView.getSelectionModel().getSelectedItem();
        if (selectedExercise == null) return;

        ExerciseSet set = new ExerciseSet(10, 0);
        selectedExercise.addSet(set);

        setTable.setItems(FXCollections.observableArrayList(selectedExercise.getSetList()));
    }

    /**
     * Removes the selected set from the selected exercise.
     */
    @FXML
    private void removeSet() {
        Exercise selectedExercise = exerciseListView.getSelectionModel().getSelectedItem();
        ExerciseSet selectedSet = setTable.getSelectionModel().getSelectedItem();

        if (selectedExercise == null || selectedSet == null) return;

        selectedExercise.getSetList().remove(selectedSet);

        setTable.setItems(FXCollections.observableArrayList(selectedExercise.getSetList()));
    }

    /**
     * Saves the workout for the current user after validating its name and content.
     *
     * @throws RuntimeException if an unexpected error occurs
     */
    @FXML
    private void saveWorkout() {
        User currentUser = Session.getCurrentUser();
        if (currentUser == null) {
            alert("No user logged in!");
            return;
        }

        String workoutName = workoutNameField.getText().trim();
        //String workoutNotes = exerciseDescriptionArea.getText().trim();
        if (workoutName.isEmpty()) {
            alert("Please enter a workout name!");
            return;
        }

        currentWorkout.setName(workoutName);

        if (currentWorkout.getExercises().isEmpty()) {
            alert("Please add at least one exercise!");
            return;
        }

        try {
            WorkoutDao workoutDao = new WorkoutDao();
            workoutDao.insert(currentWorkout, currentUser.getId());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Workout saved successfully!");
            alert.showAndWait();

        } catch (SQLException e) {
            alert("Error saving workout: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Navigates the user back to the Workout Intermediate screen.
     *
     * @param event the action event triggered by clicking the back button
     * @throws IOException if the destination screen's FXML fails to load
     */
    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/workout_intermediate.fxml")));
        Scene scene = new Scene(parent);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Workouts");
        stage.show();
    }

    /**
     * Shows an error alert dialog with the provided message.
     * Helper method to easily display an alert.
     * @param msg the message to display in the alert
     */
    private static void alert(String msg) {
        var a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
