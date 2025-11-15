package com.fitnessapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class TrackWorkoutController {

    @FXML private Spinner<Integer> setsSpinner;
    @FXML private TextField exerciseNameInput;
    @FXML private VBox exercisesContainer;

    @FXML
    public void initialize() {
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 3);
        setsSpinner.setValueFactory(valueFactory);
    }

    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        // Load the main menu FXML
        Parent mainMenuParent = FXMLLoader.load(getClass().getResource("/fxml/mainmenu.fxml"));
        Scene mainMenuScene = new Scene(mainMenuParent);

        // Get the current stage (window) and set the new scene
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(mainMenuScene);
        stage.setTitle("Main Menu");
        stage.show();
    }

}
