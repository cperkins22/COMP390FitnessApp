package com.fitnessapp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DailyLog {
    private UUID ID;
    private LocalDate date;
    private UUID userID;
    private List<Meal> meals;
    private List<Workout> workouts;

    public DailyLog(LocalDate date, UUID userID) {
        this.ID = UUID.randomUUID();
        this.date = date;
        this.userID = userID;
        this.meals = new ArrayList<>();
        this.workouts = new ArrayList<>();
    }

    public UUID getId() {
        return ID;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public UUID getUserID() {
        return userID;
    }

    public List<Meal> getMeals() {
        return new ArrayList<>(meals);
    }

    public List<Workout> getWorkouts() {
        return new ArrayList<>(workouts);
    }

    public boolean addMeal(Meal meal) {
        if (meal == null) {
            return false;
        }
        return meals.add(meal);
    }

    public Meal removeMeal(int index) {
        if (index >= 0 && index < meals.size()) {
            return meals.remove(index);
        }
        return null;
    }

    public void clearMeals() {
        meals.clear();
    }

    public int getWorkoutCount() {
        return workouts.size();
    }

    public String getWorkoutSummary() {
        if (workouts.isEmpty()) {
            return "No workouts logged for this day.";
        }

        StringBuilder summary = new StringBuilder();
        summary.append("Workout Summary for ").append(date).append(":\n");
        summary.append("----------------------------------------\n");

        for (int i = 0; i < workouts.size(); i++) {
            Workout workout = workouts.get(i);
            summary.append(String.format("%d. Workout on %s\n",
                    i + 1,
                    workout.getDate()));
        }

        summary.append("----------------------------------------\n");
        summary.append(String.format("Total workouts: %d\n", workouts.size()));

        return summary.toString();
    }

    public boolean addWorkout(Workout workout) {
        if (workout == null) {
            return false;
        }
        return workouts.add(workout);
    }

    public Workout removeWorkout(int index) {
        if (index >= 0 && index < workouts.size()) {
            return workouts.remove(index);
        }
        return null;
    }

    public void clearWorkouts() {
        workouts.clear();
    }

    public boolean hasWorkouts() {
        return !workouts.isEmpty();
    }

    public int getMealCount() {
        return meals.size();
    }

    public int getTotalCalories() {
        int total = 0;
        for (Meal meal : meals) {
            total += meal.getCalories();
        }
        return total;
    }

    public float getTotalProtein() {
        float total = 0.0f;
        for (Meal meal : meals) {
            total += meal.getProtein();
        }
        return total;
    }

    public float getTotalCarbs() {
        float total = 0.0f;
        for (Meal meal : meals) {
            total += meal.getCarbs();
        }
        return total;
    }

    public float getTotalFats() {
        float total = 0.0f;
        for (Meal meal : meals) {
            total += meal.getFat();
        }
        return total;
    }

    public String getDailySummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("===========================================\n");
        summary.append(String.format("Daily Log for %s\n", date));
        summary.append("===========================================\n\n");

        summary.append("NUTRITION SUMMARY:\n");
        summary.append(String.format("  Meals logged: %d\n", getMealCount()));
        summary.append(String.format("  Total Calories: %d kcal\n", getTotalCalories()));
        summary.append(String.format("  Protein: %.1f g\n", getTotalProtein()));
        summary.append(String.format("  Carbohydrates: %.1f g\n", getTotalCarbs()));
        summary.append(String.format("  Fats: %.1f g\n\n", getTotalFats()));

        summary.append("WORKOUT SUMMARY:\n");
        if (hasWorkouts()) {
            summary.append(String.format("  Workouts completed: %d\n", getWorkoutCount()));
            summary.append(getWorkoutSummary());
        } else {
            summary.append("  No workouts logged.\n");
        }

        return summary.toString();
    }

    @Override
    public String toString() {
        return String.format("DailyLog{id=%s, date=%s, userID=%s, meals=%d, workouts=%d}",
                ID, date, userID, meals.size(), workouts.size());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DailyLog other = (DailyLog) obj;
        return ID.equals(other.ID);
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }
}
