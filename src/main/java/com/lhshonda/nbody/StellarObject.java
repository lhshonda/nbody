package com.lhshonda.nbody;

import java.util.LinkedList;
import java.util.Queue;
import javafx.geometry.Point2D;

public class StellarObject {

    // >> CLASS FIELDS
    private double x, y;
    private double vx, vy;
    private double mass;
    private double ax;
    private double ay;
    private double forceX, forceY;
    private double radius;

    // >> TRAIL FIELDS
    public static final int MAX_TRAIL_POINTS = 200;
    private final Queue<Point2D> positionHistory;

    public StellarObject(double x, double y, double vx, double vy, double mass, double radius) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.mass = mass;
        this.radius = radius;
        this.ax = 0.0;
        this.ay = 0.0;
        this.forceX = 0;
        this.forceY = 0;

        // >> IMPLEMENT QUEUE VIA LINKED LIST
        this.positionHistory = new LinkedList<>();

        // >> INITIAL QUEUE VALUE ON OBJECT CONSTRUCTION
        this.positionHistory.add(new Point2D(this.x, this.y));
    }

    // >> METHOD TO ADD ONTO QUEUE
    public void logPosition() {
        this.positionHistory.add(new Point2D(this.x, this.y));

        // >> REMOVE TRACER POINT THRESHOLD
        if (this.positionHistory.size() > MAX_TRAIL_POINTS) {
            this.positionHistory.poll();
        }
    }

    public Queue<Point2D> getPositionHistory() {
        return this.positionHistory;
    }

    // >> CLEAR FORCES METHOD
    public void resetForce() {
        this.forceX = 0;
        this.forceY = 0;
    }

    // >> ADD FORCE METHOD
    public void addForce(double fx, double fy) {
        this.forceX += fx;
        this.forceY += fy;
    }

    // >> GETTER METHODS
    public double getX() {return x;}
    public double getY() {return y;}
    public double getVx() {return vx;}
    public double getVy() {return vy;}
    public double getAx() {return ax;}
    public double getAy() {return ay;}
    public double getMass() {return mass;}
    public double getRadius() {return radius;}
    public double getForceX() {return forceX;}
    public double getForceY() {return forceY;}

    // >> SETTER METHODS
    public void setX(double x) {this.x = x;}
    public void setY(double y) {this.y = y;}
    public void setVx(double vx) {this.vx = vx;}
    public void setVy(double vy) {this.vy = vy;}
    public void setAx(double ax) {this.ax = ax;}
    public void setAy(double ay) {this.ay = ay;}
}
