package com.fitnessapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private void handleLoginButton(ActionEvent event) throws IOException {
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
    private void handleCreateAccount(javafx.event.ActionEvent event) throws java.io.IOException {
        javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(
                getClass().getResource("/fxml/create_profile.fxml")
        );
        javafx.scene.Scene scene = new javafx.scene.Scene(root);
        javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource())
                .getScene().getWindow();
        stage.setTitle("Create Profile");
        stage.setScene(scene);
        stage.show();
    }


}
