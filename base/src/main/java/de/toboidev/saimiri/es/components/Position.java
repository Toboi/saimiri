package de.toboidev.saimiri.es.components;

import com.jme3.math.Vector3f;
import com.simsilica.es.PersistentComponent;

/**
 * @author Eike Foede <toboi@toboidev.de>
 */
public class Position implements PersistentComponent {
    public final float x;
    public final float y;

    public Position(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Position() {
        x = 0;
        y = 0;
    }

    public Vector3f asVector() {
        return asVector(0);
    }

    public Vector3f asVector(float z) {
        return new Vector3f(x, y, z);
    }
}
