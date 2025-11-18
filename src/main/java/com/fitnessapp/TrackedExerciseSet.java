package com.fitnessapp;

/**
 *
 Class is intended to keep track of exercises primarily based on weight lifted and the amount of reps
 */

public class TrackedExerciseSet {
    /**
     * Establishment of relevant variables
     */
    private int reps;
    private float weight;

    public TrackedExerciseSet(int reps, float weight) {
        this.reps = reps;
        this.weight = weight;
    }

    /**
     * Gathers entered information and stores it for later usage
     */

    public int getReps() { return reps; }
    public void setReps(int reps) { this.reps = reps; }

    public float getWeight() { return weight; }
    public void setWeight(float weight) { this.weight = weight; }
}

