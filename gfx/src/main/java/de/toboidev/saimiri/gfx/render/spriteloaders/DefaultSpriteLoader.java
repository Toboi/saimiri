package de.toboidev.saimiri.gfx.render.spriteloaders;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import de.toboidev.saimiri.gfx.render.SpriteLoader;

/**
 * @author Eike Foede <toboi@toboidev.de>
 */
public class DefaultSpriteLoader implements SpriteLoader {
    private final AssetManager assetManager;

    public DefaultSpriteLoader(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public void setTexture(String baseTexture, AssetManager assetManager, Material material) {
        material.setTexture("Color", assetManager.loadTexture(baseTexture));
    }

    @Override public Spatial loadSprite(String name) {
        Geometry geom = new Geometry("Sprite", createQuad());

        Material mat = new Material(assetManager, "de/toboidev/saimiri/gfx/geometry.j3md");
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        setTexture(name, assetManager, mat);
        geom.setMaterial(mat);
        return geom;
    }

    private Mesh createQuad() {
        Mesh m = new Mesh();
        float extent = 0.5f;
        m.setBuffer(VertexBuffer.Type.Position, 3, new float[]{
                -extent, -extent, 0,
                -extent, extent, 0,
                extent, extent, 0,
                extent, -extent, 0
        });

        m.setBuffer(VertexBuffer.Type.TexCoord, 2, new float[]{
                0, 0,
                0, 1,
                1, 1,
                1, 0
        });
        m.setBuffer(VertexBuffer.Type.Index, 3, new short[]{
                0, 2, 1,
                0, 3, 2
        });
        m.updateBound();
        m.setStatic();
        return m;
    }
}
