package de.toboidev.saimiri.examples.simpleDeferredLighting;

import com.jme3.app.DebugKeysAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioListenerState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import de.toboidev.saimiri.es.DefaultEntityDataProvider;
import de.toboidev.saimiri.es.components.Position;
import de.toboidev.saimiri.es.components.Rotation;
import de.toboidev.saimiri.es.components.Size;
import de.toboidev.saimiri.gfx.animation.AnimationControl;
import de.toboidev.saimiri.gfx.animation.SpriteAnimationRenderState;
import de.toboidev.saimiri.gfx.components.*;
import de.toboidev.saimiri.gfx.deferred.DeferredLightingProcessor;
import de.toboidev.saimiri.gfx.deferred.LightSystem;
import de.toboidev.saimiri.gfx.render.CameraControllerState;
import de.toboidev.saimiri.gfx.render.SpriteRenderState;
import de.toboidev.saimiri.gfx.render.spriteloaders.DeferredLightingSpriteLoader;

/**
 * @author Eike Foede <toboi@toboidev.de>
 */
public class Main extends SimpleApplication {
    EntityData ed;
    EntityId center;
    EntityId light1;
    EntityId light2;
    EntityId animatedSprite;
    AnimationControl ac;

    public static void main(String[] args) {
        Main m = new Main();
        m.start();
    }

    public Main() {
        super(new AudioListenerState(), new DebugKeysAppState());
    }

    @Override public void simpleInitApp() {
        setPauseOnLostFocus(false);
//        stateManager.attach(new VideoRecorderAppState());

        DeferredLightingProcessor dp = new DeferredLightingProcessor(assetManager);
        dp.setAmbientLight(ColorRGBA.Black);
        viewPort.addProcessor(dp);

        DefaultEntityDataProvider edp = new DefaultEntityDataProvider();
        ed = edp.getEntityData();

        stateManager.attach(edp);
        stateManager.attach(new SpriteRenderState(rootNode, new DeferredLightingSpriteLoader(assetManager)));
        stateManager.attach(new LightSystem(dp));
        stateManager.attach(new CameraControllerState());
        stateManager.attach(new SpriteAnimationRenderState());


        center = ed.createEntity();
        ed.setComponents(center, new Position(0, 0), new Size(1.5f, 1.5f), new Rotation(0f), new SpriteComponent("scifiwall/scifiwall.jpg"));
        ed.setComponent(center, new RenderComponent(-9));
        ed.setComponent(center, new PointLightComponent(0.3f, ColorRGBA.Cyan, 0.1f));
        ed.setComponent(center, new CameraController(0, 1f, "default"));

        light1 = ed.createEntity();
        ed.setComponents(light1, new PointLightComponent(0.9f, ColorRGBA.Green, 0.2f));
        ed.setComponents(light1, new Size(0.07f, 0.07f), new Rotation(0f), new SpriteComponent("light.png"), new RenderComponent());

        light2 = ed.createEntity();
        ed.setComponents(light2, new PointLightComponent(1.5f, ColorRGBA.Orange, 0.3f));
        ed.setComponents(light2, new Size(0.1f, 0.1f), new Rotation(0f), new SpriteComponent("light.png"), new RenderComponent());

        animatedSprite = ed.createEntity();
        ed.setComponents(animatedSprite, new Position(0, 0), new Size(0.5f, 0.5f), new Rotation(0f), new SpriteComponent("animationTest.png"), new RenderComponent(0));
        ed.setComponents(animatedSprite, new AnimationBaseComponent(2, 2));
        ed.setComponents(animatedSprite, new AnimationStateComponent(1));
    }

    @Override public void simpleUpdate(float tpf) {
        //Just animate some properties for a somewhat interesting result

        float time = getTimer().getTimeInSeconds();
        ed.setComponents(animatedSprite, new AnimationStateComponent(((int) (time * 1.5f) % 4)));
        time *= 0.7f;
        //ed.setComponent(center, new Position(FastMath.sin(time), FastMath.cos(time)));
        time = time * 0.5f;
        ed.setComponent(center, new PointLightComponent(0.7f, ColorRGBA.Cyan, 0.3f * (FastMath.sin(time) + 1.5f)));
        time = -time * 3.4f;
        ed.setComponent(light1, new Position(FastMath.sin(time) * 0.6f, FastMath.cos(time) * 0.6f));
        time = time * 0.4f;
        ed.setComponent(light2, new Position(FastMath.sin(time), 0));
    }
}