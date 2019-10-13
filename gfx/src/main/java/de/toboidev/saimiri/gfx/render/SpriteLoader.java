package de.toboidev.saimiri.gfx.render;

import com.jme3.scene.Spatial;

/**
 * @author Eike Foede <toboi@toboidev.de>
 */
public interface SpriteLoader
{
    Spatial loadSprite(String name);
}
