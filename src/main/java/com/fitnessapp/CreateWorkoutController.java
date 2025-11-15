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
import javafx.stage.Stage;

import java.io.IOException;

import javafx.scene.control.*;

import java.sql.SQLException;

public class CreateWorkoutController {

    @FXML private TextField workoutNameField;
    @FXML private ListView<Exercise> exerciseListView;
    @FXML private TableView<ExerciseSet> setTable;
    @FXML private TableColumn<ExerciseSet, Integer> repsCol;
    @FXML private TableColumn<ExerciseSet, Float> weightCol;

    private Workout currentWorkout;
    private ObservableList<Exercise> exerciseObservableList;

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
    }

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
        if (workoutName.isEmpty()) {
            showAlert("Please enter a workout name!");
            return;
        }

        currentWorkout.setNotes(workoutName);

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
