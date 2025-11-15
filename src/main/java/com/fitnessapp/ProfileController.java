package com.fitnessapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

public class ProfileController implements Initializable {

    @FXML private Label firstNameLabel;
    @FXML private Label lastNameLabel;
    @FXML private Label heightLabel;
    @FXML private Label weightLabel;
    @FXML private Label userIdLabel;
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
        heightLabel.setText(String.format("%.0f in", user.getHeight()));
        weightLabel.setText(String.format("%.0f lb", user.getWeight()));
        userIdLabel.setText(safeUUID(user.getId()));

        float h = user.getHeight();
        float w = user.getWeight();
        float bmi = (h > 0) ? (703.0f * w / (h * h)) : 0.0f;
        bmiLabel.setText(String.format("%.1f", bmi));
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }

    private String safeUUID(UUID id) {
        return id == null ? "" : id.toString();
    }
}