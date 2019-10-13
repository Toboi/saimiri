package de.toboidev.saimiri.gfx.deferred.lights;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.texture.Texture2D;
import de.toboidev.saimiri.gfx.deferred.Light;

public class PointLight implements Light {
    private ColorRGBA lightColor = ColorRGBA.White;
    private Spatial spatial;
    private float radius = 1;
    private float height = 1.5f;
    private Material material;
    private Vector3f position = Vector3f.ZERO;

    public PointLight(AssetManager assetManager) {
        Mesh q = createQuad();
        spatial = new Geometry("PointLight", q);
        material = new Material(assetManager, "de/toboidev/saimiri/gfx/deferred/lights/pointlight.j3md");
        material.setFloat("Height", height);
        material.setColor("Color", lightColor);
        material.getAdditionalRenderState().setBlendEquation(RenderState.BlendEquation.Add);
        material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Additive);
        material.getAdditionalRenderState().setDepthTest(false);
        material.getAdditionalRenderState().setDepthWrite(false);
        spatial.setQueueBucket(RenderQueue.Bucket.Transparent);
        spatial.setMaterial(material);
        spatial.setLocalScale(radius);
        spatial.setLocalTranslation(position);
    }

    @Override public Spatial getSpatial() {
        return spatial;
    }

    @Override
    public void setGBufferColor(Texture2D texture) {
        material.setTexture("GDiffuse", texture);
    }

    public void setLightColor(ColorRGBA lightColor) {
        this.lightColor = lightColor;
        material.setColor("Color", lightColor);
    }

    public void setRadius(float radius) {
        this.radius = radius;
        spatial.setLocalScale(radius);
    }

    public void setHeight(float height) {
        this.height = height;
        material.setFloat("Height", height);
    }

    public void setPosition(float x, float y) {
        this.position = new Vector3f(x, y, 0);
        spatial.setLocalTranslation(position);
    }

    @Override public void setGBufferNormal(Texture2D texture) {
        material.setTexture("GNormal", texture);
    }

    private Mesh createQuad() {
        Mesh m = new Mesh();
        float extent = 1;
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
