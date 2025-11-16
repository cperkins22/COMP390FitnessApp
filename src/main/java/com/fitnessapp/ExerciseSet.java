package com.fitnessapp;

/**
 * Represents a single set within an exercise.
 * Stores the number of reps and the weight used.
 */
public class ExerciseSet {

    /** Number of repetitions completed in this set. */
    private int reps;

    /** Weight used in this set (in pounds, kilograms, etc.). */
    private float weight;

    /** A flag to check if the user has entered their reps for this set */
    private boolean repsEntered = false;

    /** A flag to check if the user has entered their weight for this set */
    private boolean weightEntered = false;

    /**
     * Constructs a new ExerciseSet with the given reps and weight.
     *
     * @param reps the number of repetitions
     * @param weight the weight used for this set
     */
    public ExerciseSet(int reps, float weight) {
        this.reps = reps;
        this.weight = weight;
    }

    /**
     * Gets the number of repetitions in this set.
     * @return the number of reps
     */
    public int getReps() {
        return reps;
    }

    /**
     * Sets the number of repetitions in this set.
     * @param reps the new number of reps
     */
    public void setReps(int reps){
        this.reps = reps;
    }

    /**
     * Gets the weight used in this set.
     * @return the weight value
     */
    public float getWeight() {
        return weight;
    }

    /**
     * Sets the weight used in this set.
     * @param weight  the weight value to be set
     */
    public void setWeight(float weight) {
        this.weight = weight;
    }

    /**
     * Gets whether the user has logged their workout reps into this set.
     * @return the flag value
     */
    public boolean getRepsEntered() {
        return repsEntered;
    }

    /**
     * Sets the repsEntered flag for this set.
     * @param repsEntered  the repsEntered flag position to be set
     */
    public void setRepsEntered(boolean repsEntered) {
        this.repsEntered = repsEntered;
    }

    /**
     * Gets whether the user has logged their workout weight into this set.
     * @return the flag value
     */
    public boolean getWeightEntered() {
        return weightEntered;
    }

    /**
     * Sets the weightEntered flag for this set.
     * @param weightEntered  the weightEntered flag position to be set
     */
    public void setWeightEntered(boolean weightEntered) {
        this.weightEntered = weightEntered;
    }

}
