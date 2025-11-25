package com.lhshonda.nbody;

// POJO
public class StellarObject {

    /* The physics properties are private in order to prevent other parts of the code from
    accidentally interacting with it. This is an OOP concept named encapsulation. */

    // physical properties
    private double x, y;
    private double vx, vy;
    private double mass;
    private double ax;
    private double ay;

    // current forces acting on the body (tb calculated every frame)
    private double forceX, forceY;

    // drawing properties
    private double radius;

    /*
    This constructor is called when the 'new' keyword is used. It initializes all
    the fields, getting them into a ready-to-use state. It 'constructs' the object.

    It must have no return type, and it must be the same name as the class.
    The keyword 'this' will ensure a reference to the specific object instance being created when the
    constructor is called.
     */

    public StellarObject(double x, double y, double vx, double vy, double mass, double radius) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.ax = ax;
        this.ay = ay;
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
    public double getAx() {return ax;}
    public double getAy() {return ay;}
    public double getMass() {return mass;}
    public double getRadius() {return radius;}
    public double getForceX() {return forceX;}
    public double getForceY() {return forceY;}

    // defining setters to update data
    public void setX(double x) {this.x = x;}
    public void setY(double y) {this.y = y;}
    public void setVx(double vx) {this.vx = vx;}
    public void setVy(double vy) {this.vy = vy;}
    public void setAx(double ax) {this.ax = ax;}
    public void setAy(double ay) {this.ay = ay;}
}
