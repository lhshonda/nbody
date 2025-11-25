package com.lhshonda.nbody;

import javafx.application.Application; // class managing the lifecycle of the app
import javafx.stage.Stage; // top level window
import javafx.scene.Scene; // content inside the window
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane; // the root container of the canvas
import javafx.scene.canvas.GraphicsContext; // the toolbox used to interact with the canvas
import javafx.scene.paint.Color; // class holding color information; used to tell gc what color to use
import javafx.animation.AnimationTimer; // object with 'handle (long now)' method; typically called 60 times a second

public class NBodySimulator extends Application {

    // defining some constants for window size
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;

    // simulation constants
    private static final double SUN_MASS = 330_000;
    private static final double EARTH_MASS = 1;

    // class fields
    private PhysicsEngine simulation;

    // creating the 'pen'
    private GraphicsContext gc;

    // tells the compiler this method overrides a method declared in a superclass and acts as a form of protection against typos
    @Override
    public void start(Stage stage) {
        // this is the "root node", aka the scene graph foundation
        Pane root = new Pane(); // pane is the simplest layout container
        Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT); // defining the new canvas with the field values
        this.gc = canvas.getGraphicsContext2D(); // getting the 'pen'
        root.getChildren().add(canvas); // adding the canvas to the root container

        // create the engine
        simulation = new PhysicsEngine();

        // create the sun
        StellarObject sun = new StellarObject(
                WINDOW_WIDTH / 2.0,
                WINDOW_HEIGHT / 2.0,
                0,
                0,
                SUN_MASS,
                20
        );
        simulation.addBody(sun);

        // create the earth
        StellarObject earth = new StellarObject(
                WINDOW_WIDTH / 4.0,
                WINDOW_HEIGHT / 2.0,
                0,
                -40,
                EARTH_MASS,
                5
        );
        simulation.addBody(earth);

        AnimationTimer timer = new AnimationTimer() {
            // creating a variable inside the timer in nanoseconds
            private long lastUpdate = 0;


            @Override // replacing a method from the parent class
            public void handle(long now) {

                // set lastUpdate on the first frame to zero in order to attain deltaTime
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return;
                }

                /* calculate time elapsed since last frame; dividing by one billion because there are
                one billion nanoseconds in one second; the physics formulas rely on seconds */
                double deltaTime = (now - lastUpdate) / 1_000_000_000.0;
                lastUpdate = now;

                // capping deltaTime to 1/60th of a second
                if (deltaTime > 0.0166) {
                    deltaTime = 0.0166;
                }

                simulation.update(deltaTime);
                draw();
            }
        };
        timer.start();

        // the content inside the window is the scene
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        // giving the application window a name
        stage.setTitle("N-Body Simulation 2D");

        // attaches the scene to the window
        stage.setScene(scene);

        // tells java to create the OS-level window, display it on the screen, and to start rendering
        stage.show();
    }

    // helper method for handler() from AnimationTimer
    private void draw() {
        // clear entire screen with black bg
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        // draw color for the bodies
        for (StellarObject body : simulation.getBodies()) {
            if (body.getMass() > 300_000) {
                gc.setFill(Color.YELLOW);
            } else {
                gc.setFill(Color.AQUAMARINE);
            }

            double r = body.getRadius();
            double diameter = r * 2;

            gc.fillOval(body.getX() - r, body.getY() - r, diameter, diameter);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
