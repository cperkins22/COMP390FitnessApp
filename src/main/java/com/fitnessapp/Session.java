package com.fitnessapp;

/**
 * Simple session manager to track the currently logged-in user.
 * Uses a static field to maintain user state across the application.
 */
public class Session {

    private static User currentUser;

    /**
     * Gets the currently logged-in user.
     * @return the current user, or null if no user is logged in
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Sets the currently logged-in user.
     * @param user the user to set as current
     */
    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    /**
     * Clears the current user session (logout).
     */
    public static void clear() {
        currentUser = null;
    }
}
