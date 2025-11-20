package com.fitnessapp;

import javafx.beans.property.*;

/**
 * A small helper class to help with displaying archived workout sets
 * Represents a single row in a table view for displaying archived workout sets.
 * Stores the exercise name, set number, reps, and weight using JavaFX properties.
 */
public class ExerciseSetRow {

    /** Name of the exercise. */
    private final StringProperty exerciseName;

    /** Set number within the exercise. */
    private final IntegerProperty setNumber;

    /** Number of repetitions in the set. */
    private final IntegerProperty reps;

    /** Weight used in the set. */
    private final FloatProperty weight;

    /**
     * Constructs a new ExerciseSetRow with the specified values.
     *
     * @param exerciseName the name of the exercise
     * @param setNumber the set number
     * @param reps the number of repetitions
     * @param weight the weight used in the set
     */
    public ExerciseSetRow(String exerciseName, int setNumber, int reps, float weight) {
        this.exerciseName = new SimpleStringProperty(exerciseName);
        this.setNumber = new SimpleIntegerProperty(setNumber);
        this.reps = new SimpleIntegerProperty(reps);
        this.weight = new SimpleFloatProperty(weight);
    }

    /**
     * Gets the property representing the exercise name.
     *
     * @return the exercise name property
     */
    public StringProperty exerciseNameProperty() {
        return exerciseName;
    }

    /**
     * Gets the property representing the set number.
     *
     * @return the set number property
     */
    public IntegerProperty setNumberProperty() {
        return setNumber;
    }

    /**
     * Gets the property representing the number of repetitions.
     *
     * @return the reps property
     */
    public IntegerProperty repsProperty() {
        return reps;
    }

    /**
     * Gets the property representing the weight used in the set.
     *
     * @return the weight property
     */
    public FloatProperty weightProperty() {
        return weight;
    }
}
