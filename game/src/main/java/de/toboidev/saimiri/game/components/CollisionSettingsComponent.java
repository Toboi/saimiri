package de.toboidev.saimiri.game.components;

import com.simsilica.es.PersistentComponent;

public class CollisionSettingsComponent implements PersistentComponent {
    public final boolean collisionEnabled;
    public final boolean collisionEventsEnabled;

    public CollisionSettingsComponent(boolean collisionEnabled, boolean collisionEventsEnabled) {
        this.collisionEnabled = collisionEnabled;
        this.collisionEventsEnabled = collisionEventsEnabled;
    }
}
