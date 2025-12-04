package com.kiber.ctfprogram;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TaskController extends VBox {

    @FXML private Label taskTitle;
    @FXML private TextField flagField;
    @FXML private Label hintLabel;

    private String correctFlagHash;
    private String hintText;

    public TaskController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("task-view.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load task-view.fxml", e);
        }
    }

    public void setTask(String title, String hint, String flagHash) {
        taskTitle.setText(title);
        hintText = hint;
        correctFlagHash = flagHash;
    }

    @FXML
    private void showHint() {
        hintLabel.setText(hintText);
        hintLabel.setVisible(true);
    }

    @FXML
    private void submitFlag() {
        String entered = flagField.getText().trim();
        String enteredHash = sha256(entered);

        if (enteredHash.equalsIgnoreCase(correctFlagHash)) {
            System.out.println("Correct!");
        } else {
            System.out.println("Wrong flag.");
        }
    }

    private static String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
