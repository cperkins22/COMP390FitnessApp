package com.fitnessapp;

import com.fitnessapp.User;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        User user = getUserData(scanner);
        System.out.println("\nUser created:");
        System.out.println(user.toString());

        System.out.println("\nTesting getters with your input:");
        System.out.println("First Name: " + user.getFirstName());
        System.out.println("Last Name: " + user.getLastName());
        System.out.println("Full Name: " + user.getFullName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Height: " + user.getHeight() + " inches");
        System.out.println("Weight: " + user.getWeight() + " pounds");
        System.out.println("ID: " + user.getId());

        System.out.println("\nTesting getBMI():");
        System.out.println("Your BMI: " + user.getBMI());

        System.out.println("\nTesting updateStats():");
        System.out.print("Enter new height (inches): ");
        float newHeight = scanner.nextFloat();
        System.out.print("Enter new weight (pounds): ");
        float newWeight = scanner.nextFloat();
        int result = user.updateStats(newHeight, newWeight);
        System.out.println("Update result: " + result + " (0 = success)");
        System.out.println("After update: " + user.toString());

        scanner.close();
    }

    public static User getUserData(Scanner scanner) {
        System.out.print("First name: ");
        String firstName = scanner.nextLine();

        System.out.print("Last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Height (inches): ");
        float height = scanner.nextFloat();

        System.out.print("Weight (pounds): ");
        float weight = scanner.nextFloat();

        return new User(firstName, lastName, email, height, weight);
    }
}