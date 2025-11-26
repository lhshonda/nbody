package com.lhshonda.nbody;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.animation.AnimationTimer;

public class NBodySimulator extends Application {

    // >> WINDOW SIZING
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;

    // >> CALCULATION CONSTANTS
    private static final double SUN_MASS = 330_000;
    private static final double EARTH_MASS = 1;

    // >> CLASS FIELDS
    private PhysicsEngine simulation;   // :: PHYSICS ENGINE
    private GraphicsContext gc;         // :: THE "PEN"
    private AnimationTimer timer;       // :: IMPORTED TIMER
    private boolean isPaused = false;   // :: PLAY STATE
    private ControlPanel uiPanel;       // :: UI PANEL CONTROLS

    // >> MAIN ENTRY POINT
    @Override
    public void start(Stage primaryStage) {

        // >> PROGRAM WINDOW
        BorderPane root = new BorderPane();
        Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.gc = canvas.getGraphicsContext2D();
        root.setCenter(canvas);

        // >> MODELING
        simulation = new PhysicsEngine();
        setupInitialBodies();

        // >> UI PANEL CREATION
        uiPanel = new ControlPanel();
        root.setRight(uiPanel.getRootPanel());

        // >> UI LOGIC
        uiPanel.getPauseButton().setOnAction(e -> togglePause());
        uiPanel.getResetButton().setOnAction(e -> restartSimulation());

        // >> CORE LOOP LOGIC
        timer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                // :: The first frame defined as "now".
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return;
                }

                // :: Calculating time elapse since the last frame.
                // :: Dividing by one billion because there are one billion nanoseconds in one second.
                double deltaTime = (now - lastUpdate) / 1_000_000_000.0;
                lastUpdate = now;

                // >> DELTA TIME MAX THRESHOLD
                if (deltaTime > 0.0166) {
                    deltaTime = 0.0166;
                }

                simulation.update(deltaTime);
                draw();
            }
        };

        // >> BEGIN LOOP
        timer.start();

        // >> JAVAFX DISPLAYING
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setTitle("N-Body Simulation 2D");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // >> DRAW HELPER METHOD
    private void draw() {
        // >> CLEAR CANVAS WITH BLACK BG
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        // >> DRAWING BODIES
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

    // >> TIMER HELPER METHOD
    private void togglePause() {
        if (isPaused) {
            timer.start();
            isPaused = false;
            uiPanel.getPauseButton().setText("Pause");
        } else {
            timer.stop();
            isPaused = true;
            uiPanel.getPauseButton().setText("Play");
        }
    }

    private void setupInitialBodies() {
        simulation.getBodies().clear();

        // >> SUN
        simulation.addBody (new StellarObject(
                WINDOW_WIDTH / 2.0,
                WINDOW_HEIGHT / 2.0,
                0,
                0,
                SUN_MASS,
                20
        ));

        // >> EARTH
        double r_pixels = 256.0;
        double v_orbit = 113.5;

        simulation.addBody(new StellarObject(
                (WINDOW_WIDTH / 2.0) - r_pixels,
                WINDOW_HEIGHT / 2.0,
                0,
                -v_orbit,
                1,
                5
        ));

        // >> RANDOM BODY
        double r2_pixels = 150.0;
        double v2_orbit = Math.sqrt((PhysicsEngine.G * SUN_MASS) / r2_pixels);

        simulation.addBody(new StellarObject(
                (WINDOW_WIDTH / 2.0) + r2_pixels,
                WINDOW_HEIGHT / 2.0,
                0,
                v2_orbit,
                5,
                7
        ));
    }

    private void restartSimulation() {
        timer.stop();
        simulation.getBodies().clear();
        isPaused = false;
        setupInitialBodies();
        timer.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
