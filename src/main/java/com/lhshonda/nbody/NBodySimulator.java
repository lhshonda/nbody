package com.lhshonda.nbody;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.animation.AnimationTimer;

public class NBodySimulator extends Application {

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
    private Camera camera;
    private InputHandler inputHandler;

    // >> MAIN ENTRY POINT
    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas();

        // >> UI PANEL CREATION
        uiPanel = new ControlPanel();
        VBox controlPanelBox = uiPanel.getRootPanel();

        // >> PROGRAM WINDOW
        StackPane root = new StackPane();
        root.getChildren().addAll(canvas, controlPanelBox);
        StackPane.setAlignment(controlPanelBox, Pos.TOP_LEFT);
        controlPanelBox.setPickOnBounds(false);

        // >> CANVAS RESIZING
        canvas.widthProperty().bind(primaryStage.widthProperty());
        canvas.heightProperty().bind(primaryStage.heightProperty());

        // >> GET GRAPHICAL CONTEXT
        gc = canvas.getGraphicsContext2D();

        // >> MODELING
        simulation = new PhysicsEngine();
        setupInitialBodies();

        // >> CREATE AND STAGE SCENE
        Scene scene = new Scene(root, 1024, 768);
        primaryStage.setTitle("N-Body Simulation 2D");
        primaryStage.setScene(scene);
//        primaryStage.show();

        // >> CAMERA INITIALIZATION
        // :: Created after the scene in order to know the starting size.
        this.camera = new Camera(scene.getWidth(), scene.getHeight(), AU);

        // >> INPUT HANDLER INITIALIZATION
        this.inputHandler = new InputHandler(camera, simulation, this);
        this.inputHandler.attach(canvas);

        // >> RESIZE LISTENER
        canvas.widthProperty().addListener((observable, oldValue, newValue) -> {
            camera.onResize(newValue.doubleValue(), gc.getCanvas().getHeight(), AU);
            draw();
        });
        canvas.heightProperty().addListener((observable, oldValue, newValue) -> {
            camera.onResize(gc.getCanvas().getWidth(), newValue.doubleValue(), AU);
        });

        // >> DISPLAY
        primaryStage.show();

        // >> UI LOGIC
        uiPanel.getPauseButton().setOnAction(e -> togglePause());
        uiPanel.getResetButton().setOnAction(e -> restartSimulation());
        uiPanel.getGravitySlider().valueProperty().addListener(
                (observable, oldVal, newVal) -> {
                    PhysicsEngine.G = PhysicsEngine.G_DEFAULT * newVal.doubleValue();
                }
        );

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
    }

    // >> DRAW HELPER METHOD
    public void draw() {

        // >> CLEAR CANVAS WITH BLACK BG
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        // >> DRAWING BODIES
        for (StellarObject body : simulation.getBodies()) {
            // >> GET REAL WORLD POSITION
            double worldX = body.getX();
            double worldY = body.getY();

            // >> CONVERT TO PIXELS
            double screenX = (worldX * camera.getScale()) + camera.getOffsetX();
            double screenY = (-worldY * camera.getScale()) + camera.getOffsetY();

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

        simulation.addBody (new StellarObject(
                AU * 0.8,
                0,
                0,
                EARTH_VELOCITY,
                EARTH_MASS,
                EARTH_RADIUS
        ));
    }

    private void restartSimulation() {
        timer.stop();
        setupInitialBodies();
        isPaused = false;
        uiPanel.getPauseButton().setText("Pause");

        PhysicsEngine.G = PhysicsEngine.G_DEFAULT;
        uiPanel.getGravitySlider().setValue(1.0);

        timer.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
