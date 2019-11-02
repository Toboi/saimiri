package de.toboidev.saimiri.game.components;

import com.simsilica.es.PersistentComponent;

public class PlatformerCharacterComponent implements PersistentComponent {
    public final double jumpSpeed;
    public final double horizontalSpeed;
    public final double gravity;

    public PlatformerCharacterComponent(double jumpSpeed, double horizontalSpeed, double gravity) {
        this.jumpSpeed = jumpSpeed;
        this.horizontalSpeed = horizontalSpeed;
        this.gravity = gravity;
    }

    public PlatformerCharacterComponent() {
        this(450, 250, 1500);
    }
}
