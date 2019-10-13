package de.toboidev.saimiri.gfx.deferred;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityContainer;
import com.simsilica.es.EntityData;
import de.toboidev.saimiri.es.EntityDataProvider;
import de.toboidev.saimiri.es.components.Position;
import de.toboidev.saimiri.gfx.components.PointLightComponent;
import de.toboidev.saimiri.gfx.deferred.lights.PointLight;

/**
 * @author Eike Foede <toboi@toboidev.de>
 */
public class LightSystem extends BaseAppState {
    private DeferredLightingProcessor dp;
    private PointLightContainer pointLightContainer;
    private AssetManager assetManager;

    public LightSystem(DeferredLightingProcessor dp) {
        this.dp = dp;
    }

    @Override protected void initialize(Application app) {
        assetManager = app.getAssetManager();
        EntityData ed = getState(EntityDataProvider.class).getEntityData();
        pointLightContainer = new PointLightContainer(ed);
    }

    @Override protected void cleanup(Application app) {
    }

    @Override protected void onEnable() {
        pointLightContainer.start();
    }

    @Override public void update(float tpf) {
        pointLightContainer.update();
    }

    @Override protected void onDisable() {
        pointLightContainer.stop();
    }

    class PointLightContainer extends EntityContainer<PointLight> {
        @SuppressWarnings("unchecked") PointLightContainer(EntityData ed) {
            super(ed, PointLightComponent.class, Position.class);
        }

        @Override protected PointLight addObject(Entity e) {
            PointLight pl = new PointLight(assetManager);
            updateObject(pl, e);
            dp.addLight(pl);
            return pl;
        }

        @Override protected void updateObject(PointLight light, Entity e) {
            Position pos = e.get(Position.class);
            light.setPosition(pos.x, pos.y);
            PointLightComponent pc = e.get(PointLightComponent.class);
            light.setRadius(pc.radius);
            light.setLightColor(pc.color);
            light.setHeight(pc.height);
        }

        @Override protected void removeObject(PointLight light, Entity e) {
            dp.removeLight(light);
        }
    }
}
