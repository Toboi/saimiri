package de.toboidev.saimiri.gfx.render;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityContainer;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import de.toboidev.saimiri.es.EntityDataProvider;
import de.toboidev.saimiri.es.components.Position;
import de.toboidev.saimiri.es.components.Rotation;
import de.toboidev.saimiri.es.components.Scale;
import de.toboidev.saimiri.gfx.components.RenderComponent;
import de.toboidev.saimiri.gfx.render.spriteloaders.DefaultSpriteLoader;

/**
 * @author Eike Foede <toboi@toboidev.de>
 */
public class StaticRenderState extends BaseAppState {

    private static final String USERDATA_EID_KEY = "entityId";
    private RenderContainer container;
    private Node rootNode;
    private SpriteLoader spriteLoader;

    public StaticRenderState(Node rootNode) {
        this.rootNode = rootNode;
    }

    public StaticRenderState(Node rootNode, SpriteLoader spriteLoader) {
        this.rootNode = rootNode;
        this.spriteLoader = spriteLoader;
    }

    @Override protected void initialize(Application app) {
        EntityData ed = getState(EntityDataProvider.class).getEntityData();
        if (spriteLoader == null) {
            spriteLoader = new DefaultSpriteLoader(app.getAssetManager());
        }
        container = new RenderContainer(ed);
    }

    @Override protected void cleanup(Application app) {

    }

    @Override protected void onEnable() {
        container.start();
    }

    @Override protected void onDisable() {
        container.stop();
    }

    @Override public void update(float tpf) {
        container.update();
    }

    /**
     * @param eId
     * @return The Spatial that is representing the entity with the given id, if there is one, otherwise null.
     */
    public Spatial getSpatial(EntityId eId) {
        return container.getObject(eId);
    }

    /**
     * Returns the id of the entity that this spatial is representing.
     * Returns EntityId.NULL_ID if there is no corresponding Entity.
     *
     * @param spatial
     * @return Id of the Entity corresponding to the spatial or EntityId.NULL_ID if no Entity is found.
     */
    public EntityId getEntityId(Spatial spatial) {
        Long eId = spatial.getUserData(USERDATA_EID_KEY);
        if (eId != null) {
            return new EntityId(eId);
        }
        if (spatial.getParent() != null) {
            return getEntityId(spatial.getParent());
        }
        return EntityId.NULL_ID;
    }

    class RenderContainer extends EntityContainer<Spatial> {
        @SuppressWarnings("unchecked") RenderContainer(EntityData ed) {
            super(ed, StaticSprite.class, RenderComponent.class, Position.class, Rotation.class, Scale.class);
        }

        @Override protected Spatial addObject(Entity e) {
            StaticSprite sprite = e.get(StaticSprite.class);
            Spatial spatial = spriteLoader.loadSprite(sprite.sprite);
            spatial.setUserData(USERDATA_EID_KEY, e.getId().getId());
            rootNode.attachChild(spatial);
            updateObject(spatial, e);
            return spatial;
        }

        @Override protected void updateObject(Spatial spatial, Entity e) {
            Position position = e.get(Position.class);
            Rotation rotation = e.get(Rotation.class);
            Scale scale = e.get(Scale.class);
            RenderComponent renderComponent = e.get(RenderComponent.class);

            //Translation
            spatial.setLocalTranslation(position.asVector(renderComponent.layer));

            //Rotation
            spatial.setLocalRotation(rotation.asQuaternion());

            //Scale
            spatial.setLocalScale(scale.asVector());
        }

        @Override protected void removeObject(Spatial spatial, Entity e) {
            rootNode.detachChild(spatial);
        }
    }
}
