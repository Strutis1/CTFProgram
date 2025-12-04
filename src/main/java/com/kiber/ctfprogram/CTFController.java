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

    @FXML
    public void initialize() {
        task1.setTask("Task 1: Hidden in plain sight", "Its dark in here.", "6258a6b7c2910f6fba790341281fce628d894c6992db3d21557773949082a9da");
        task2.setTask("Task 2: Metadata", "Right-click → properties.", "FLAG2");
        task3.setTask("Task 3: Steganography", "Maybe an image?", "FLAG3");
        task4.setTask("Task 4", "Hint 4", "FLAG4");
        task5.setTask("Task 5", "Hint 5", "FLAG5");
        task6.setTask("Task 6", "Hint 6", "FLAG6");
        task7.setTask("Task 7", "Hint 7", "FLAG7");
        task8.setTask("Task 8", "Hint 8", "FLAG8");
        task9.setTask("Task 9", "Hint 9", "FLAG9");
        task10.setTask("Task 10", "Hint 10", "FLAG10");
    }
}
