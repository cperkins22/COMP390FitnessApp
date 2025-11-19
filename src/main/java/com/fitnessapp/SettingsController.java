package com.fitnessapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller for the Settings screen.
 * Provides navigation back to the main menu, clearing workouts, and logging out.
 */
public class SettingsController {

    /**
     * Handles navigation back to the main menu screen.
     *
     * @param event the button click event
     * @throws IOException if the FXML cannot be loaded
     */
    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        Parent mainMenuParent = FXMLLoader.load(getClass().getResource("/fxml/main_menu.fxml"));
        Scene mainMenuScene = new Scene(mainMenuParent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(mainMenuScene);
        stage.setTitle("Main Menu");
        stage.show();
    }

    /**
     * Deletes all workouts associated with the currently logged-in user.
     * Prints errors to the console if deletion fails.
     */
    @FXML
    private void deleteAllWorkouts() {
        User currentUser = Session.getCurrentUser();
        WorkoutDao workoutDao = new WorkoutDao();
        try{
            workoutDao.deleteAllWorkoutsForUser(currentUser.getId());
        } catch (SQLException e){
            System.out.println("ERROR DELETING WORKOUTS: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles logging out the current user.
     * Clears the session and navigates back to the profile selection screen.
     *
     * @param event the button click event
     * @throws IOException if the FXML cannot be loaded
     */
    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        Session.clear();
        Parent login = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        Scene scene = new Scene(login);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Select Profile");
        stage.setScene(scene);
        stage.show();
    }
}
