package de.toboidev.saimiri.es.components;

import com.jme3.math.Vector3f;
import com.simsilica.es.PersistentComponent;

/**
 * @author Eike Foede <toboi@toboidev.de>
 */
public class Size implements PersistentComponent {
    public final float x;
    public final float y;

    public Size(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Size(float scale) {
        this.x = scale;
        this.y = scale;
    }

    public Size() {
        x = 1;
        y = 1;
    }

    public Vector3f asVector() {
        return new Vector3f(x, y, 1);
    }
}
