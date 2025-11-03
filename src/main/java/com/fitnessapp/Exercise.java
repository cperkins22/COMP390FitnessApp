package com.fitnessapp;

import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

/**
 * Cashel Stuger 10/2025
 * Exercise class:
 * Represents a single exercise performed during a workout.
 * Each Exercise contains info such as its name, description,
 * and a list of ExerciseSet objects that record the repetitions
 * and weight lifted for each set.
 *
 * Attributes:
 * - id (UUID): Unique identifier automatically generated for each exercise.
 * - name (String): The name of the exercise (e.g., "Bench Press").
 * - description (String): A short summary of the exercise.
 * - sets (int): The total number of sets performed.
 * - repsPerSet (int): Default or planned number of repetitions per set.
 * - setList (List<ExerciseSet>): Stores individual sets with reps and weight.
 *
 * Methods:
 * - addSet(ExerciseSet set): Adds a new set to the exercise.
 * - getTotalWeightLifted(): Calculates total (reps × weight) for all sets.
 * - Getters and Setters: Access or modify exercise data safely.
 */
public class Exercise {

    private UUID id;
    private String name;
    private String description;
    private int sets;
    private int repsPerSet;
    private List<ExerciseSet> setList;

    // Minimal constructor and initialize state
    public Exercise(String name, String description){

        // Generate a random, unique ID for this exercise
        this.id = UUID.randomUUID();

        // Set the name and description provided by whoever creates the object
        this.name = name;
        this.description = description;

        // Initialize these numeric values to 0 by default
        this.sets = 0;
        this.repsPerSet = 0;

        // Create an empty ArrayList so we can later add ExerciseSets to it
        this.setList = new ArrayList<>();

    }


    // Add one ExerciseSet to the exercise
    // e.g. benchPress.addSet(new ExerciseSet(8,135f));
    public void addSet(ExerciseSet set){

        // Check for null / nagtive values
        if (set==null) return;
        if (set.getReps() < 0 || set.getWeight() < 0) return;

        // Add the ExerciseSet object to the list
        setList.add(set);

        // Update the "sets" count so it always matches how many sets we have
        this.sets = setList.size();

    }


    // Calculates the total weight lifted in all the sets
    public float getTotalWeightLifted(){

        // Start total at 0
        float total = 0.0f;

        // Loop through every ExerciseSet object in setList
        for (ExerciseSet s : setList){
            // Add reps*weight to the total
            total += (s.getReps() * s.getWeight());
        }

        // Return final sum
        return total;

    }


    // GETTERS + SETTERS

    public UUID getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public int getSets(){
        return sets;
    }

    public int getRepsPerSet(){
        return repsPerSet;
    }

    public void setRepsPerSet(int repsPerSet){
        this.repsPerSet = repsPerSet;
    }

    public List<ExerciseSet> getSetList(){
        return setList;
    }


}
