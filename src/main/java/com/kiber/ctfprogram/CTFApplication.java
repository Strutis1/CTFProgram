package com.kiber.ctfprogram;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CTFApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(CTFApplication.class.getResource("main-view.fxml"));

        Scene scene = new Scene(loader.load());

        stage.setTitle("Mano CTF");

        stage.setScene(scene);

        stage.setResizable(true);

        stage.setMinWidth(900);
        stage.setMinHeight(700);

        stage.show();
    }
}
