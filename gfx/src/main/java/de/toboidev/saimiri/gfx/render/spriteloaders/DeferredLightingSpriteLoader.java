package de.toboidev.saimiri.gfx.render.spriteloaders;

import com.jme3.asset.AssetManager;
import com.jme3.asset.AssetNotFoundException;
import com.jme3.material.Material;

/**
 * @author Eike Foede <toboi@toboidev.de>
 */
public class DeferredLightingSpriteLoader extends DefaultSpriteLoader {

    public DeferredLightingSpriteLoader(AssetManager assetManager) {
        super(assetManager);
    }

    @Override public void setTexture(String baseTexture, AssetManager assetManager, Material material) {
        super.setTexture(baseTexture, assetManager, material);

        try {
            String normalSearchPath = baseTexture.replaceAll("\\.(\\w+)$", "-normal.$1");
            material.setTexture("NormalMap", assetManager.loadTexture(normalSearchPath));
        } catch (AssetNotFoundException ex) {
        }
        try {
            String glowSearchPath = baseTexture.replaceAll("\\.(\\w+)$", "-glow.$1");
            material.setTexture("GlowMap", assetManager.loadTexture(glowSearchPath));
        } catch (AssetNotFoundException ex) {
        }
    }
}
