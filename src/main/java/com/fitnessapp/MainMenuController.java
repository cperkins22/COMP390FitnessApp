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
        Parent settingsParent = FXMLLoader.load(getClass().getResource("/fxml/settings.fxml"));
        Scene settingsScene = new Scene(settingsParent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(settingsScene);
        stage.setTitle("Settings");
        stage.show();
    }

    @FXML
    private void handleTrackWorkoutButton(ActionEvent event) throws IOException {
        Parent workoutParent = FXMLLoader.load(getClass().getResource("/fxml/workout_intermediate.fxml"));
        Scene workoutScene = new Scene(workoutParent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(workoutScene);
        stage.setTitle("Workouts");
        stage.show();
    }

    @FXML
    private void handleTrackMealButton(ActionEvent event) throws IOException {
        Parent mealParent = FXMLLoader.load(getClass().getResource("/fxml/TrackMeal.fxml"));
        Scene mealScene = new Scene(mealParent);
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
        stage.setTitle("Personal Stats");
        stage.setScene(scene);
        stage.show();
    }
}

