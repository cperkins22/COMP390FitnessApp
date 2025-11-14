package com.fitnessapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Acts as the profile picker screen.
 * Shows all users and lets you pick one, then go to PIN entry.
 */
public class LoginController {

    @FXML
    private ListView<User> userListView;

    private final UserDao userDao = new UserDao();

    @FXML
    private void initialize() {
        // Load all users from DB into the ListView
        try {
            List<User> users = userDao.findAll();
            ObservableList<User> items = FXCollections.observableArrayList(users);

            // Make the list display "First Last" instead of the verbose toString()
            userListView.setItems(items);
            userListView.setCellFactory(list -> new javafx.scene.control.ListCell<>() {
                @Override
                protected void updateItem(User item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getFirstName() + " " + item.getLastName());
                    }
                }
            });

        } catch (SQLException e) {
            alert("Failed to load users: " + e.getMessage());
        }
    }

    /**
     * Opens the Create Profile screen.
     */
    @FXML
    private void handleCreateAccount(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/create_profile.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Create Profile");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Called when the user clicks "Next".
     * If a user is selected, opens the PIN entry screen for that user.
     */
    @FXML
    private void handleNext(ActionEvent event) throws IOException {
        User selected = userListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            alert("Please select a profile first.");
            return;
        }

        // Load the PIN entry screen and pass the selected user to it
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/enter_pin.fxml"));
        Parent root = loader.load();

        PinController pinController = loader.getController();
        pinController.setUser(selected);  // pass selected user

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Enter PIN");
        stage.setScene(scene);
        stage.show();
    }

    // Simple helper for error alerts
    private static void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
