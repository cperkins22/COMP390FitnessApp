package com.fitnessapp;

public class TrackedExerciseSet {
    private int reps;
    private float weight;

    public TrackedExerciseSet(int reps, float weight) {
        this.reps = reps;
        this.weight = weight;
    }

    public int getReps() { return reps; }
    public void setReps(int reps) { this.reps = reps; }

    public float getWeight() { return weight; }
    public void setWeight(float weight) { this.weight = weight; }
}

