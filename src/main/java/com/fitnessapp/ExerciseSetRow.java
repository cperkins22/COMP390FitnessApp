package com.fitnessapp;

/**
 * A small helper class to help with displaying archived workout sets
 */
import javafx.beans.property.*;

public class ExerciseSetRow {
    private final StringProperty exerciseName;
    private final IntegerProperty setNumber;
    private final IntegerProperty reps;
    private final FloatProperty weight;

    public ExerciseSetRow(String exerciseName, int setNumber, int reps, float weight) {
        this.exerciseName = new SimpleStringProperty(exerciseName);
        this.setNumber = new SimpleIntegerProperty(setNumber);
        this.reps = new SimpleIntegerProperty(reps);
        this.weight = new SimpleFloatProperty(weight);
    }

    public StringProperty exerciseNameProperty() { return exerciseName; }
    public IntegerProperty setNumberProperty() { return setNumber; }
    public IntegerProperty repsProperty() { return reps; }
    public FloatProperty weightProperty() { return weight; }
}
