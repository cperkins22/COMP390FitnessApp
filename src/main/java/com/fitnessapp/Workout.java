package com.fitnessapp;

import java.util.List;

public class Workout {
    //init variables
    private int id;
    private int date;
    private String notes;
    private List<Exercise> exercises;

    //blank constructor for now
    public Workout(){
    }

    public int getId(){
        return id;
    }

    public int getDate(){
        return date;
    }
    public void setDate(int date){
        this.date = date;
    }

    public String getNotes(){
        return notes;
    }
    public void setNotes(String notes){
        this.notes = notes;
    }

    public List<Exercise> getExercises(){
        return exercises;
    }
    public void setExercises(List<Exercise> exercises){
        this.exercises = exercises;
    }

    public void addExercise(Exercise exercise){
        exercises.add(exercise);
    }

    public float getTotalVolume(){
        float total = 0;
        for (Exercise exercise : exercises){
            for (ExerciseSet set : exercise.getSetList()){
                total += set.getReps() * set.getWeight();
            }
        }
        return total;
    }
}