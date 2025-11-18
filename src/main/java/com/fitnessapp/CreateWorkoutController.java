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

import javafx.scene.control.*;

import java.sql.SQLException;

/**
 * Class is intended to guide the process of creating workouts and their important components
 */
public class CreateWorkoutController {

    @FXML private TextField workoutNameField;
    @FXML private ListView<Exercise> exerciseListView;
    @FXML private TableView<ExerciseSet> setTable;
    @FXML private TableColumn<ExerciseSet, Integer> repsCol;
    @FXML private TableColumn<ExerciseSet, Float> weightCol;
    @FXML private TextArea exerciseDescriptionArea;

    private Workout currentWorkout;
    private ObservableList<Exercise> exerciseObservableList;


    /**
     * Procedure for creating a bare bones initial point to create a workout
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

        exerciseListView.setCellFactory(TextFieldListCell.forListView(new javafx.util.StringConverter<Exercise>() {
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
     * Different procedures for adding, removing, and saving workouts
     */
    @FXML
    private void addExercise() {
        Exercise newExercise = new Exercise("New Exercise", "");
        exerciseObservableList.add(newExercise);
        currentWorkout.getExercises().add(newExercise);
        exerciseListView.getSelectionModel().select(newExercise);
    }

    @FXML
    private void removeExercise() {
        Exercise selected = exerciseListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            exerciseObservableList.remove(selected);
            currentWorkout.getExercises().remove(selected);
            setTable.setItems(null);
        }
    }

    @FXML
    private void addSet() {
        Exercise selectedExercise = exerciseListView.getSelectionModel().getSelectedItem();
        if (selectedExercise == null) return;

        ExerciseSet set = new ExerciseSet(10, 0);
        selectedExercise.addSet(set);

        setTable.setItems(FXCollections.observableArrayList(selectedExercise.getSetList()));
    }

    @FXML
    private void removeSet() {
        Exercise selectedExercise = exerciseListView.getSelectionModel().getSelectedItem();
        ExerciseSet selectedSet = setTable.getSelectionModel().getSelectedItem();

        if (selectedExercise == null || selectedSet == null) return;

        selectedExercise.getSetList().remove(selectedSet);

        setTable.setItems(FXCollections.observableArrayList(selectedExercise.getSetList()));
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void saveWorkout() {
        User currentUser = Session.getCurrentUser();
        if (currentUser == null) {
            showAlert("No user logged in!");
            return;
        }

        String workoutName = workoutNameField.getText().trim();
        //String workoutNotes = exerciseDescriptionArea.getText().trim();
        if (workoutName.isEmpty()) {
            showAlert("Please enter a workout name!");
            return;
        }

        currentWorkout.setName(workoutName);

        if (currentWorkout.getExercises().isEmpty()) {
            showAlert("Please add at least one exercise!");
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
            showAlert("Error saving workout: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/workout_intermediate.fxml"));
        Scene scene = new Scene(parent);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Workouts");
        stage.show();
    }
}
