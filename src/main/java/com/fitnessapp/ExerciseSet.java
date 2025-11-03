package com.fitnessapp;


// Store data for ONE set (e.g. 8 reps @135 lbs)
public class ExerciseSet {

    private int reps;
    private float weight;

    // Constructor
    ExerciseSet(int reps, float weight){

        this.reps = reps;
        this.weight = weight;
    }

    public int getReps(){
        return reps;
    }

    public float getWeight(){
        return weight;
    }
}
