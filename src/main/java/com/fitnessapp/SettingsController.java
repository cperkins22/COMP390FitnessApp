package com.fitnessapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsController {

    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
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
    private void handleLogout(ActionEvent event) throws IOException {
        // Clear the current user session
        Session.clear();

        Parent login = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        Scene scene = new Scene(login);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Select Profile");
        stage.setScene(scene);
        stage.show();
    }

}
