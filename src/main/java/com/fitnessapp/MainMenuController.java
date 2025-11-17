package com.fitnessapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class MainMenuController {

    @FXML private Label caloriesLabel;

    @FXML
    public void initialize() {
        try {
            //Get the current user.
            User currentUser = Session.getCurrentUser();
            //Create a DailyLogDao object to access the database.
            DailyLogDao dailyLogDao = new DailyLogDao();
            // Fetch today's log (or create it if it doesn't exist)
            DailyLog todayLog = dailyLogDao.getOrCreateToday(currentUser.getId());

            // Get the total calories
            int totalCalories = todayLog.getTotalCalories();

            // Display on the label
            caloriesLabel.setText(String.valueOf(totalCalories));

        } catch (SQLException e) {
            e.printStackTrace();
            caloriesLabel.setText("0"); // fallback
        }
    }

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
        Parent workoutParent = FXMLLoader.load(getClass().getResource("/fxml/workout_intermediate.fxml"));
        Scene workoutScene = new Scene(workoutParent);

        // Get the current stage (window) and set the new scene
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(workoutScene);
        stage.setTitle("Workouts");
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

}
