package com.lhshonda.nbody;

public class Camera {

    // >> CONSTANTS
    private static final double INITIAL_VIEW_WIDTH_AU = 8.5;

    // >> FIELDS
    private double scale;
    private double offsetX;
    private double offsetY;
    private double lastMouseX;
    private double lastMouseY;

    // >> CONSTRUCTOR
    public Camera(double canvasWidth, double canvasHeight, double au) {
        this.lastMouseX = 0;
        this.lastMouseY = 0;

        onResize(canvasWidth, canvasHeight, au);
    }

    // >> GETTERS
    public double getScale() { return this.scale; }
    public double getOffsetX() { return this.offsetX; }
    public double getOffsetY() { return this.offsetY; }

    // >> EVENT HANDLERS
    public void onResize(double width, double height, double au) {
        this.scale = width / (au * INITIAL_VIEW_WIDTH_AU);
        this.offsetX = width / 2;
        this.offsetY = height / 2;
    }

    // >> PAN/DRAG ANCHOR
    public void onMousePressed(double x, double y) {
        this.lastMouseX = x;
        this.lastMouseY = y;
    }

    // >> DELTA CALC / CAMERA MOVEMENT
    public void onMouseDragged(double x, double y) {
        double dx = x - lastMouseX;
        double dy = y - lastMouseY;

        this.offsetX += dx;
        this.offsetY += dy;

        // >> UPDATE POSITION
        this.lastMouseX = x;
        this.lastMouseY = y;
    }

    // >> CAMERA ZOOM SCROLL (Y FLIPPED)
    public void onScroll(double mouseX, double mouseY, double deltaY) {

        // >> ZOOM TUNING
        final double ZOOM_SPEED = 0.001;

        // >> WORLD COORDS BEFORE ZOOM
        double worldX_before = (mouseX - this.offsetX) / this.scale;
        double worldY_before = (mouseY - this.offsetY) / (-this.scale);

        // >> APPLY ZOOM
        double zoomFactor = 1.0 + (deltaY * ZOOM_SPEED);

        // >> CLAMP ZOOM
        if (zoomFactor < 0.5) zoomFactor = 0.5;
        if (zoomFactor > 2.0) zoomFactor = 2.0;

        double newScale = scale * zoomFactor;

        // >> SCREEN COORDS AFTER ZOOM
        double screenX_after = (worldX_before * newScale) + offsetX;
        double screenY_after = (-worldY_before * newScale) + offsetY;

        // >> OFFSET ADJUSTMENT
        this.offsetX += (mouseX - screenX_after);
        this.offsetY += (mouseY - screenY_after);

        // >> SET NEW SCALE
        this.scale = newScale;
    }
}
