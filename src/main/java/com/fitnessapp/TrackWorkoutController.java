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
import java.util.Objects;

/**
 * Controller for tracking workouts.
 * Handles displaying available workouts, exercises, and sets.
 * Allows the user to edit sets, add/remove sets, and save a tracked workout.
 */
public class TrackWorkoutController {

    // ---------- FXML UI Elements ----------

    /** Dropdown to select a workout */
    @FXML private ComboBox<Workout> workoutSelector;

    /** List of exercises for the selected workout */
    @FXML private ListView<Exercise> exerciseList;

    /** Table to display exercise sets */
    @FXML private TableView<ExerciseSet> setTable;

    /** Table column for set index */
    @FXML private TableColumn<ExerciseSet, Integer> setCol;

    /** Table column for number of reps */
    @FXML private TableColumn<ExerciseSet, Integer> repsCol;

    /** Table column for weight lifted */
    @FXML private TableColumn<ExerciseSet, Float> weightCol;

    /** Button to add a new set to an exercise */
    @FXML private Button addSetButton;

    /** Button to remove a selected set from an exercise */
    @FXML private Button removeSetButton;

    /** Button to complete and save the tracked workout */
    @FXML private Button completeWorkoutButton;

    // ---------- Data ----------

    /** List of workouts available for the current user */
    private ObservableList<Workout> availableWorkouts;

    /** Currently active workout selected in the UI */
    private Workout activeWorkout;

    /**
     * Initializes the controller.
     * Loads workouts from the database and sets up UI bindings and listeners.
     */
    @FXML
    public void initialize() {
        User currentUser = Session.getCurrentUser();
        if (currentUser == null) {
            System.out.println("ERROR: No user logged in.");
            return;
        }

        availableWorkouts = FXCollections.observableArrayList();

        try {
            WorkoutDao workoutDao = new WorkoutDao();
            availableWorkouts.addAll(workoutDao.findByUserId(currentUser.getId()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        workoutSelector.setItems(availableWorkouts);

        // Show exercises when a workout is selected
        workoutSelector.setOnAction(event -> {
            activeWorkout = workoutSelector.getValue();
            if (activeWorkout != null) {
                exerciseList.setItems(FXCollections.observableArrayList(activeWorkout.getExercises()));
                setTable.setItems(FXCollections.emptyObservableList());
            }
        });

        // Show sets when an exercise is selected
        exerciseList.getSelectionModel().selectedItemProperty().addListener((obs, oldEx, newEx) -> {
            if (newEx != null) {
                setTable.setItems(FXCollections.observableArrayList(newEx.getSetList()));
            }
        });

        setupTableColumns();
    }

    /**
     * Configures table column bindings and makes the setTable editable.
     */
    private void setupTableColumns() {
        repsCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getReps()).asObject());
        weightCol.setCellValueFactory(data -> new SimpleFloatProperty(data.getValue().getWeight()).asObject());

        setTable.setEditable(true);

        setCol.setCellValueFactory(col -> {
            int index = setTable.getItems().indexOf(col.getValue());
            return new ReadOnlyObjectWrapper<>(index + 1);
        });
        setCol.setEditable(false);

        repsCol.setCellFactory(col -> new TableCell<ExerciseSet, Integer>() {
            private TextField textField;

            @Override
            public void startEdit() {
                super.startEdit();
                if (!isEmpty()) {
                    textField = new TextField(getItem() == null ? "" : String.valueOf(getItem()));
                    setGraphic(textField);
                    setText(null);
                    textField.selectAll();
                    textField.requestFocus();

                    textField.setOnAction(e -> {
                        try {
                            commitEdit(Integer.parseInt(textField.getText().trim()));
                        } catch (NumberFormatException ex) {
                            showAlert(Alert.AlertType.ERROR, "Invalid reps", "Please enter a whole number for reps.");
                            cancelEdit();
                        }
                    });

                    textField.focusedProperty().addListener((obs, oldV, newV) -> {
                        if (!newV) {
                            try {
                                commitEdit(Integer.parseInt(textField.getText().trim()));
                            } catch (NumberFormatException ex) {
                                showAlert(Alert.AlertType.ERROR, "Invalid reps", "Please enter a whole number for reps.");
                                cancelEdit();
                            }
                        }
                    });
                }
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getItem() == null ? "" : String.valueOf(getItem()));
                setGraphic(null);
            }

            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else if (isEditing()) {
                    if (textField != null) textField.setText(String.valueOf(item));
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(String.valueOf(item));
                    setGraphic(null);
                }
            }
        });

        repsCol.setOnEditCommit(event -> {
            if (event.getNewValue() != null) {
                event.getRowValue().setReps(event.getNewValue());
                setTable.refresh();
            }
        });

