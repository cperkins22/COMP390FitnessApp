package com.fitnessapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class LoginController {

    // Hardcoded test credentials
    private static final String TEST_USERNAME = "test@example.com";
    private static final String TEST_PASSWORD = "password123";

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    private final UserDao userDao = new UserDao();

    @FXML
    private void handleLoginButton(ActionEvent event) throws IOException {
        String username = trim(usernameField.getText());
        String password = trim(passwordField.getText());

        // Validate inputs
        if (username.isEmpty() || password.isEmpty()) {
            alert("Please enter both username and password.");
            return;
        }

        // Check hardcoded credentials
        if (!username.equals(TEST_USERNAME) || !password.equals(TEST_PASSWORD)) {
            alert("Invalid username or password.");
            return;
        }

        // If credentials match, look up the user in the database
        try {
            Optional<User> userOpt = userDao.findByEmail(username);
            if (userOpt.isEmpty()) {
                alert("User not found in database. Please create an account first.");
                return;
            }

            // Set the session
            Session.setCurrentUser(userOpt.get());

            // Navigate to main menu
            Parent mainMenuParent = FXMLLoader.load(getClass().getResource("/fxml/mainmenu.fxml"));
            Scene mainMenuScene = new Scene(mainMenuParent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(mainMenuScene);
            stage.setTitle("Main Menu");
            stage.show();

        } catch (SQLException e) {
            alert("Database error: " + e.getMessage());
        }
    }

    @FXML
    private void handleCreateAccount(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/create_profile.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Create Profile");
        stage.setScene(scene);
        stage.show();
    }

    // Helper methods
    private static String trim(String s) {
        return s == null ? "" : s.trim();
    }

    private static void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
