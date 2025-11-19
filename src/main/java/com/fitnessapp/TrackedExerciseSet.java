package com.fitnessapp;

/**
 * Represents a single set of an exercise that has been tracked.
 * Stores the number of repetitions and the weight used for this set.
 */
public class TrackedExerciseSet {

    /** Number of repetitions completed in this set. */
    private int reps;

    /** Weight used for this set (in pounds, kilograms, etc.). */
    private float weight;

    /**
     * Constructs a new TrackedExerciseSet with the specified reps and weight.
     * @param reps the number of repetitions
     * @param weight the weight used for the set
     */
    public TrackedExerciseSet(int reps, float weight) {
        this.reps = reps;
        this.weight = weight;
    }

    /**
     * Returns the number of repetitions in this set.
     * @return the number of reps
     */
    public int getReps() {
        return reps;
    }

    /**
     * Sets the number of repetitions for this set.
     * @param reps the number of reps to set
     */
    public void setReps(int reps) {
        this.reps = reps;
    }

    /**
     * Returns the weight used in this set.
     * @return the weight value
     */
    public float getWeight() {
        return weight;
    }

    /**
     * Sets the weight used for this set.
     * @param weight the weight value to set
     */
    public void setWeight(float weight) {
        this.weight = weight;
    }
}//class end
