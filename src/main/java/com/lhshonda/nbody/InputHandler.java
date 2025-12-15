package com.lhshonda.nbody;

import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class InputHandler {

    // >> FIELDS
    private final Camera camera;
    private final PhysicsEngine physicsEngine;
    private final NBodySimulator simulator;

    // >> NEW FIELDS
    private double startX;
    private double startY;
    private StellarObject bodyToFling;

    // >> CONSTANTS
    private static final double NEW_BODY_MASS = 5.972e24;
    private static final double NEW_BODY_RADIUS = 6.371e6;

    // >> CONSTRUCTOR
    public InputHandler(Camera camera, PhysicsEngine physicsEngine, NBodySimulator simulator) {
        this.camera = camera;
        this.physicsEngine = physicsEngine;
        this.simulator = simulator;
    }

    // >> MOUSE EVENT LISTENERS
    public void attach(Canvas canvas) {
        canvas.setOnMousePressed(this::handleMousePressed);
        canvas.setOnMouseDragged(this::handleMouseDragged);
        canvas.setOnMouseReleased(this::handleMouseReleased);
        canvas.setOnScroll(this::handleScroll);
    }

    // >> PRIVATE EVENT HANDLERS
    private void handleMousePressed(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY) {
            camera.onMousePressed(e.getX(), e.getY());
        } else if (e.getButton() == MouseButton.SECONDARY) {

            // SAVE BODY COORDS
            this.startX = e.getX();
            this.startY = e.getY();
            this.bodyToFling = createBodyAtScreenPosition(e.getX(), e.getY());
        }
    }

    private void handleMouseDragged(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY) {
            camera.onMouseDragged(e.getX(), e.getY());
            simulator.draw();
        } else if (e.getButton() == MouseButton.SECONDARY) {
            simulator.draw();
        }
    }

    private void handleMouseReleased(MouseEvent e) {
        if (e.getButton() == MouseButton.SECONDARY && this.bodyToFling != null) {
            
            double endX = e.getX();
            double endY = e.getY();
            
            double deltaScreenX = endX - this.startX;
            double deltaScreenY = endY - this.startY;
            
            final double FLING_FACTOR = 2e2; 
            
            double velocityX = deltaScreenX * FLING_FACTOR;
            double velocityY = deltaScreenY * -FLING_FACTOR; 
            
            this.bodyToFling.setVx(velocityX);
            this.bodyToFling.setVy(velocityY);
            
            physicsEngine.addBody(this.bodyToFling);
            
            this.bodyToFling = null;
            simulator.draw();
            
        }
    }

    private void handleScroll(ScrollEvent e) {
        camera.onScroll(e.getX(), e.getY(), e.getDeltaY());
        simulator.draw();
    }

    private StellarObject createBodyAtScreenPosition(double screenX, double screenY) {

    // >> CURRENT STATE
    double scale = camera.getScale();
    double offsetX = camera.getOffsetX();
    double offsetY = camera.getOffsetY();

    // >> CONVERT PIXELS TO SI
    double worldX = (screenX - offsetX) / scale;
    double worldY = (screenY - offsetY) / -scale;

    // >> CREATING NEW BODY
    StellarObject newBody = new StellarObject(
            worldX,
            worldY,
            0,
            0,
            NEW_BODY_MASS,
            NEW_BODY_RADIUS
    );

    return newBody;
}

}
