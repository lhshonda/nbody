package com.lhshonda.nbody;

// POJO
public class StellarObject {

    // physical properties
    private double x, y;
    private double vx, vy;
    private double mass;

    // current forces acting on the body (tb calculated every frame)
    private double forceX, forceY;

    // drawing properties
    private double radius;

    public StellarObject(double x, double y, double mass, double radius) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.mass = mass;
        this.radius = radius;

        this.forceX = 0;
        this.forceY = 0;
    }

    // clearing the cumulative forces
    public void resetForce() {
        this.forceX = 0;
        this.forceY = 0;
    }

    // adding force vectors
    public void addForce(double fx, double fy) {
        this.forceX += fx;
        this.forceY += fy;
    }

    // defining getters to read data from stellar objects
    public double getX() {return x;}
    public double getY() {return y;}
    public double getVx() {return vx;}
    public double getVy() {return vy;}
    public double getMass() {return mass;}
    public double getRadius() {return radius;}
    public double getForceX() {return forceX;}
    public double getForceY() {return forceY;}

    // defining setters to update data
    public void setX(double x) {this.x = x;}
    public void setY(double y) {this.y = y;}
    public void setVx(double vx) {this.vx = vx;}
    public void setVy(double vy) {this.vy = vy;}
}
