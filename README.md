## N-Body Simulation

#### This is a gravitational simulation built with Java and JavaFX.
Planetary motion is modelled with a Velocity Verlet integration for stable orbits. \
The application is rendered with JavaFX and support dynamic interaction. \
Roles are decoupled with the MVC pattern using a "pluggable" view system via the `ISimulationView` interface.

### Interactivity

**Pan:** Click & Drag to move.

**Zoom:** Zoom in and out with the scrollwheel.

**Add Body:** Right click to add a celestial body with no initial velocity vectors.

**Pause/Play:** Stop and play the simulation with the UI.

**Reset:** Bring everything back to the beggining with the reset button.

**Gravity Multiplier:** Tune gravity to your liking with the included slider.
