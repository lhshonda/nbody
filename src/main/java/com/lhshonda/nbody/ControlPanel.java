package com.lhshonda.nbody;

import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;

public class ControlPanel {
    private final VBox rootPanel;
    private final Button pauseButton;
    private final Button resetButton;

    public ControlPanel() {
        // >> CREATING BUTTONS
        this.pauseButton = new Button("Pause");
        this.pauseButton.setPrefWidth(100);

        this.resetButton = new Button("Reset");
        this.resetButton.setPrefWidth(100);

        // >> DEFINING LAYOUT
        this.rootPanel = new VBox();
        this.rootPanel.setPadding(new Insets(10,10,10,10));
        this.rootPanel.getChildren().addAll(this.pauseButton, this.resetButton);
    }

    // >> GETTER METHODS
    public VBox getRootPanel() { return this.rootPanel; }
    public Button getPauseButton() { return this.pauseButton; }
    public Button getResetButton() { return this.resetButton; }
}