        weightCol.setCellFactory(col -> new TableCell<ExerciseSet, Float>() {
            private TextField textField;

            @Override
            public void startEdit() {
                super.startEdit();
                if (!isEmpty()) {
                    textField = new TextField(getItem() == null ? "" : String.valueOf(getItem()));
                    setGraphic(textField);
                    setText(null);
                    textField.selectAll();
                    textField.requestFocus();

                    textField.setOnAction(e -> {
                        try {
                            commitEdit(Float.parseFloat(textField.getText().trim()));
                        } catch (NumberFormatException ex) {
                            showAlert(Alert.AlertType.ERROR, "Invalid weight", "Please enter a number like 135 or 135.5 for weight.");
                            cancelEdit();
                        }
                    });

                    textField.focusedProperty().addListener((obs, oldV, newV) -> {
                        if (!newV) {
                            try {
                                commitEdit(Float.parseFloat(textField.getText().trim()));
                            } catch (NumberFormatException ex) {
                                showAlert(Alert.AlertType.ERROR, "Invalid weight", "Please enter a number like 135 or 135.5 for weight.");
                                cancelEdit();
                            }
                        }
                    });
                }
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getItem() == null ? "" : String.valueOf(getItem()));
                setGraphic(null);
            }

            @Override
            protected void updateItem(Float item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else if (isEditing()) {
                    if (textField != null) textField.setText(String.valueOf(item));
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(String.valueOf(item));
                    setGraphic(null);
                }
            }
        });

        weightCol.setOnEditCommit(event -> {
            if (event.getNewValue() != null) {
                event.getRowValue().setWeight(event.getNewValue());
                setTable.refresh();
            }
        });
    }

    // ---------- Button Handlers ----------

    /**
     * Adds a new set (0 reps, 0 weight) to the selected exercise.
     */
    @FXML
    private void addSet() {
        Exercise selected = exerciseList.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        ExerciseSet newSet = new ExerciseSet(0, 0f);
        selected.getSetList().add(newSet);
        setTable.getItems().add(newSet);
        setTable.refresh();
    }

    /**
     * Removes the selected set from the selected exercise.
     */
    @FXML
    private void removeSet() {
        Exercise selected = exerciseList.getSelectionModel().getSelectedItem();
        ExerciseSet selectedSet = setTable.getSelectionModel().getSelectedItem();

        if (selected == null || selectedSet == null) return;

        selected.getSetList().remove(selectedSet);
        setTable.getItems().remove(selectedSet);
        setTable.refresh();
    }

    /**
     * Saves the tracked workout to the database.
     * Converts exercises and sets to TrackedExercise/TrackedExerciseSet objects and uses DAOs.
     */
    @FXML
    private void completeWorkout() {
        User currentUser = Session.getCurrentUser();
        if (currentUser == null) {
            showAlert(Alert.AlertType.ERROR, "No user logged in", "Please log in before saving a workout.");
            return;
        }

        if (activeWorkout == null) {
            showAlert(Alert.AlertType.WARNING, "No Workout Selected", "Please choose a workout from the dropdown before saving.");
            return;
        }

        try (Connection conn = Database.getConnection()) {
            TrackedWorkout trackedWorkout = new TrackedWorkout();
            trackedWorkout.setName(activeWorkout.getName());

            for (Exercise ex : activeWorkout.getExercises()) {
                TrackedExercise trackedEx = new TrackedExercise();
                trackedEx.setName(ex.getName());
                for (ExerciseSet set : ex.getSetList()) {
                    TrackedExerciseSet trackedSet = new TrackedExerciseSet(set.getReps(), set.getWeight());
                    trackedEx.addSet(trackedSet);
                }
                trackedWorkout.addExercise(trackedEx);
            }

            TrackedWorkoutDao workoutDao = new TrackedWorkoutDao(conn);
            workoutDao.save(trackedWorkout, currentUser.getId());

            showAlert(Alert.AlertType.INFORMATION, "Workout Saved", "Your tracked workout has been saved successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error Saving Workout", e.getMessage());
        }
    }

    /**
     * Navigates back to the workout intermediate screen.
     * @param event the back button click
     * @throws IOException if the FXML cannot be loaded
     */
    @FXML
    private void handleBackButton(javafx.event.ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/workout_intermediate.fxml")));
        Scene scene = new Scene(parent);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Workouts");
        stage.show();
    }

    /**
     * Helper to show an alert dialog.
     * @param type the type of alert
     * @param header the alert header text
     * @param content the alert content text
     */
    private void showAlert(Alert.AlertType type, String header, String content) {
        Alert alert = new Alert(type);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
