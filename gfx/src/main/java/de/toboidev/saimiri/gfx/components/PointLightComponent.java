package de.toboidev.saimiri.gfx.components;

import com.jme3.math.ColorRGBA;
import com.simsilica.es.PersistentComponent;

/**
 * @author Eike Foede <toboi@toboidev.de>
 */
public class PointLightComponent implements PersistentComponent
{
    public final float radius;
    public final ColorRGBA color;
    public final float height;

    public PointLightComponent(float radius, ColorRGBA color, float height)
    {
        this.radius = radius;
        this.color = color;
        this.height = height;
    }

    public PointLightComponent(float radius, ColorRGBA color)
    {
        this(radius, color, 1.5f);
    }
}
