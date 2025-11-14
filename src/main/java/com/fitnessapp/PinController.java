package com.fitnessapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Screen for entering the PIN for the selected user.
 */
public class PinController {

    @FXML private Label userLabel;
    @FXML private PasswordField pinField;

    private final UserDao userDao = new UserDao();

    // The user selected on the profile picker
    private User user;

    /**
     * Called by the LoginController after loading this FXML.
     */
    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            userLabel.setText("Enter PIN for " + user.getFirstName() + " " + user.getLastName());
        }
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        if (user == null) {
            alert("No user selected.");
            goBackToLogin(event);
            return;
        }

        String enteredPin = pinField.getText() == null ? "" : pinField.getText().trim();
        if (enteredPin.isEmpty()) {
            alert("Please enter your PIN.");
            return;
        }

        try {
            boolean ok = userDao.validatePin(user.getId(), enteredPin);
            if (!ok) {
                alert("Incorrect PIN.");
                return;
            }

            // Success: store user in Session and go to main menu
            Session.setCurrentUser(user);

            Parent root = FXMLLoader.load(getClass().getResource("/fxml/mainmenu.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Main Menu");
            stage.setScene(scene);
            stage.show();

        } catch (SQLException e) {
            alert("Error validating PIN: " + e.getMessage());
        } catch (IOException e) {
            alert("Failed to load main menu: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        goBackToLogin(event);
    }

    private void goBackToLogin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Select Profile");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            alert("Failed to load profile picker: " + e.getMessage());
        }
    }

    private static void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
