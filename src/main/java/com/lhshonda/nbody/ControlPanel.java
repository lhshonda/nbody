package com.lhshonda.nbody;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;

public class ControlPanel {
    private final VBox sliderContainer;
    private final HBox centeredContainer;
    private final Region spacerLeft;
    private final Region spacerRight;
    private final VBox rootPanel;
    private final HBox topControls;
    private final Button pauseButton;
    private final Button resetButton;
    private final Slider gravitySlider;

    public ControlPanel() {
        // >> PAUSE BUTTON
        this.pauseButton = new Button("Pause");
        this.pauseButton.setPrefWidth(100);
        this.pauseButton.getStyleClass().add("control-button");

        // >> RESET BUTTON
        this.resetButton = new Button("Reset");
        this.resetButton.setPrefWidth(100);
        this.resetButton.getStyleClass().add("control-button");

        // >> GRAVITY SLIDER
        this.gravitySlider = new Slider();
        this.gravitySlider.getStyleClass().add("control-slider");
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
        this.sliderContainer = new VBox(5);
        this.sliderContainer.setAlignment(Pos.TOP_CENTER);
        this.sliderContainer.getChildren().addAll(this.gravitySlider);

        this.topControls = new HBox(50);
        this.topControls.setAlignment(Pos.CENTER);
        this.topControls.setPadding(new Insets(10, 0, 10, 0));
        topControls.getChildren().addAll(this.resetButton, this.sliderContainer, this.pauseButton);

        this.rootPanel = new VBox(20);
        this.rootPanel.getStyleClass().add("control-panel");
        
        this.spacerLeft = new Region();
        HBox.setHgrow(spacerLeft, Priority.ALWAYS);
        this.spacerRight = new Region();
        HBox.setHgrow(spacerRight, Priority.ALWAYS);
        this.centeredContainer = new HBox(spacerLeft, topControls, spacerRight);
        this.rootPanel.getChildren().add(centeredContainer);
        this.rootPanel.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
    }

    // >> GETTER METHODS
    public VBox getRootPanel() { return this.rootPanel; }
    public Button getPauseButton() { return this.pauseButton; }
    public Button getResetButton() { return this.resetButton; }
    public Slider getGravitySlider() { return this.gravitySlider; }
}
