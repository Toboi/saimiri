package de.toboidev.saimiri.gfx.deferred.lights;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
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
    private final Spatial spatial;
    private float radius = 1;
    private float height = 1.5f;
    private final Material material;
    private Vector3f position = Vector3f.ZERO;

    public PointLight(AssetManager assetManager) {
        Mesh q = createCircleApproximation(8);
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

    private Mesh createCircleApproximation(int numSlices) {
        if (numSlices < 3) {
            throw new IllegalArgumentException("Can only generate circle approximations with at least 3 slices");
        }

        float anglePerSlice = FastMath.TWO_PI / numSlices;
        float hypothenuse = 1 / FastMath.cos(anglePerSlice / 2.0f);

        float[] positionBuffer = new float[(numSlices + 1) * 3];
        float[] texCoordBuffer = new float[(numSlices + 1) * 2];

        //Center vertex
        positionBuffer[0] = 0;
        positionBuffer[1] = 0;
        positionBuffer[2] = 0;
        texCoordBuffer[0] = 0;
        texCoordBuffer[1] = 0;

        for (int i = 0; i < numSlices; i++) {
            float angle = anglePerSlice * (i + 0.5f);
            float x = FastMath.cos(angle) * hypothenuse;
            float y = FastMath.sin(angle) * hypothenuse;
            positionBuffer[(i + 1) * 3] = x;
            positionBuffer[(i + 1) * 3 + 1] = y;
            positionBuffer[(i + 1) * 3 + 2] = 0;
            texCoordBuffer[(i + 1) * 2] = x;
            texCoordBuffer[(i + 1) * 2 + 1] = y;
        }


        int[] indexBuffer = new int[(numSlices) * 3];

        for (int i = 0; i < numSlices; i++) {
            indexBuffer[i * 3] = 0;
            indexBuffer[i * 3 + 1] = i + 1;
            indexBuffer[i * 3 + 2] = (i + 1) % numSlices + 1;
        }

        Mesh m = new Mesh();
        float extent = 1;
        m.setBuffer(VertexBuffer.Type.Position, 3, positionBuffer);

        m.setBuffer(VertexBuffer.Type.TexCoord, 2, texCoordBuffer);
        m.setBuffer(VertexBuffer.Type.Index, 3, indexBuffer);
        m.updateBound();
        m.setStatic();
        return m;
    }
}
