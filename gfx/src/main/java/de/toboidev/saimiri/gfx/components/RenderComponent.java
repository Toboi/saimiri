package de.toboidev.saimiri.gfx.components;

import com.simsilica.es.PersistentComponent;

/**
 * This component marks an entity to be rendered
 * @author Eike Foede <toboi@toboidev.de>
 */
public class RenderComponent implements PersistentComponent
{
    /**
     * How far "up" the entity should be rendered. Only relevant for sorting of entities.
     * Valid Range is -1000 to 1000.
     * Higher values get rendered on top of lower values
     */
    public final float layer;

    public RenderComponent()
    {
        layer = 0;
    }

    public RenderComponent(float layer)
    {
        this.layer = layer;
    }
}
