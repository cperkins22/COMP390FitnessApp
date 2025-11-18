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

/**
 * Class consisting of different handlers for different buttons to traverse throughout the app
 */
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

    /**
     * Specific buttons are Settings, Track Workout, Track Meal, and Handle Profile
     */
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
        // Navigate to meal intermediate instead of directly to TrackMeal
        Parent mealParent = FXMLLoader.load(getClass().getResource("/fxml/meal_intermediate.fxml"));
        Scene mealScene = new Scene(mealParent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(mealScene);
        stage.setTitle("Meals");
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

