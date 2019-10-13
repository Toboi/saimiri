package de.toboidev.saimiri.es.components;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.simsilica.es.PersistentComponent;

/**
 * Clockwise rotation in radians
 *
 * @author Eike Foede <toboi@toboidev.de>
 */
public class Rotation implements PersistentComponent {
    public final float radians;

    public Rotation(float radians) {
        this.radians = radians;
    }

    public Rotation() {
        radians = 0;
    }

    public Quaternion asQuaternion() {
        Quaternion q = new Quaternion();
        q.fromAngleNormalAxis(-radians, Vector3f.UNIT_Z);
        return q;
    }
}
