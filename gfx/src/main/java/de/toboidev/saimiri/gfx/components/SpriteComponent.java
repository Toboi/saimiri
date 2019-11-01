package de.toboidev.saimiri.gfx.components;

import com.simsilica.es.PersistentComponent;

/**
 * @author Eike Foede <toboi@toboidev.de>
 */
public class SpriteComponent implements PersistentComponent
{
    public final String sprite;

    public SpriteComponent(String sprite)
    {
        this.sprite = sprite;
    }
}
