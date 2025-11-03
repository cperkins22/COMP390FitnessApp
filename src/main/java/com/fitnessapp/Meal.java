package com.fitnessapp;

import java.util.UUID;
import java.util.Date;

public class Meal {

    private UUID id;
    private Date date;
    private String name;
    private int calories;
    private double protein;
    private double carbs;
    private double fat;

    // Constructor - creates a new meal with just a name
    // e.g. Meal breakfast = new Meal("Scrambled Eggs");
    public Meal(String name){

        // Generate a random, unique ID for this meal
        this.id = UUID.randomUUID();

        // Set the date to right now (when the meal was created)
        this.date = new Date();

        // Set the name provided by whoever creates the object
        this.name = name;

        // Initialize all nutrition values to 0 by default
        // User can set these later with setters
        this.calories = 0;
        this.protein = 0.0;
        this.carbs = 0.0;
        this.fat = 0.0;

    }


    // Returns all the macros as a simple string
    // Makes it easy to display or log the nutrition info
    public String getTotalMacros(){

        // Build a string with all the macro values
        String macros = "Protein: " + protein + "g, Fat: " + fat + "g, Carbs: " + carbs + "g";

        // Return the formatted string
        return macros;

    }


    // Print out a nice summary of the meal
    // Gets called automatically when you do System.out.println(meal)
    public String toString(){
        return name + " - " + calories + " calories (" +
                protein + "g protein, " + carbs + "g carbs, " + fat + "g fat)";
    }


    // GETTERS + SETTERS

    public UUID getId(){
        return id;
    }

    public Date getDate(){
        return date;
    }

    public void setDate(Date date){
        this.date = date;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getCalories(){
        return calories;
    }

    public void setCalories(int calories){
        this.calories = calories;
    }

    public double getProtein(){
        return protein;
    }

    public void setProtein(double protein){
        this.protein = protein;
    }

    public double getCarbs(){
        return carbs;
    }

    public void setCarbs(double carbs){
        this.carbs = carbs;
    }

    public double getFat(){
        return fat;
    }

    public void setFat(double fat){
        this.fat = fat;
    }


}
