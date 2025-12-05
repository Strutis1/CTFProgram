package com.kiber.ctfprogram;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CTFController {

    @FXML private TaskController task1;
    @FXML private TaskController task2;
    @FXML private TaskController task3;
    @FXML private TaskController task4;
    @FXML private TaskController task5;
    @FXML private TaskController task6;
    @FXML private TaskController task7;
    @FXML private TaskController task8;
    @FXML private TaskController task9;
    @FXML private TaskController task10;

    @FXML private TextArea terminal;
    @FXML private TextField terminalEntry;
    @FXML private TabPane tabPane;

    private TaskController[] tasks;

    String[] titles = {
            "Task 1: Hidden in plain sight",
            "Task 2: Metadata",
            "Task 3: Steganography",
            "Task 4",
            "Task 5",
            "Task 6",
            "Task 7",
            "Task 8",
            "Task 9",
            "Task 10"
    };

    String[] hints = {
            "It's dark in here.",
            "Right-click → properties.",
            "Maybe an image?",
            "Hint 4",
            "Hint 5",
            "Hint 6",
            "Hint 7",
            "Hint 8",
            "Hint 9",
            "Hint 10"
    };

    String[] hashes = {
            "b9de59896c87e0d5a8ee2e299d2680d24f971ce4e195de231b002a55aa4d3c1c",
            "FLAG2", "FLAG3", "FLAG4", "FLAG5",
            "FLAG6", "FLAG7", "FLAG8", "FLAG9", "FLAG10"
    };


    @FXML
    public void initialize() {

        tasks = new TaskController[] {
                task1, task2, task3, task4, task5,
                task6, task7, task8, task9, task10
        };


        for (TaskController t : tasks) {
            t.setParent(this);
        }

        for (int i = 0; i < tasks.length; ++i) {
            tasks[i].setTask(i, titles[i], hints[i], hashes[i]);
        }

        terminalEntry.setOnAction(e -> {
            String cmd = terminalEntry.getText().trim();
            processCommand(cmd);
            terminalEntry.clear();
        });
    }


    public void logMessage(String message) {
        terminal.appendText(message + "\n");
    }

    public void processCommand(String command) {
        logMessage(">" + command + "\n");
        switch (command) {
            case "/completed": {
                StringBuilder sb = new StringBuilder("Completed tasks:\n");
                boolean any = false;
                for (TaskController t : tasks) {
                    if (t.isCompleted()) {
                        any = true;
                        sb.append(" - ").append(t.getTitle()).append("\n");
                    }
                }
                if (!any) {
                    sb.append(" (none yet)");
                }
                logMessage(sb.toString());
                break;
            }

            case "/reset": {
                for (TaskController t : tasks) {
                    t.reset();
                }
                logMessage("All tasks have been reset.");
                break;
            }

            case "/help": {
                StringBuilder sb = new StringBuilder();
                sb.append("Commands:\n")
                        .append(" /completed - list completed tasks\n")
                        .append(" /reset     - reset all tasks\n")
                        .append(" /help      - show this help\n")
                        .append(" /finalize  - check if all tasks are done and move to registration\n");
                logMessage(sb.toString());
                break;
            }

            case "/finalize": {
                boolean allDone = true;
                StringBuilder sb = new StringBuilder();
                for (TaskController t : tasks) {
                    if (!t.isCompleted()) {
                        allDone = false;
                        sb.append("Not completed: ").append(t.getTitle()).append("\n");
                    }
                }
                if (allDone) {
                    logMessage("All tasks completed! Valio!");
                    initiateRegistration();
                } else {
                    logMessage(sb.toString());
                }
                break;
            }

            default:
                logMessage("Unknown command. Try /help");
        }
    }

    private void initiateRegistration() {
    }
}
