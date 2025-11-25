package com.lhshonda.nbody;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class nBodyApp extends Application {

    // tells the compiler this method overrides a method declared in a superclass and acts as a form of protection against typos
    @Override
    public void start(Stage stage) {
        // this is the "root node", aka the scene graph foundation
        Pane root = new Pane();
        root.setStyle("-fx-background-color: black");

        // the content inside the window is the scene
        Scene scene = new Scene(root, 800, 600);

        // giving the application window a name
        stage.setTitle("N-Body (black screen)");

        // attaches the scene to the window
        stage.setScene(scene);

        // tells java to create the OS-level window, display it on the screen, and to start rendering
        stage.show();
    }
}
