package com.fitnessapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class SettingsController {

    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        Parent mainMenuParent = FXMLLoader.load(getClass().getResource("/fxml/mainmenu.fxml"));
        Scene mainMenuScene = new Scene(mainMenuParent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(mainMenuScene);
        stage.setTitle("Main Menu");
        stage.show();
    }

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
