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
        // >> PAUSE BUTTON
        this.pauseButton = new Button("Pause");
        this.pauseButton.setPrefWidth(100);
        this.pauseButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");

        // >> RESET BUTTON
        this.resetButton = new Button("Reset");
        this.resetButton.setPrefWidth(100);
        this.resetButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");

        // >> GRAVITY SLIDER
        Label gravityLabel = new Label("Gravity:");
        gravityLabel.setStyle("-fx-text-fill: white");
        this.gravitySlider = new Slider();
        this.gravitySlider.setMaxWidth(200.0);
        this.gravitySlider.setMin(0.0);
        this.gravitySlider.setMax(10.0);
        this.gravitySlider.setValue(1.0);
        this.gravitySlider.setShowTickLabels(true);
        this.gravitySlider.setShowTickMarks(true);
        this.gravitySlider.setMajorTickUnit(1.0);
        this.gravitySlider.setMinorTickCount(1);
        this.gravitySlider.setSnapToTicks(true);


        // >> DEFINING LAYOUT
        this.rootPanel = new VBox(20);
        this.rootPanel.setPadding(new Insets(50));
        this.rootPanel.getChildren().addAll(gravityLabel, this.gravitySlider, this.resetButton, this.pauseButton);
    }

    // >> GETTER METHODS
    public VBox getRootPanel() { return this.rootPanel; }
    public Button getPauseButton() { return this.pauseButton; }
    public Button getResetButton() { return this.resetButton; }
    public Slider getGravitySlider() { return this.gravitySlider; }
}
