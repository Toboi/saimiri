package de.toboidev.saimiri.gfx.deferred;

import com.jme3.scene.Spatial;
import com.jme3.texture.Texture2D;

public interface Light {

    Spatial getSpatial();

    void setGBufferColor(Texture2D texture);

    void setGBufferNormal(Texture2D texture);

}
