package de.toboidev.saimiri.game.components;

import com.simsilica.es.PersistentComponent;

public class TopDownCharacterComponent implements PersistentComponent {
    public final double speed;

    public TopDownCharacterComponent(double speed) {
        this.speed = speed;
    }
}
