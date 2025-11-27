package com.lhshonda.nbody;

import javafx.scene.Node;
import java.util.List;

public interface ISimulationView {

    Node getViewNode();

    // >> RENDERING BODIES
    void draw(List<StellarObject> bodies);

    // >> RESIZE NOTIFIER
    void onResize(double width, double height);

}
