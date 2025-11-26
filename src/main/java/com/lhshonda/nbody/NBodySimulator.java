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

    // >> CAMERA FIELDS
    private double scale;
    private double screenOffsetX;
    private double screenOffsetY;

    // >> CONSTANTS
    private static final double AU = 1.496e11;
    private static final double SUN_MASS = 1.989e30;
    private static final double EARTH_MASS = 5.972e24;
    private static final double SUN_RADIUS = 6.963e8;
    private static final double EARTH_RADIUS = 6.371e6;
    private static final double EARTH_VELOCITY = 29780;
    private static final double TIME_STEP = 6 * 3600.0;

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

        // >> CAMERA INITIALIZATION
        this.scale = WINDOW_WIDTH / (AU * 4.5);
        this.screenOffsetX = WINDOW_WIDTH / 2.0;
        this.screenOffsetY = WINDOW_HEIGHT / 2.0;

        // >> CORE LOOP LOGIC
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                simulation.update(TIME_STEP);
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
            // >> GET REAL WORLD POSITION
            double worldX = body.getX();
            double worldY = body.getY();

            // >> CONVERT TO PIXELS
            double screenX = (worldX * scale) + screenOffsetX;
            double screenY = (-worldY * scale) + screenOffsetY;

            double visualRadius;

            if (body.getMass() > SUN_MASS / 2) {
                gc.setFill(Color.YELLOW);
                visualRadius = 10;
            } else {
                gc.setFill(Color.AQUAMARINE);
                visualRadius = 3;
            }

            gc.fillOval(
                    screenX - visualRadius,
                    screenY - visualRadius,
                    visualRadius * 2,
                    visualRadius * 2
            );
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
        simulation.addBody(new StellarObject(
                0,
                0,
                0,
                0,
                SUN_MASS,
                SUN_RADIUS
        ));

        // >> EARTH
        simulation.addBody (new StellarObject(
                AU,
                0,
                0,
                EARTH_VELOCITY,
                EARTH_MASS,
                EARTH_RADIUS
        ));

//        simulation.addBody (new StellarObject(
//                AU * 0.8,
//                0,
//                0,
//                EARTH_VELOCITY,
//                EARTH_MASS,
//                EARTH_RADIUS
//        ));
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
