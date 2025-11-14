package com.fitnessapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateWorkoutController {

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
