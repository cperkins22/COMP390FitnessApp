package com.fitnessapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the meal intermediate screen.
 * Provides navigation between tracking new meals and viewing meal history.
 */
public class MealIntermediateController {

    /**
     * Navigate back to the main menu.
     */
    @FXML
    private void handleHomeButton(ActionEvent event) throws IOException {
        Parent mainMenuParent = FXMLLoader.load(getClass().getResource("/fxml/mainmenu.fxml"));
        Scene mainMenuScene = new Scene(mainMenuParent);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(mainMenuScene);
        stage.setTitle("Main Menu");
        stage.show();
    }

    /**
     * Navigate to the Track Meal screen (for adding new meals).
     */
    @FXML
    private void handleTrackMealButton(ActionEvent event) throws IOException {
        Parent trackMealParent = FXMLLoader.load(getClass().getResource("/fxml/TrackMeal.fxml"));
        Scene trackMealScene = new Scene(trackMealParent);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(trackMealScene);
        stage.setTitle("Track Meal");
        stage.show();
    }

    /**
     * Navigate to the Meal Archive screen (for viewing past meals).
     */
    @FXML
    private void handleViewMealArchiveButton(ActionEvent event) throws IOException {
        Parent mealArchiveParent = FXMLLoader.load(getClass().getResource("/fxml/meal_archive.fxml"));
        Scene mealArchiveScene = new Scene(mealArchiveParent);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(mealArchiveScene);
        stage.setTitle("Meal Archive");
        stage.show();
    }
}