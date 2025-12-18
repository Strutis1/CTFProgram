package com.kiber.ctfprogram;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.prefs.Preferences;

import javafx.stage.FileChooser;


public class TaskController extends VBox {

    private CTFController parent;

    public void setParent(CTFController parent) {
        this.parent = parent;
    }


    @FXML private Label taskTitle;
    @FXML private TextField flagField;
    @FXML private Label hintLabel;
    @FXML private TextArea taskData;


    private String correctFlagHash;
    private String hintText;
    private String title;
    private boolean completed = false;
    private int taskIndex = -1;

    private static final Preferences PREFS =
            Preferences.userRoot().node("com/kiber/ctfprogram");


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


    public void setTask(int index, String title, String content, String hint, String flagHash) {
        this.taskIndex = index;
        this.title = title;
        this.hintText = hint;
        this.correctFlagHash = flagHash;

        taskTitle.setText(title);

        if (taskData != null) {
            if (content != null && !content.isBlank()) {
                taskData.setText(content);
                taskData.setVisible(true);
                taskData.setManaged(true);
            } else {
                taskData.clear();
                taskData.setVisible(false);
                taskData.setManaged(false);
            }
        }

        this.completed = PREFS.getBoolean(completedKey(), false);

        if (completed) {
            applyCompletedUI();
        } else {
            resetUIOnly();
        }
    }


    private String completedKey() {
        return "task.completed." + taskIndex;
    }

    private void applyCompletedUI() {
        hintLabel.setVisible(true);
        hintLabel.setText("(completed)");
        flagField.setEditable(false);

        taskTitle.getStyleClass().remove("task-label");
        taskTitle.getStyleClass().add("task-label-glow");
    }



    private void resetUIOnly() {
        flagField.clear();
        flagField.setEditable(true);
        hintLabel.setVisible(false);
        hintLabel.setText("");

        taskTitle.getStyleClass().remove("task-label-glow");
        if (!taskTitle.getStyleClass().contains("task-label")) {
            taskTitle.getStyleClass().add("task-label");
        }
    }


    public boolean isCompleted() {
        return completed;
    }

    public void reset() {
        completed = false;
        PREFS.putBoolean(completedKey(), false);
        resetUIOnly();
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title != null ? title : "(unnamed task)";
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
            parent.logMessage(getTitle() + " - Correct!");
            completed = true;
            PREFS.putBoolean(completedKey(), true);
            applyCompletedUI();
        } else {
            parent.logMessage(getTitle() + " - Wrong flag.");
        }
    }

    @FXML
    private void exportTaskFile() {
        String resourcePath = null;
        String suggestedName = null;

        switch (taskIndex) {
            case 4:
                resourcePath = "/com/kiber/ctfprogram/taskFiles/task5.png";
                suggestedName = "task5.png";
                break;
            case 5:
                resourcePath = "/com/kiber/ctfprogram/taskFiles/task6.jpg";
                suggestedName = "task6.jpg";
                break;
            case 7:
                resourcePath = "/com/kiber/ctfprogram/taskFiles/task8.txt";
                suggestedName = "task8.txt";
                break;
            default:
                parent.logMessage(getTitle() + " - No file to export for this task.");
                return;
        }

        try (InputStream in = getClass().getResourceAsStream(resourcePath)) {
            if (in == null) {
                parent.logMessage(getTitle() + " - Resource not found: " + resourcePath);
                return;
            }

            FileChooser chooser = new FileChooser();
            chooser.setTitle("Save file for " + getTitle());
            chooser.setInitialFileName(suggestedName);

            File target = chooser.showSaveDialog(this.getScene().getWindow());
            if (target == null) {
                return;
            }

            Files.copy(in, target.toPath(), StandardCopyOption.REPLACE_EXISTING);
            parent.logMessage(getTitle() + " - Exported to: " + target.getAbsolutePath());
        } catch (IOException e) {
            parent.logMessage(getTitle() + " - Failed to export: " + e.getMessage());
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
