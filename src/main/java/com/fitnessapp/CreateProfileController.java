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
 * Handles creation of a new user profile and saving it to the DB.
 */
public class CreateProfileController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField heightField;
    @FXML private TextField weightField;
    @FXML private PasswordField pinField;

    private final UserDao userDao = new UserDao();

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

    private static String trim(String s) { return s == null ? "" : s.trim(); }

    private static Float parseFloat(String s) {
        try { return Float.parseFloat(s.trim()); } catch (Exception e) { return null; }
    }

    private static void alert(String msg) {
        var a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
