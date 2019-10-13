package de.toboidev.saimiri.gfx.render;

import com.simsilica.es.PersistentComponent;

/**
 * @author Eike Foede <toboi@toboidev.de>
 */
public class StaticSprite implements PersistentComponent
{
    public final String sprite;

    public StaticSprite(String sprite)
    {
        this.sprite = sprite;
    }
}
