package com.fitnessapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class WorkoutArchiveController {

    // ---------- FXML ----------
    @FXML private ListView<TrackedWorkout> workoutListView;
    @FXML private ListView<TrackedExercise> exerciseListView;
    @FXML private TableView<ExerciseSetRow> setTable;
    @FXML private TableColumn<ExerciseSetRow, Integer> setNumberCol;
    @FXML private TableColumn<ExerciseSetRow, Integer> repsCol;
    @FXML private TableColumn<ExerciseSetRow, Float> weightCol;

    // ---------- Data ----------
    private ObservableList<TrackedWorkout> workouts;

    @FXML
    public void initialize() {
        workouts = FXCollections.observableArrayList();

        try (Connection conn = Database.getConnection()) {
            UUID userId = Session.getCurrentUser().getId();

            TrackedWorkoutDao workoutDao = new TrackedWorkoutDao(conn);
            List<TrackedWorkout> loadedWorkouts = workoutDao.findByUserId(userId);

            // Sort by date descending (most recent first)
            loadedWorkouts.sort(Comparator.comparing(TrackedWorkout::getDateCompleted).reversed());
            workouts.addAll(loadedWorkouts);

            workoutListView.setItems(workouts);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // ---------- Table columns ----------
        setNumberCol.setCellValueFactory(data -> data.getValue().setNumberProperty().asObject());
        repsCol.setCellValueFactory(data -> data.getValue().repsProperty().asObject());
        weightCol.setCellValueFactory(data -> data.getValue().weightProperty().asObject());

        // ---------- Workout selection ----------
        workoutListView.getSelectionModel().selectedItemProperty().addListener((obs, oldWorkout, newWorkout) -> {
            if (newWorkout != null) {
                // Populate exercises for this workout
                exerciseListView.setItems(FXCollections.observableArrayList(newWorkout.getExercises()));
                setTable.setItems(FXCollections.emptyObservableList());
            }
        });

        // ---------- Exercise selection ----------
        exerciseListView.getSelectionModel().selectedItemProperty().addListener((obs, oldEx, newEx) -> {
            if (newEx != null) {
                ObservableList<ExerciseSetRow> rows = FXCollections.observableArrayList();
                List<TrackedExerciseSet> sets = newEx.getSets();

                for (int i = 0; i < sets.size(); i++) {
                    TrackedExerciseSet set = sets.get(i);
                    rows.add(new ExerciseSetRow(newEx.getName(), i + 1, set.getReps(), set.getWeight()));
                }

                setTable.setItems(rows);
            }
        });
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
