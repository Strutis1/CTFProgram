package com.kiber.ctfprogram;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CTFController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
