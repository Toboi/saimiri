package de.toboidev.saimiri.es.components;

import com.jme3.math.Vector2f;
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

    public Position(Vector2f vec) {
        this.x = vec.x;
        this.y = vec.y;
    }

    public Position() {
        x = 0;
        y = 0;
    }

    public Vector2f asVector2f() {
        return new Vector2f(x, y);
    }

    public Vector3f asVector3f() {
        return asVector3f(0);
    }

    public Vector3f asVector3f(float z) {
        return new Vector3f(x, y, z);
    }
}
