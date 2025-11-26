package com.lhshonda.nbody;

import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

public class ControlPanel {
    private final VBox rootPanel;
    private final Button pauseButton;
    private final Button resetButton;
    private final Slider gravitySlider;

    public ControlPanel() {
        // >> CREATING BUTTONS
        this.pauseButton = new Button("Pause");
        this.pauseButton.setPrefWidth(100);

        this.resetButton = new Button("Reset");
        this.resetButton.setPrefWidth(100);

        Label gravityLabel = new Label("Gravity:");
        this.gravitySlider = new Slider();
        this.gravitySlider.setMin(0.1);
        this.gravitySlider.setMax(10.0);
        this.gravitySlider.setValue(1.0);

        // >> DEFINING LAYOUT
        this.rootPanel = new VBox(10);
        this.rootPanel.setPadding(new Insets(10,10,10,10));
        this.rootPanel.getChildren().addAll(this.pauseButton, this.resetButton, gravityLabel, this.gravitySlider);
    }

    // >> GETTER METHODS
    public VBox getRootPanel() { return this.rootPanel; }
    public Button getPauseButton() { return this.pauseButton; }
    public Button getResetButton() { return this.resetButton; }
    public Slider getGravitySlider() { return this.gravitySlider; }
}
