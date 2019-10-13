package de.toboidev.saimiri.es.components;

import com.jme3.math.Vector3f;
import com.simsilica.es.PersistentComponent;

/**
 * @author Eike Foede <toboi@toboidev.de>
 */
public class Scale implements PersistentComponent {
    public final float x;
    public final float y;

    public Scale(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Scale(float scale) {
        this.x = scale;
        this.y = scale;
    }

    public Scale() {
        x = 1;
        y = 1;
    }

    public Vector3f asVector() {
        return new Vector3f(x, y, 1);
    }
}
