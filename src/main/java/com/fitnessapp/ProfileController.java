package com.fitnessapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

public class ProfileController implements Initializable {

    // These fx:id values must match profile.fxml labels
    @FXML private Label firstNameLabel;
    @FXML private Label lastNameLabel;
    @FXML private Label heightLabel;     // inches
    @FXML private Label weightLabel;     // pounds
    @FXML private Label userIdLabel;     // UUID
    @FXML private Label bmiLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        User user = Session.getCurrentUser();
        if (user == null) {
            // Fallback (or redirect to login if you prefer)
            firstNameLabel.setText("-");
            lastNameLabel.setText("-");
            heightLabel.setText("-");
            weightLabel.setText("-");
            userIdLabel.setText("-");
            bmiLabel.setText("-");
            return;
        }

        firstNameLabel.setText(safe(user.getFirstName()));
        lastNameLabel.setText(safe(user.getLastName()));
        heightLabel.setText(String.format("%.0f in", user.getHeight()));   // stored in inches
        weightLabel.setText(String.format("%.0f lb", user.getWeight()));   // stored in pounds
        userIdLabel.setText(safeUUID(user.getId()));

        // If your User.getBMI() already exists and uses 703 * lb / in^2, you can call that.
        // Otherwise compute explicitly with your units:
        float h = user.getHeight();
        float w = user.getWeight();
        float bmi = (h > 0) ? (703.0f * w / (h * h)) : 0.0f;
        bmiLabel.setText(String.format("%.1f", bmi));
    }

    private String safe(String s) { return s == null ? "" : s; }
    private String safeUUID(UUID id) { return id == null ? "" : id.toString(); }

}