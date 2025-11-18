package com.fitnessapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
/**
 * Class acts as a barrier to move to the Archive Workout Window, the Create Workout Window, and the Track Workout Window
 */




 public class WorkoutIntermediateController {

    @FXML
    private void handleHomeButton(ActionEvent event) throws IOException {
        // Load the main menu FXML
        Parent mainMenuParent = FXMLLoader.load(getClass().getResource("/fxml/mainmenu.fxml"));
        Scene mainMenuScene = new Scene(mainMenuParent);

        // Get the current stage (window) and set the new scene
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(mainMenuScene);
        stage.setTitle("Main Menu");
        stage.show();
    }

    @FXML
    private void handleTrackWorkoutButton(ActionEvent event) throws IOException {
        // Load the main menu FXML
        Parent trackWorkoutParent = FXMLLoader.load(getClass().getResource("/fxml/track_workout.fxml"));
        Scene trackWorkoutScene = new Scene(trackWorkoutParent);

        // Get the current stage (window) and set the new scene
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(trackWorkoutScene);
        stage.setTitle("Track Workout");
        stage.show();
    }

    @FXML
    private void handleCreateWorkoutButton(ActionEvent event) throws IOException {
        // Load the main menu FXML
        Parent createWorkoutParent = FXMLLoader.load(getClass().getResource("/fxml/create_workout.fxml"));
        Scene createWorkoutScene = new Scene(createWorkoutParent);

        // Get the current stage (window) and set the new scene
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(createWorkoutScene);
        stage.setTitle("Create Workout");
        stage.show();
    }

    @FXML
    private void handleViewArchivedWorkoutsButton(ActionEvent event) throws IOException {
        // Load the main menu FXML
        Parent workoutArchiveParent = FXMLLoader.load(getClass().getResource("/fxml/workout_archive.fxml"));
        Scene workoutArchiveScene = new Scene(workoutArchiveParent);

        // Get the current stage (window) and set the new scene
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(workoutArchiveScene);
        stage.setTitle("Archived Workouts");
        stage.show();
    }

}
