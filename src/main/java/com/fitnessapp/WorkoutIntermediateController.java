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
 * Controller for the intermediate workout screen.
 * Acts as a navigation hub for:
 *  - Tracking a workout
 *  - Creating a new workout
 *  - Viewing archived workouts
 *  - Returning to the main menu
 */
public class WorkoutIntermediateController {

    /**
     * Navigate back to the Main Menu screen.
     */
    @FXML
    private void handleHomeButton(ActionEvent event) throws IOException {
        navigateToScene(event, "/fxml/main_menu.fxml", "Main Menu");
    }

    /**
     * Navigate to the Track Workout screen.
     */
    @FXML
    private void handleTrackWorkoutButton(ActionEvent event) throws IOException {
        navigateToScene(event, "/fxml/track_workout.fxml", "Track Workout");
    }

    /**
     * Navigate to the Create Workout screen.
     */
    @FXML
    private void handleCreateWorkoutButton(ActionEvent event) throws IOException {
        navigateToScene(event, "/fxml/create_workout.fxml", "Create Workout");
    }

    /**
     * Navigate to the Archived Workouts screen.
     */
    @FXML
    private void handleViewArchivedWorkoutsButton(ActionEvent event) throws IOException {
        navigateToScene(event, "/fxml/workout_archive.fxml", "Archived Workouts");
    }

    /**
     * Helper method to load an FXML file and switch scenes.
     * @param event the button click event
     * @param fxmlPath path to the FXML resource
     * @param title title of the new window
     * @throws IOException if FXML loading fails
     */
    private void navigateToScene(ActionEvent event, String fxmlPath, String title) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource(fxmlPath));
        Scene scene = new Scene(parent);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }
}
