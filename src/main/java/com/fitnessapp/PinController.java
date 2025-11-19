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
 * Controller for the screen for entering the PIN for the selected user.
 */
public class PinController {

    /** Label showing the selected user's name for PIN entry. */
    @FXML private Label userLabel;
    /** PasswordField where the user enters their PIN. */
    @FXML private PasswordField pinField;
    /** Data access object for user-related database operations. */
    private final UserDao userDao = new UserDao();
    /** The user selected on the profile picker. */
    private User user;

    /**
     * Sets the selected user.
     * Called by the LoginController after loading this FXML.
     *
     * @param user the selected user
     */
    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            userLabel.setText("Enter PIN for " + user.getFirstName() + " " + user.getLastName());
        }
    }

    /**
     * Handles the login button click.
     * Validates the entered PIN and navigates to the main menu if correct.
     *
     * @param event the button click event
     */
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

            Parent root = FXMLLoader.load(getClass().getResource("/fxml/main_menu.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Main Menu");
            stage.setScene(scene);
            stage.show();

            //Create a DailyLogDao object to access the database
            DailyLogDao dailyLogDao = new DailyLogDao();
            // Get today's daily log object for the user that just logged in,
            // If the user has already logged in today, it will already exist.
            // If the user has not logged in yet today, a new DailyLog will be created.
            DailyLog todayLog = dailyLogDao.getOrCreateToday(user.getId());

        } catch (SQLException e) {
            alert("Error validating PIN: " + e.getMessage());
        } catch (IOException e) {
            alert("Failed to load main menu: " + e.getMessage());
        }
    }

    /**
     * Handles the back button click.
     * Navigates back to the profile picker screen.
     *
     * @param event the button click event
     */
    @FXML
    private void handleBack(ActionEvent event) {
        goBackToLogin(event);
    }

    /**
     * Helper to navigate back to the profile picker screen.
     *
     * @param event the button click event
     */
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

    /**
     * Shows an error alert with the given message.
     *
     * @param msg the message to display
     */
    private static void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}//class end
