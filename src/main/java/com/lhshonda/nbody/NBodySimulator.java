package com.lhshonda.nbody;

import javafx.application.Application; // class managing the lifecycle of the app
import javafx.stage.Stage; // top level window
import javafx.scene.Scene; // content inside the window
import javafx.scene.layout.Pane; // the root container of the canvas
import javafx.scene.canvas.GraphicsContext; // the toolbox used to interact with the canvas
import javafx.scene.paint.Color; // class holding color information; used to tell gc what color to use
import javafx.animation.AnimationTimer; // object with 'handle (long now)' method; typically called 60 times a second

public class NBodySimulator extends Application {

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
