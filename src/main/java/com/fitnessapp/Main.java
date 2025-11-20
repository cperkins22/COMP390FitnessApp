package com.fitnessapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Entry point for JavaFX.
 * Loads the main dashboard UI from FXML and applies the stylesheet.
 */
public class Main extends Application {

    /**
     * Starts the JavaFX application.
     * Initializes and displays the primary stage (window) using the dashboard layout.
     *
     * @param stage the primary stage for this application
     * @throws Exception if the FXML or CSS resources fail to load
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 200);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());
        stage.setTitle("FitnessApp Login");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Launches the FitnessApp JavaFX application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch();
    }
}//class end
