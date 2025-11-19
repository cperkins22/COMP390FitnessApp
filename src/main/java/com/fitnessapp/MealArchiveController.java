package com.fitnessapp;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller for viewing archived meals grouped by date.
 * Displays meal history with nutritional information and daily totals.
 */
public class MealArchiveController {

    // ---------- FXML UI Elements ---------- //

    /** ListView showing dates with logged meals. */
    @FXML private ListView<String> dateListView;
    /** ListView showing meals for the selected date. */
    @FXML private ListView<Meal> mealListView;
    /** Label showing total calories for the selected date. */
    @FXML private Label totalCaloriesLabel;
    /** Label showing total protein for the selected date. */
    @FXML private Label totalProteinLabel;
    /** Label showing total carbohydrates for the selected date. */
    @FXML private Label totalCarbsLabel;
    /** Label showing total fats for the selected date. */
    @FXML private Label totalFatsLabel;

    // ---------- Data ---------- //

    /** Data access object for meals. */
    private final MealDao mealDao = new MealDao();
    /** Map of date strings to lists of meals for that date. */
    private Map<String, List<Meal>> mealsByDate;
    /** Formatter for displaying dates in the UI. */
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM d, yyyy");

    /**
     * Initializes the MealArchive screen.
     * Loads meals for the current user, groups them by date, and populates the date list.
     */
    @FXML
    public void initialize() {
        User currentUser = Session.getCurrentUser();
        if (currentUser == null) {
            System.out.println("ERROR: No user logged in.");
            return;
        }

        try {
            // Load all meals for the current user
            List<Meal> allMeals = mealDao.findByUserId(currentUser.getId());

            // Group meals by date
            mealsByDate = allMeals.stream()
                    .collect(Collectors.groupingBy(meal -> {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(meal.getDate());
                        cal.set(Calendar.HOUR_OF_DAY, 0);
                        cal.set(Calendar.MINUTE, 0);
                        cal.set(Calendar.SECOND, 0);
                        cal.set(Calendar.MILLISECOND, 0);
                        return dateFormat.format(cal.getTime());
                    }));

            // Populate date list (sorted newest first)
            List<String> dates = new ArrayList<>(mealsByDate.keySet());
            dates.sort((d1, d2) -> {
                try {
                    Date date1 = dateFormat.parse(d1);
                    Date date2 = dateFormat.parse(d2);
                    return date2.compareTo(date1); // Descending order
                } catch (Exception e) {
                    return 0;
                }
            });

            dateListView.setItems(FXCollections.observableArrayList(dates));

            // When user selects a date, show meals for that date
            dateListView.getSelectionModel().selectedItemProperty().addListener((obs, oldDate, newDate) -> {
                if (newDate != null) {
                    displayMealsForDate(newDate);
                }
            });

            // Custom cell factory for meal list to show detailed info
            mealListView.setCellFactory(list -> new ListCell<>() {
                @Override
                protected void updateItem(Meal meal, boolean empty) {
                    super.updateItem(meal, empty);
                    if (empty || meal == null) {
                        setText(null);
                    } else {
                        setText(meal.toString());
                    }
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error loading meal history: " + e.getMessage());
        }
    }

    /**
     * Display all meals for a specific date and calculate totals.
     *
     * @param dateString the formatted date string
     */
    private void displayMealsForDate(String dateString) {
        List<Meal> mealsForDate = mealsByDate.get(dateString);

        if (mealsForDate == null || mealsForDate.isEmpty()) {
            mealListView.setItems(FXCollections.observableArrayList());
            resetTotals();
            return;
        }

        // Display meals
        mealListView.setItems(FXCollections.observableArrayList(mealsForDate));

        // Calculate totals
        int totalCalories = 0;
        double totalProtein = 0.0;
        double totalCarbs = 0.0;
        double totalFats = 0.0;

        for (Meal meal : mealsForDate) {
            totalCalories += meal.getCalories();
            totalProtein += meal.getProtein();
            totalCarbs += meal.getCarbs();
            totalFats += meal.getFat();
        }

        // Update labels
        totalCaloriesLabel.setText(String.format("%d cal", totalCalories));
        totalProteinLabel.setText(String.format("%.1fg", totalProtein));
        totalCarbsLabel.setText(String.format("%.1fg", totalCarbs));
        totalFatsLabel.setText(String.format("%.1fg", totalFats));
    }

    /**
     * Reset all total labels to zero.
     */
    private void resetTotals() {
        totalCaloriesLabel.setText("0 cal");
        totalProteinLabel.setText("0g");
        totalCarbsLabel.setText("0g");
        totalFatsLabel.setText("0g");
    }

    /**
     * Navigates back to the menu screen.
     *
     * @param event the button click event
     * @throws IOException if loading the FXML fails
     */
    @FXML
    private void handleBackButton(javafx.event.ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/main_menu.fxml"));
        Scene scene = new Scene(parent);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Main Menu");
        stage.show();
    }

    /**
     * Delete the selected meal from the database.
     */
    @FXML
    private void handleDeleteMeal() {
        Meal selectedMeal = mealListView.getSelectionModel().getSelectedItem();
        if (selectedMeal == null) {
            showAlert("Please select a meal to delete.");
            return;
        }

        // Confirm deletion
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Meal");
        confirmAlert.setContentText("Are you sure you want to delete: " + selectedMeal.getName() + "?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                User currentUser = Session.getCurrentUser();
                Date mealDate = selectedMeal.getDate();

                // Delete the meal
                mealDao.delete(selectedMeal.getId());

                // Update daily log for that date
                DailyLogDao dailyLogDao = new DailyLogDao();
                Date normalizedDate = DailyLogDao.normalizeToDateOnly(mealDate);
                Optional<DailyLog> logOpt = dailyLogDao.findByUserIdAndDate(currentUser.getId(), normalizedDate);

                if (logOpt.isPresent()) {
                    DailyLog log = logOpt.get();

                    // Recalculate total calories for that date
                    List<Meal> mealsForDate = mealDao.findByUserIdAndDate(currentUser.getId(), mealDate);
                    int totalCalories = mealsForDate.stream()
                            .mapToInt(Meal::getCalories)
                            .sum();

                    log.setTotalCalories(totalCalories);
                    dailyLogDao.update(log);
                }

                // Refresh the display
                String selectedDate = dateListView.getSelectionModel().getSelectedItem();
                if (selectedDate != null) {
                    // Remove from local data
                    mealsByDate.get(selectedDate).remove(selectedMeal);

                    // If no more meals for this date, remove the date
                    if (mealsByDate.get(selectedDate).isEmpty()) {
                        mealsByDate.remove(selectedDate);
                        dateListView.getItems().remove(selectedDate);
                    } else {
                        // Refresh the meal list
                        displayMealsForDate(selectedDate);
                    }
                }

                showInfoAlert("Meal deleted successfully!");

            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error deleting meal: " + e.getMessage());
            }
        }
    }

    /**
     * Show an error alert.
     *
     * @param message the message to display
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Show an info alert.
     *
     * @param message the message to display
     */
    private void showInfoAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}