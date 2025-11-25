package com.lhshonda.nbody;

import java.util.ArrayList; // a type of 'List' that will allow us to easily add and loop over items
import java.util.List; // the 'interface';

public class PhysicsEngine {

    /* The '<>' syntax is called generics. They're used to tell a class (in this case ArrayList)
    what they're allowed to hold. List<String> would be a list that only holds strings. */
    private final List<StellarObject> bodies;
    /* A list is a data structure that holds an ordered collection of elements. Unlike a basic array
    it can grow and shrink as you add/remove things. */

    private final double G = 5; // gravitational constant
    /* In the real world 'G' is very small (6.674e-11).
    In order to make it noticeable it's scaled by a lot. */


    // constructor used to pass a newly created list into bodies
    public PhysicsEngine() {
        this.bodies = new ArrayList<>();
    }

    // public method used to add bodies within the newly created list
    public void addBody(StellarObject body) {
        this.bodies.add(body);
    }

    // public method used to return the list of bodies
    public List<StellarObject> getBodies() {
        return this.bodies;
    }

    // the main part taking care of the interactions between bodies
    public void update(double deltaTime) {
         for (StellarObject body : this.bodies) {
             body.resetForce();
         }

         for (int i = 0; i < this.bodies.size(); i++) {
             StellarObject bodyA = this.bodies.get(i);

             for (int j = i + 1; j < this.bodies.size(); j++) {
                 StellarObject bodyB = this.bodies.get(j);

                 /* Finding the distance between bodies with the distance formula.
                 Note: dx and dy are not just distances, they are vector components. */
                 double dx = bodyB.getX() - bodyA.getX();
                 double dy = bodyB.getY() - bodyA.getY();
                 double rSquared  = (dx * dx) + (dy * dy);
                 double r = Math.sqrt(rSquared);

                 // clamping the radius from bodies to prevent division by zero
                 // F = (G*(m_1 * m_2))/r^2

                 if (r < 5.0) {
                     r = 5.0;
                     rSquared = r * r;
                 }

                 // calculating the force magnitude between two bodies
                 double forceMag = (G * bodyA.getMass() * bodyB.getMass()) / rSquared;

                 // calculating the force components
                 double fx = forceMag * (dx / r);
                 double fy = forceMag * (dy / r);
                 /* By dividing the vector component by the total distance we attain a
                 vector of length one. This multiplied by the force magnitude allows us to
                 get respective force components in order to update objects. */

                 // adding forces to the different bodies
                 bodyA.addForce(fx, fy);
                 bodyB.addForce(-fx, -fy);
             }
         }

         for (StellarObject body : this.bodies) {

             // getting acceleration from Newton's 2nd Law (a = F / m)
             double ax = body.getForceX() / body.getMass();
             double ay = body.getForceY() / body.getMass();

             // updating velocity (kinematic eq v_f = v_i + (a * dt)
             body.setVx(body.getVx() + (ax * deltaTime));
             body.setVy(body.getVy() + (ay * deltaTime));

             // updating position based on the new velocity
             body.setX(body.getX() + (body.getVx() * deltaTime));
             body.setY(body.getY() + (body.getVy() * deltaTime));
         }
    }
}
