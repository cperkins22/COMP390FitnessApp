package com.fitnessapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.UUID;

/**
 * Controller for the user profile screen.
 * Displays personal stats (name, height, weight, BMI, etc.) and allows updating them.
 */
public class ProfileController implements Initializable {

    /** Label for displaying the user's first name. */
    @FXML private Label firstNameLabel;
    /** Label for displaying the user's last name. */
    @FXML private Label lastNameLabel;
    /** Label for displaying the user's height. */
    @FXML private Label heightLabel;
    /** Label for displaying the user's current weight. */
    @FXML private Label weightLabel;
    /** Label for displaying the user's weight goal. */
    @FXML private Label weightGoalLabel;
    /** Label for displaying the user's unique ID. */
    @FXML private Label userIdLabel;
    /** Label for displaying the user's calculated BMI. */
    @FXML private Label bmiLabel;
    /** TextField for updating the user's first name. */
    @FXML private TextField updateFirstNameField;
    /** TextField for updating the user's last name. */
    @FXML private TextField updateLastNameField;
    /** TextField for updating the user's height. */
    @FXML private TextField updateHeightField;
    /** TextField for updating the user's weight. */
    @FXML private TextField updateWeightField;
    /** TextField for updating the user's weight goal. */
    @FXML private TextField updateWeightGoalField;

    /**
     * Initializes the profile screen with the current user's data.
     * Displays placeholders if no user is logged in.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        User user = Session.getCurrentUser();
        if (user == null) {
            firstNameLabel.setText("-");
            lastNameLabel.setText("-");
            heightLabel.setText("-");
            weightLabel.setText("-");
            weightGoalLabel.setText("-");
            userIdLabel.setText("-");
            bmiLabel.setText("-");
            return;
        }

        firstNameLabel.setText(safe(user.getFirstName()));
        lastNameLabel.setText(safe(user.getLastName()));

        heightLabel.setText(String.format("%d ft %.0f in", (int) (user.getHeight() / 12), (user.getHeight() % 12)));
        weightLabel.setText(String.format("%.0f lb", user.getWeight()));
        weightGoalLabel.setText(String.format("%.0f lb", user.getWeightGoal()));

        userIdLabel.setText(safeUUID(user.getId()));

        float h = user.getHeight();
        float w = user.getWeight();
        float bmi = (h > 0) ? (703.0f * w / (h * h)) : 0.0f;
        bmiLabel.setText(String.format("%.1f", bmi));
    }

    /**
     * Handles navigation back to the main menu screen.
     *
     * @param event the button click event
     * @throws IOException if FXML cannot be loaded
     */
    @FXML
    private void handleBackToMenu(ActionEvent event) throws IOException {
        Parent mainMenuParent = FXMLLoader.load(getClass().getResource("/fxml/main_menu.fxml"));
        Scene mainMenuScene = new Scene(mainMenuParent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(mainMenuScene);
        stage.setTitle("Main Menu");
        stage.show();
    }

    /**
     * Updates the user's first name in both session and database.
     *
     * @param event the button click event
     */
    @FXML
    private void handleUpdateFirstName(ActionEvent event) throws IOException {
        try {
            System.out.println("FIRST NAME UPDATED");
            User user = Session.getCurrentUser();
            user.setFirstName(updateFirstNameField.getText());
            UserDao userDao = new UserDao();
            userDao.update(user);
            firstNameLabel.setText(user.getFirstName());
            updateFirstNameField.clear();
        } catch (SQLException e) {
            alert("Failed to update user: " + e.getMessage());
        }
    }

    /**
     * Updates the user's last name in both session and database.
     *
     * @param event the button click event
     */
    @FXML
    private void handleUpdateLastName(ActionEvent event) throws IOException {
        try {
            System.out.println("LAST NAME UPDATED");
            User user = Session.getCurrentUser();
            user.setLastName(updateLastNameField.getText());
            UserDao userDao = new UserDao();
            userDao.update(user);
            lastNameLabel.setText(user.getLastName());
            updateLastNameField.clear();
        } catch (SQLException e) {
            alert("Failed to update user: " + e.getMessage());
        }
    }

    /**
     * Updates the user's height in both session and database.
     *
     * @param event the button click event
     */
    @FXML
    private void handleUpdateHeight(ActionEvent event) throws IOException {
        try {
            System.out.println("HEIGHT UPDATED");
            User user = Session.getCurrentUser();
            user.setHeight(Integer.parseInt(updateHeightField.getText()));
            UserDao userDao = new UserDao();
            userDao.update(user);
            heightLabel.setText(String.format("%d ft %.0f in", (int) (user.getHeight() / 12), (user.getHeight() % 12)));
            updateHeightField.clear();
        } catch (SQLException e) {
            alert("Failed to update user: " + e.getMessage());
        }
    }

    /**
     * Updates the user's weight in both session and database.
     *
     * @param event the button click event
     */
    @FXML
    private void handleUpdateWeight(ActionEvent event) throws IOException {
        try{
            System.out.println("WEIGHT UPDATED");
            User user = Session.getCurrentUser();
            user.setWeight(Integer.parseInt(updateWeightField.getText()));
            UserDao userDao = new UserDao();
            userDao.update(user);
            weightLabel.setText(String.format("%.0f lb", user.getWeight()));
            updateWeightField.clear();
        } catch (SQLException e) {
            alert("Failed to update user: " + e.getMessage());
        }

    }

    /**
     * Updates the user's weight goal in both session and database.
     *
     * @param event the button click event
     */
    @FXML
    private void handleUpdateWeightGoal(ActionEvent event) throws IOException {
        try{
            System.out.println("WEIGHT GOAL UPDATED");
            User user = Session.getCurrentUser();
            user.setWeightGoal(Integer.parseInt(updateWeightGoalField.getText()));
            UserDao userDao = new UserDao();
            userDao.update(user);
            weightGoalLabel.setText(String.format("%.0f lb", user.getWeightGoal()));
            updateWeightGoalField.clear();
        } catch (SQLException e) {
            alert("Failed to update user: " + e.getMessage());
        }
    }
    /**
     * Returns a non-null string for display.
     *
     * @param s the string to check
     * @return the original string if non-null, otherwise an empty string
     */
    private String safe(String s) {
        return s == null ? "" : s;
    }

    /**
     * Returns a non-null string representation of a UUID.
     *
     * @param id the UUID to check
     * @return the UUID string if non-null, otherwise an empty string
     */
    private String safeUUID(UUID id) {
        return id == null ? "" : id.toString();
    }

    /**
     * Displays an error alert with the given message.
     *
     * @param msg the message to display
     */    private static void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}