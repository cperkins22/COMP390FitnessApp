package com.fitnessapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    @FXML
    private void handleSettingsButton(ActionEvent event) throws IOException {
        // Load the main menu FXML
        Parent settingsParent = FXMLLoader.load(getClass().getResource("/fxml/settings.fxml"));
        Scene settingsScene = new Scene(settingsParent);

        // Get the current stage (window) and set the new scene
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(settingsScene);
        stage.setTitle("Settings");
        stage.show();
    }

    @FXML
    private void handleTrackWorkoutButton(ActionEvent event) throws IOException {
        // Load the main menu FXML
        Parent workoutParent = FXMLLoader.load(getClass().getResource("/fxml/TrackWorkout.fxml"));
        Scene workoutScene = new Scene(workoutParent);

        // Get the current stage (window) and set the new scene
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(workoutScene);
        stage.setTitle("Workout");
        stage.show();
    }
    @FXML
    private void handleTrackMealButton(ActionEvent event) throws IOException {
        // Load the main menu FXML
        Parent mealParent = FXMLLoader.load(getClass().getResource("/fxml/TrackMeal.fxml"));
        Scene mealScene = new Scene(mealParent);

        // Get the current stage (window) and set the new scene
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(mealScene);
        stage.setTitle("Meal Plan");
        stage.show();
    }

    @FXML
    private void handleProfileButton(ActionEvent event) throws IOException {
        Parent profile = FXMLLoader.load(getClass().getResource("/fxml/profile.fxml"));
        Scene scene = new Scene(profile);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Profile");
        stage.setScene(scene);
        stage.show();
    }
}
