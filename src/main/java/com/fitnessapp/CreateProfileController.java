package com.fitnessapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

/**
 * A controller class for the CreateProfile screen.
 * Handles creation of a new user profile and saving it to the database.
 */
public class CreateProfileController {
    /**
     * JavaFX fields for the user to fill out
     */
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField heightField;
    @FXML private TextField weightField;
    @FXML private PasswordField pinField;

    /** User database access object to communicate with the database */
    private final UserDao userDao = new UserDao();

    /**
     * Handles the cancel action and returns the user to the profile picker (login screen).
     *
     * @param event the event triggered by clicking the cancel button
     * @throws IOException if the login FXML file cannot be loaded
     */
    @FXML
    private void handleCancel(ActionEvent event) throws IOException {
        // Go back to profile picker (login.fxml)
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Select Profile");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Validates user input, constructs a {@link User} object, saves it using the DAO,
     * and navigates back to the login screen on success.
     *
     * @param event the event triggered by the save button
     */
    @FXML
    private void handleSave(ActionEvent event) {
        String first  = trim(firstNameField.getText());
        String last   = trim(lastNameField.getText());
        String email  = trim(emailField.getText());
        String heightStr = trim(heightField.getText());
        String weightStr = trim(weightField.getText());
        String pin    = trim(pinField.getText());

        if (first.isEmpty() || last.isEmpty() || email.isEmpty()
                || heightStr.isEmpty() || weightStr.isEmpty() || pin.isEmpty()) {
            alert("All fields, including PIN, are required.");
            return;
        }

        // Very basic PIN validation
        if (pin.length() != 4 || !pin.chars().allMatch(Character::isDigit)) {
            alert("PIN must be exactly 4 digits.");
            return;
        }

        Float height = parseFloat(heightStr);
        Float weight = parseFloat(weightStr);
        if (height == null || height <= 0 || weight == null || weight <= 0) {
            alert("Height and weight must be positive numbers.");
            return;
        }

        try {
            User user = new User(first, last, email, pin, height, weight);
            userDao.insert(user);

            // After saving, go back to profile picker so user can select and log in
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Select Profile");
            stage.setScene(scene);
            stage.show();

        } catch (SQLException e) {
            alert("Failed to save user: " + e.getMessage());
        } catch (IOException e) {
            alert("Failed to load login screen: " + e.getMessage());
        }
    }

    /**
     * Trims leading and trailing whitespace from the given string.
     *
     * @param s the string to trim; may be null
     * @return a trimmed string, or an empty string if {@code s} is null
     */
    private static String trim(String s) { return s == null ? "" : s.trim(); }

    /**
     * Attempts to parse a string into a Float.
     *
     * @param s the string to parse
     * @return the parsed float value, or {@code null} if parsing fails
     */
    private static Float parseFloat(String s) {
        try { return Float.parseFloat(s.trim()); } catch (Exception e) { return null; }
    }

    /**
     * Shows an error alert dialog with the provided message.
     * Helper method to easily display an alert.
     * @param msg the message to display in the alert
     */
    private static void alert(String msg) {
        var a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}//class end
