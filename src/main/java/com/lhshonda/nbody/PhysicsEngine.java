package com.lhshonda.nbody;

import java.util.ArrayList;
import java.util.List;

public class PhysicsEngine {

    private final List<StellarObject> bodies;

    // >> GRAVITATIONAL CONSTANT
    public static final double G_DEFAULT = 6.67430e-11;
    public static double G = G_DEFAULT;

    // >> CONSTRUCTOR
    public PhysicsEngine() {
        this.bodies = new ArrayList<>();
    }

    // >> ADD BODY METHOD
    public void addBody(StellarObject body) { this.bodies.add(body); }

    // >> ARRAY LIST GETTER
    public List<StellarObject> getBodies() { return this.bodies; }

    // >> PHYSICS ENGINE
    public void update(double dt) {

        // :: Updating body positions based on the current velocity & previous frame's acceleration.
        for (StellarObject body : this.bodies) {
            body.setX(body.getX() + body.getVx() * dt + (0.5 * body.getAx() * dt * dt));
            body.setY(body.getY() + body.getVy() * dt + (0.5 * body.getAy() * dt * dt));

            // >> ADD WORLD POSITION INTO QUEUE
            body.logPosition();
        }

        // >> BODY FORCE RESET
        for (StellarObject body : this.bodies) {
            body.resetForce();
        }

        for (int i = 0; i < this.bodies.size(); i++) {
            StellarObject bodyA = this.bodies.get(i);

            for (int j = i + 1; j < this.bodies.size(); j++) {
                StellarObject bodyB = this.bodies.get(j);

                // >> DISTANCE CALCULATION
                double dx = bodyB.getX() - bodyA.getX();
                double dy = bodyB.getY() - bodyA.getY();
                double rSquared  = (dx * dx) + (dy * dy);
                double r = Math.sqrt(rSquared);

                // >> DISTANCE CLAMP
                double minDistance = bodyA.getRadius() - bodyB.getRadius();
                if (r < minDistance) {
                    r = minDistance;
                    rSquared = r * r;
                }

                // >> FORCE MAGNITUDE
                // :: F = (G * (Mass1 * Mass2) / r^2)
                double forceMag = (G * bodyA.getMass() * bodyB.getMass()) / rSquared;

                // >> FORCE COMPONENTS
                /* By dividing the vector component by the total distance we attain a
                vector of length one. This multiplied by the force magnitude allows us to
                get respective force components in order to update objects. */
                double fx = forceMag * (dx / r);
                double fy = forceMag * (dy / r);

                // >> ADDING FORCES
                bodyA.addForce(fx, fy);
                bodyB.addForce(-fx, -fy);
            }
        }

         for (StellarObject body : this.bodies) {
             // >> NEW ACCELERATION
             double ax_new = body.getForceX() / body.getMass();
             double ay_new = body.getForceY() / body.getMass();

             // PREVIOUS ACCELERATION
             double ax_old = body.getAx();
             double ay_old = body.getAy();

             // >> VELOCITY VERLET
             body.setVx(body.getVx() + 0.5 * (ax_old + ax_new) * dt);
             body.setVy(body.getVy() + 0.5 * (ay_old + ay_new) * dt);

             // >> STORING NEW ACCELERATION
             body.setAx(ax_new);
             body.setAy(ay_new);
         }
    }
}
