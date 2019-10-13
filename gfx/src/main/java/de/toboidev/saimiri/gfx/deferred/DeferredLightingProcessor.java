package de.toboidev.saimiri.gfx.deferred;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.post.SceneProcessor;
import com.jme3.profile.AppProfiler;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.Image;
import com.jme3.texture.Texture2D;

import java.util.ArrayList;
import java.util.List;

public class DeferredLightingProcessor implements SceneProcessor {

    private static int renderBufferSamples = 1;
    private static Node lightScene;
    private FrameBuffer gBuffer;
    private Texture2D color;
    private Texture2D normal;
    private Texture2D glow;
    private List<Light> lights;
    private ViewPort lightViewPort;
    private ColorRGBA ambientLight = ColorRGBA.White.mult(0.1f);
    private Geometry ambientAndGlowRenderTarget;
    private AssetManager assetManager;
    private RenderManager renderManager;

    private FrameBuffer tempBuffer;
    private ViewPort viewPort;
    private boolean inited = false;

    public DeferredLightingProcessor(AssetManager assetManager) {
        this.assetManager = assetManager;
        lightScene = new Node("lights");
        lights = new ArrayList<>();
    }

    public boolean addLight(Light light) {
        light.setGBufferColor(color);
        light.setGBufferNormal(normal);
        lightScene.attachChild(light.getSpatial());
        return lights.add(light);
    }

    public boolean removeLight(Light l) {
        if (l != null) {
            lightScene.detachChild((l).getSpatial());
        }
        return lights.remove(l);
    }

    @Override public void initialize(RenderManager rm, ViewPort vp) {
        this.renderManager = rm;
        this.viewPort = vp;
        vp.getQueue().setGeometryComparator(RenderQueue.Bucket.Opaque, new com.jme3.renderer.queue.GuiComparator());
        vp.setClearColor(true);
        vp.setClearFlags(true, true, true);
        vp.setBackgroundColor(ColorRGBA.Black);
        reshape(vp, vp.getCamera().getWidth(), vp.getCamera().getHeight());
        inited = true;
    }

    @Override public void reshape(ViewPort vp, int w, int h) {
        color = new Texture2D(w, h, Image.Format.RGB32F);
        normal = new Texture2D(w, h, Image.Format.RGB8);
        glow = new Texture2D(w, h, Image.Format.RGB8);
        gBuffer = new FrameBuffer(w, h, renderBufferSamples);
        gBuffer.addColorTexture(color);
        gBuffer.addColorTexture(normal);
        gBuffer.addColorTexture(glow);
        gBuffer.setMultiTarget(true);

        lightViewPort = new ViewPort("lights", vp.getCamera());
        lightViewPort.attachScene(lightScene);
        lightViewPort.setClearFlags(true, true, true);

        for (Light l : lights) {
            l.setGBufferNormal(normal);
            l.setGBufferColor(color);
        }


        Quad q = new Quad(1, 1);
        ambientAndGlowRenderTarget = new Geometry("ambientAndGlowRenderTarget", q);
        Material mat = new Material(assetManager, "de/toboidev/saimiri/gfx/deferred/ambientAndGlowRenderTarget.j3md");
        mat.setTexture("GDiffuse", color);
        mat.setTexture("GGlow", glow);
        mat.setColor("Ambient", ambientLight);
        ambientAndGlowRenderTarget.setMaterial(mat);
        ambientAndGlowRenderTarget.setIgnoreTransform(true);
        ambientAndGlowRenderTarget.setCullHint(Spatial.CullHint.Never);
        mat.getAdditionalRenderState().setDepthTest(false);
        mat.getAdditionalRenderState().setDepthWrite(false);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Additive);
        ambientAndGlowRenderTarget.setQueueBucket(RenderQueue.Bucket.Opaque);
    }

    public void setAmbientLight(ColorRGBA ambientLight) {
        this.ambientLight = ambientLight;
        if (ambientAndGlowRenderTarget != null) {
            ambientAndGlowRenderTarget.getMaterial().setColor("Ambient", ambientLight);
        }
    }

    @Override public boolean isInitialized() {
        return inited;
    }

    @Override public void preFrame(float tpf) {
        tempBuffer = viewPort.getOutputFrameBuffer();
        renderManager.setForcedTechnique("GBuffer");
        viewPort.setOutputFrameBuffer(gBuffer);
    }

    @Override public void postQueue(RenderQueue rq) {

    }

    @Override public void postFrame(FrameBuffer out) {

        renderManager.setForcedTechnique(null);
        lightViewPort.setOutputFrameBuffer(tempBuffer);
        lightScene.updateGeometricState();

        renderManager.renderViewPort(lightViewPort, 0);
        renderManager.renderGeometry(ambientAndGlowRenderTarget);
        viewPort.setOutputFrameBuffer(tempBuffer);
    }

    @Override public void cleanup() {

    }

    @Override public void setProfiler(AppProfiler profiler) {
    }
}
