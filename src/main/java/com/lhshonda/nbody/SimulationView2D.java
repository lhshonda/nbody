package com.lhshonda.nbody;

import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.List;
import java.util.Queue;
import javafx.geometry.Point2D;

public class SimulationView2D implements ISimulationView {

    // >> 2D COMPONENTS
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final Camera camera;
    private final InputHandler inputHandler;

    // >> NEW FIELDS FOR HS
    private final PhysicsEngine physicsEngine;
    final double VISUAL_RADIUS_FACTOR = 20.0;

    // >> WORLD CONSTANTS
    private static final double AU = 1.49e11;
    private static final double SUN_MASS = 1.989e30;

    public SimulationView2D(PhysicsEngine physicsEngine, NBodySimulator simulation) {
        this.physicsEngine = physicsEngine;
        this.canvas = new Canvas();
        this.gc = canvas.getGraphicsContext2D();
        this.camera = new Camera(1024, 768, AU);

        this.inputHandler = new InputHandler(this.camera, physicsEngine, simulation);
        this.inputHandler.attach(this.canvas);
    }

    // >> POLYMORPHIC CONTRACT
    @Override
    public Node getViewNode() {
        return this.canvas;
    }

    @Override
    public void onResize(double width, double height) {
        this.canvas.setWidth(width);
        this.canvas.setHeight(height);
        camera.onResize(width, height, AU);
    }

    @Override
    public void draw (List<StellarObject> bodies) {

        // >> CLEAR CANVAS WITH BLACK BG
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        // >> HS APPEARANCE
        gc.setLineWidth(1.0);
        gc.setLineDashes(5, 5);
        gc.setStroke(Color.web("#3498db", 0.45));

        for (StellarObject body : bodies) {
            
            // >> CALC HILL RADIUS
            double hillRadiusWorld = physicsEngine.calculateHillRadius(body);
            
            if (hillRadiusWorld > 0.0) {
                
                // >> WLRD RADIUS TO PXLS
                double hillRadiusScreen = hillRadiusWorld * camera.getScale() * VISUAL_RADIUS_FACTOR;
                
                // >> WLRD POS TO SCREEN
                double screenX = (body.getX() * camera.getScale()) + camera.getOffsetX();
                double screenY = (-body.getY() * camera.getScale()) + camera.getOffsetY();

                // >> DRAW HS
                gc.strokeOval(
                    screenX - hillRadiusScreen, 
                    screenY - hillRadiusScreen, 
                    2 * hillRadiusScreen, 
                    2 * hillRadiusScreen
                );
            }
        }
        
        gc.setLineDashes(0); 
        
        // >> TRAIL APPEARANCE
        gc.setStroke(Color.RED.deriveColor(0,1,1,0.2));
        gc.setLineWidth(2.0);

        for (StellarObject body : bodies) {

            Queue<Point2D> history = body.getPositionHistory();
            if (history.size() < 2) { continue; }

            gc.beginPath();
            boolean firstPoint = true;

            for (Point2D worldPos : history) {
                double screenX = worldPos.getX() * camera.getScale() + camera.getOffsetX();
                double screenY = -worldPos.getY() * camera.getScale() + camera.getOffsetY();

                if (firstPoint) {
                    gc.moveTo(screenX, screenY);
                    firstPoint = false;
                } else {
                    gc.lineTo(screenX, screenY);
                }
            }
            gc.stroke();
        }

        for (StellarObject body : bodies) {
            
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
                gc.setFill(Color.RED);
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
}
