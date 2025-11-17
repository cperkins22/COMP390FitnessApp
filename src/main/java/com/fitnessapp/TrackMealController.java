package com.fitnessapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class TrackMealController {

    @FXML
    private TextField foodNameInput;

    @FXML
    private TextField caloriesInput;

    @FXML
    private TextField proteinInput;

    @FXML
    private TextField carbsInput;

    @FXML
    private TextField fatsInput;

    @FXML
    private VBox mealsContainer;

    private final MealDao mealDao = new MealDao();

    /**
     * Initialize the controller - load today's meals when the screen loads.
     */
    @FXML
    public void initialize() {
        loadTodaysMeals();
    }

    /**
     * Handle the "Add Meal" button click.
     */
    @FXML
    private void handleAddMeal(ActionEvent event) {
        // Get the current user from the session
        User currentUser = Session.getCurrentUser();
        if (currentUser == null) {
            showAlert("No user logged in!");
            return;
        }

        // Validate inputs
        String foodName = foodNameInput.getText().trim();
        if (foodName.isEmpty()) {
            showAlert("Please enter a food name!");
            return;
        }

        String caloriesStr = caloriesInput.getText().trim();
        String proteinStr = proteinInput.getText().trim();
        String carbsStr = carbsInput.getText().trim();
        String fatsStr = fatsInput.getText().trim();

        // Parse the numeric values
        try {
            int calories = Integer.parseInt(caloriesStr);
            double protein = Double.parseDouble(proteinStr);
            double carbs = Double.parseDouble(carbsStr);
            double fats = Double.parseDouble(fatsStr);

            // Validate that values are non-negative
            if (calories < 0 || protein < 0 || carbs < 0 || fats < 0) {
                showAlert("All values must be positive numbers!");
                return;
            }

            // Create the meal
            Meal meal = new Meal(foodName);
            meal.setCalories(calories);
            meal.setProtein(protein);
            meal.setCarbs(carbs);
            meal.setFat(fats);

            // Save to database
            mealDao.insert(meal, currentUser.getId());

            // Update daily log with new calorie total
            DailyLogDao dailyLogDao = new DailyLogDao();
            DailyLog todayLog = dailyLogDao.getOrCreateToday(currentUser.getId());

            // Recalculate total calories from all meals today
            List<Meal> todaysMeals = mealDao.findByUserIdAndDate(currentUser.getId(), new Date());
            int totalCalories = todaysMeals.stream()
                    .mapToInt(Meal::getCalories)
                    .sum();

            todayLog.setTotalCalories(totalCalories);
            dailyLogDao.update(todayLog);

            // Clear the input fields
            clearInputFields();

            // Reload today's meals to show the new one
            loadTodaysMeals();

        } catch (NumberFormatException e) {
            showAlert("Please enter valid numbers for calories and macros!");
        } catch (SQLException e) {
            showAlert("Error saving meal to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handle the "Clear" button click.
     */
    @FXML
    private void handleClear(ActionEvent event) {
        clearInputFields();
    }

    /**
     * Load and display today's meals for the current user.
     */
    private void loadTodaysMeals() {
        User currentUser = Session.getCurrentUser();
        if (currentUser == null) {
            return;
        }

        try {
            // Get today's meals from the database
            List<Meal> todaysMeals = mealDao.findByUserIdAndDate(currentUser.getId(), new Date());

            // Clear the container
            mealsContainer.getChildren().clear();

            if (todaysMeals.isEmpty()) {
                // Show "no meals" message
                Label noMealsLabel = new Label("No meals added yet");
                noMealsLabel.setStyle("-fx-text-fill: #7f8c8d;");
                mealsContainer.getChildren().add(noMealsLabel);
            } else {
                // Display each meal
                for (Meal meal : todaysMeals) {
                    Label mealLabel = new Label(meal.toString());
                    mealLabel.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 14;");
                    mealsContainer.getChildren().add(mealLabel);
                }
            }

        } catch (SQLException e) {
            showAlert("Error loading meals: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Clear all input fields.
     */
    private void clearInputFields() {
        foodNameInput.clear();
        caloriesInput.clear();
        proteinInput.clear();
        carbsInput.clear();
        fatsInput.clear();
    }

    /**
     * Show an alert dialog with the given message.
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Navigate back to meal intermediate screen.
     */
    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        // Navigate back to meal intermediate instead of main menu
        Parent mealIntermediateParent = FXMLLoader.load(getClass().getResource("/fxml/meal_intermediate.fxml"));
        Scene mealIntermediateScene = new Scene(mealIntermediateParent);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(mealIntermediateScene);
        stage.setTitle("Meals");
        stage.show();
    }
}