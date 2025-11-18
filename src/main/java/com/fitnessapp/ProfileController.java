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
 * This class creates a profile and assigns certain traits to it for later trackage and usage (name, height, weight, ID, etc.)
 */
public class ProfileController implements Initializable {

    @FXML private Label firstNameLabel;
    @FXML private Label lastNameLabel;
    @FXML private Label heightLabel;
    @FXML private Label weightLabel;
    @FXML private Label weightGoalLabel;
    @FXML private Label userIdLabel;
    @FXML private Label bmiLabel;
    //text fields for updating personal stats
    @FXML private TextField updateFirstNameField;
    @FXML private TextField updateLastNameField;
    @FXML private TextField updateHeightField;
    @FXML private TextField updateWeightField;
    @FXML private TextField updateWeightGoalField;

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

    @FXML
    private void handleBackToMenu(ActionEvent event) throws IOException {
        Parent mainMenuParent = FXMLLoader.load(getClass().getResource("/fxml/mainmenu.fxml"));
        Scene mainMenuScene = new Scene(mainMenuParent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(mainMenuScene);
        stage.setTitle("Main Menu");
        stage.show();
    }

    /**
     * Below are the handlers to change certain metrics providing the user wishes to do so
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

    private String safe(String s) {
        return s == null ? "" : s;
    }

    private String safeUUID(UUID id) {
        return id == null ? "" : id.toString();
    }

    // Simple helper for error alerts
    private static void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}