package com.fitnessapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class CreateProfileController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField heightField; // inches
    @FXML private TextField weightField; // pounds

    private final UserDao userDao = new UserDao();

    @FXML
    private void handleSave(ActionEvent event) {
        // very basic validation
        String first = trim(firstNameField.getText());
        String last  = trim(lastNameField.getText());
        String email = trim(emailField.getText());
        Float height = parseFloat(heightField.getText());
        Float weight = parseFloat(weightField.getText());

        if (first.isEmpty() || last.isEmpty() || email.isEmpty() || height == null || weight == null) {
            alert("Please fill out all fields with valid values.");
            return;
        }
        if (height <= 0 || weight <= 0) {
            alert("Height and weight must be positive numbers.");
            return;
        }

        try {
            // Build and persist the user
            User u = new User();
            u.setFirstName(first);
            u.setLastName(last);
            u.setEmail(email);
            u.setHeight(height); // inches
            u.setWeight(weight); // pounds

            userDao.insert(u);
            Session.setCurrentUser(u);

            // Navigate to profile view
            go(event, "/fxml/profile.fxml", "Profile");
        } catch (SQLException e) {
            alert("Could not save user (email may already exist).");
        } catch (IOException ioe) {
            alert("Navigation error.");
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) throws IOException {
        go(event, "/fxml/login.fxml", "Login");
    }

    // helpers
    private void go(ActionEvent e, String fxml, String title) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
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
