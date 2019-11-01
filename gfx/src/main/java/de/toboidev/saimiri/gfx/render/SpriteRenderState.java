package de.toboidev.saimiri.gfx.render;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityContainer;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import de.toboidev.saimiri.es.EntityDataProvider;
import de.toboidev.saimiri.es.components.Position;
import de.toboidev.saimiri.es.components.Rotation;
import de.toboidev.saimiri.es.components.Size;
import de.toboidev.saimiri.gfx.components.RenderComponent;
import de.toboidev.saimiri.gfx.components.SpriteComponent;
import de.toboidev.saimiri.gfx.render.spriteloaders.DefaultSpriteLoader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Eike Foede <toboi@toboidev.de>
 */
public class SpriteRenderState extends BaseAppState {

    private static final String USERDATA_EID_KEY = "entityId";
    private RenderContainer container;
    private final Node rootNode;
    private SpriteLoader spriteLoader;

    private final HashMap<EntityId, ArrayList<Control>> pendingControls;

    public SpriteRenderState(Node rootNode) {
        this.rootNode = rootNode;
        pendingControls = new HashMap<>();
    }

    public SpriteRenderState(Node rootNode, SpriteLoader spriteLoader) {
        this(rootNode);
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
     * @param eId The entity for which the spatial should be retrieved.
     * @return The Spatial that is representing the entity with the given id, if there is one, otherwise null.
     */
    public Spatial getSpatial(EntityId eId) {
        if (container == null) {
            return null;
        }
        return container.getObject(eId);
    }

    /**
     * Returns the id of the entity that this spatial is representing.
     * Returns EntityId.NULL_ID if there is no corresponding Entity.
     *
     * @param spatial The spatial to which the entity should be found.
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

    /**
     * Attach a Control to the spatial representing the given Entity. If no spatial has been created for this entity,
     * the attachment is deferred until a spatial gets created for this entity.
     *
     * @param eId     The id of the entity.
     * @param control The control to attach
     */
    public void attachControl(EntityId eId, Control control) {
        Spatial spatial = getSpatial(eId);
        if (spatial != null) {
            spatial.addControl(control);
        } else {
            ArrayList<Control> controlList = pendingControls.get(eId);
            if (controlList == null) {
                controlList = new ArrayList<>();
                pendingControls.put(eId, controlList);
            }
            controlList.add(control);
        }
    }

    public void removeControl(EntityId eId, Control control) {
        Spatial spatial = getSpatial(eId);
        if (spatial != null) {
            spatial.removeControl(control);
        } else {
            ArrayList<Control> controlList = pendingControls.get(eId);
            if (controlList != null) {
                controlList.remove(control);
            }
        }
    }

    private class RenderContainer extends EntityContainer<Spatial> {
        @SuppressWarnings("unchecked") RenderContainer(EntityData ed) {
            super(ed, SpriteComponent.class, RenderComponent.class, Position.class, Rotation.class, Size.class);
        }

        @Override protected Spatial addObject(Entity e) {
            SpriteComponent sprite = e.get(SpriteComponent.class);
            Spatial spatial = spriteLoader.loadSprite(sprite.sprite);

            spatial.setUserData(USERDATA_EID_KEY, e.getId().getId());

            if (pendingControls.containsKey(e.getId())) {
                for (Control c : pendingControls.get(e.getId())) {
                    spatial.addControl(c);
                }
                pendingControls.remove(e.getId());
            }

            rootNode.attachChild(spatial);
            updateObject(spatial, e);
            return spatial;
        }

        @Override protected void updateObject(Spatial spatial, Entity e) {
            Position position = e.get(Position.class);
            Rotation rotation = e.get(Rotation.class);
            Size size = e.get(Size.class);
            RenderComponent renderComponent = e.get(RenderComponent.class);

            //Translation
            spatial.setLocalTranslation(position.asVector(renderComponent.layer));

            //Rotation
            spatial.setLocalRotation(rotation.asQuaternion());

            //Scale
            spatial.setLocalScale(size.asVector());
        }

        @Override protected void removeObject(Spatial spatial, Entity e) {
            rootNode.detachChild(spatial);
        }
    }
}
