package com.fitnessapp;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

public class TrackWorkoutController {

    // ---------- FXML UI Elements ----------
    @FXML private ComboBox<Workout> workoutSelector;
    @FXML private ListView<Exercise> exerciseList;
    @FXML private TableView<ExerciseSet> setTable;
    @FXML private TableColumn<ExerciseSet, Integer> setCol;
    @FXML private TableColumn<ExerciseSet, Integer> repsCol;
    @FXML private TableColumn<ExerciseSet, Float> weightCol;

    @FXML private Button addSetButton;
    @FXML private Button removeSetButton;
    @FXML private Button completeWorkoutButton;

    // ---------- Data ----------
    private ObservableList<Workout> availableWorkouts;
    private Workout activeWorkout;

    @FXML
    public void initialize() {
        // Load current user
        User currentUser = Session.getCurrentUser();
        if (currentUser == null) {
            System.out.println("ERROR: No user logged in.");
            return;
        }

        availableWorkouts = FXCollections.observableArrayList();

        try {
            // Load workouts using your WorkoutDao (same pattern as save)
            WorkoutDao workoutDao = new WorkoutDao();
            availableWorkouts.addAll(workoutDao.findByUserId(currentUser.getId()));

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Fill ComboBox
        workoutSelector.setItems(availableWorkouts);

        // When user selects a workout, show its exercises
        workoutSelector.setOnAction(event -> {
            activeWorkout = workoutSelector.getValue();
            if (activeWorkout != null) {
                exerciseList.setItems(FXCollections.observableArrayList(activeWorkout.getExercises()));
                setTable.setItems(FXCollections.emptyObservableList());
            }
        });

        // When user clicks an exercise, show sets
        exerciseList.getSelectionModel().selectedItemProperty().addListener((obs, oldEx, newEx) -> {
            if (newEx != null) {
                setTable.setItems(FXCollections.observableArrayList(newEx.getSetList()));
            }
        });

        //Setup column bindings
        repsCol.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getReps()).asObject()
        );

        weightCol.setCellValueFactory(data ->
                new SimpleFloatProperty(data.getValue().getWeight()).asObject()
        );

        // Make the table editable
        setTable.setEditable(true);

        // Index column
        setCol.setCellValueFactory(col -> {
            int index = setTable.getItems().indexOf(col.getValue());
            return new ReadOnlyObjectWrapper<>(index + 1);
        });
        setCol.setEditable(false); // optional

        // Reps column
        repsCol.setCellFactory(TextFieldTableCell.forTableColumn(new javafx.util.converter.IntegerStringConverter()));
        repsCol.setOnEditCommit(event -> {
            ExerciseSet set = event.getRowValue();
            set.setReps(event.getNewValue()); // update model

            // Set the text color of the edited cell to black (this is currently not working, will implement in a later version)
            TableCell<ExerciseSet, Integer> cell = (TableCell<ExerciseSet, Integer>) repsCol.getCellFactory().call(repsCol);
            cell.setStyle("-fx-font-weight: bold;");

            setTable.refresh();
        });

        // Weight column
        weightCol.setCellFactory(TextFieldTableCell.forTableColumn(new javafx.util.converter.FloatStringConverter()));
        weightCol.setOnEditCommit(event -> {
            ExerciseSet set = event.getRowValue();
            set.setWeight(event.getNewValue()); // update model
            // Set the text color of the edited cell to black (this is currently not working, will implement in a later version)
            TableCell<ExerciseSet, Float> cell = (TableCell<ExerciseSet, Float>) weightCol.getCellFactory().call(weightCol);
            cell.setStyle("-fx-font-weight: bold;");

            setTable.refresh();
        });



    }

    // ---------- BUTTONS ----------
    @FXML
    private void addSet() {
        Exercise selected = exerciseList.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        ExerciseSet newSet = new ExerciseSet(0, 0f);
        selected.getSetList().add(newSet);

        setTable.getItems().add(newSet);
        setTable.refresh();
    }

    @FXML
    private void removeSet() {
        Exercise selected = exerciseList.getSelectionModel().getSelectedItem();
        ExerciseSet selectedSet = setTable.getSelectionModel().getSelectedItem();

        if (selected == null || selectedSet == null) return;

        selected.getSetList().remove(selectedSet);
        setTable.getItems().remove(selectedSet);
        setTable.refresh();
    }

    @FXML
    private void completeWorkout() {
        // Get current user
        User currentUser = Session.getCurrentUser();
        if (currentUser == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No user logged in");
            alert.setContentText("Please log in before saving a workout.");
            alert.showAndWait();
            return;
        }

        // Make sure a workout is selected
        if (activeWorkout == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Workout Selected");
            alert.setHeaderText("Select a workout first");
            alert.setContentText("Please choose a workout from the dropdown before saving.");
            alert.showAndWait();
            return;
        }

        try (Connection conn = Database.getConnection()) {

            // 1️⃣ Create a new TrackedWorkout
            TrackedWorkout trackedWorkout = new TrackedWorkout();
            trackedWorkout.setName(activeWorkout.getName());

            // 2️⃣ Convert exercises & sets from UI to TrackedExercise & TrackedExerciseSet
            for (Exercise ex : activeWorkout.getExercises()) {
                TrackedExercise trackedEx = new TrackedExercise();
                trackedEx.setName(ex.getName());

                // Copy sets from UI (reps & weight)
                for (ExerciseSet set : ex.getSetList()) {
                    TrackedExerciseSet trackedSet = new TrackedExerciseSet(set.getReps(), set.getWeight());
                    trackedEx.addSet(trackedSet);
                }

                trackedWorkout.addExercise(trackedEx);
            }

            // 3️⃣ Save the tracked workout to DB using DAOs
            TrackedWorkoutDao workoutDao = new TrackedWorkoutDao(conn);
            workoutDao.save(trackedWorkout, currentUser.getId());

            // 4️⃣ Notify user
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Workout Saved");
            alert.setHeaderText("Workout Completed!");
            alert.setContentText("Your tracked workout has been saved successfully.");
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Saving Workout");
            alert.setHeaderText("An error occurred while saving your workout");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    @FXML
    private void handleBackButton(javafx.event.ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/workout_intermediate.fxml"));
        Scene scene = new Scene(parent);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Workouts");
        stage.show();
    }
}
