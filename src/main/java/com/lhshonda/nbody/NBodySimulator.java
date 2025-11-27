package com.lhshonda.nbody;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
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
    private PhysicsEngine simulation;
    private AnimationTimer timer;           // :: IMPORTED TIMER
    private boolean isPaused = false;       // :: PLAY STATE
    private ControlPanel uiPanel;           // :: UI PANEL CONTROLS
    private ISimulationView currentView;    // :: INTERFACE CONTRACT
    private StackPane root;                 // :: ROOT NODE

    // >> MAIN ENTRY POINT
    @Override
    public void start(Stage primaryStage) {

        // >> UI PANEL CREATION
        uiPanel = new ControlPanel();
        VBox controlPanelBox = uiPanel.getRootPanel();

        // >> PROGRAM WINDOW
        root = new StackPane();
        root.getChildren().add(controlPanelBox);
        StackPane.setAlignment(controlPanelBox, Pos.TOP_LEFT);
        controlPanelBox.setPickOnBounds(false);

        // >> MODELING
        simulation = new PhysicsEngine();
        setupInitialBodies();

        // >> SCENE & STAGE SETUP
        Scene scene = new Scene(root, 1024, 768);
        primaryStage.setTitle("N-Body Simulation 2D");
        primaryStage.setScene(scene);

        // >> RESIZE LISTENERS
        root.widthProperty().addListener((_, _, newValue) -> {
            if (currentView != null) {
                currentView.onResize(newValue.doubleValue(), root.getHeight());
            }
        });
        root.heightProperty().addListener((_, _, newValue) -> {
            if (currentView != null) {
                currentView.onResize(root.getWidth(), newValue.doubleValue());
            }
        });

        // >> UI LOGIC
        uiPanel.getPauseButton().setOnAction(_ -> togglePause());
        uiPanel.getResetButton().setOnAction(_ -> restartSimulation());
        uiPanel.getGravitySlider().valueProperty().addListener(
                (_, _, newVal) -> {
                    PhysicsEngine.G = PhysicsEngine.G_DEFAULT * newVal.doubleValue();
                }
        );

        // >> MAKE STAGE VISIBLE
        primaryStage.show();

        // FIXME: (HARD CODED AT THE MOMENT)
        switchView(new SimulationView2D(simulation, this));

        // >> CORE LOOP LOGIC
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!isPaused) {
                    simulation.update(TIME_STEP);
                }

                if (currentView != null) {
                    currentView.draw(simulation.getBodies());
                }
            }
        };

        timer.start();
    }

    // >> MODE SWITCHER
    private void switchView(ISimulationView newView) {
        this.currentView = newView;
        Node viewNode = newView.getViewNode();
        root.getChildren().addFirst(viewNode);
        currentView.onResize(root.getWidth(), root.getHeight());
    }

    // >> HELPER METHOD FOR INPUT HANDLER
    public void draw() {
        if (currentView != null) {
            currentView.draw(simulation.getBodies());
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

        // >> STOP TIMER AND RE-SETUP BODIES
        timer.stop();
        setupInitialBodies();

        // >> RESET STATE AND BUTTON
        isPaused = false;
        uiPanel.getPauseButton().setText("Pause");

        // >> REDEFINE 'G' AND RESET SLIDER
        PhysicsEngine.G = PhysicsEngine.G_DEFAULT;
        uiPanel.getGravitySlider().setValue(1.0);

        timer.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
