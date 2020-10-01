package de.toboidev.saimiri.game.systems;

import com.simsilica.es.*;
import com.simsilica.sim.AbstractGameSystem;
import com.simsilica.sim.SimTime;
import de.toboidev.saimiri.es.components.Position;
import de.toboidev.saimiri.es.components.Size;
import de.toboidev.saimiri.game.collision.*;
import de.toboidev.saimiri.game.collision.staticbodies.StaticBox;
import de.toboidev.saimiri.game.components.CollisionSettingsComponent;
import de.toboidev.saimiri.game.components.DynamicBodyComponent;
import de.toboidev.saimiri.game.components.StaticCollider;

import java.util.HashMap;
import java.util.Map;

public class CollisionSystem extends AbstractGameSystem {

    private final World world;
    private StaticBoxContainer staticBoxContainer;
    private EntitySet dynamicBodyEntities;
    private EntitySet collisionSettings;
    private Map<EntityId, PhysicsBody> allBodies;
    private Map<EntityId, DynamicBody> dynamicBodies;
    private Map<EntityId, DynamicBodyController> pendingControllers;
    private EntityData ed;


    public CollisionSystem(World world) {
        this.world = world;
    }

    public CollisionSystem() {
        this(new World());
    }

    public World getWorld() {
        return world;
    }

    @Override
    protected void initialize() {
        ed = getSystem(EntityData.class, true);
        allBodies = new HashMap<>();
        dynamicBodies = new HashMap<>();
        pendingControllers = new HashMap<>();
        collisionSettings = ed.getEntities(CollisionSettingsComponent.class);
        dynamicBodyEntities = ed.getEntities(Position.class, Size.class, DynamicBodyComponent.class);
        staticBoxContainer = new StaticBoxContainer(ed);
    }

    @Override
    public void start() {
        staticBoxContainer.start();
        for (Entity e : dynamicBodyEntities) {
            addDynamicBody(e);
        }

    }

    @Override
    public void stop() {
        staticBoxContainer.stop();
        for (Entity e : dynamicBodyEntities) {
            removeDynamicBody(e);
        }
        for (EntityId eId : allBodies.keySet()) {
            removeBody(eId);
        }
    }

    @Override
    protected void terminate() {
        collisionSettings.release();
        dynamicBodyEntities.release();
    }

    @Override
    public void update(SimTime time) {
        staticBoxContainer.update();

        updateDynamicBodies();
        updateCollisionSettings();

        world.tick((float) time.getTpf());

        for (Entity e : dynamicBodyEntities) {
            DynamicBody body = dynamicBodies.get(e.getId());
            ed.setComponent(e.getId(), new Position((float) body.getX(), (float) body.getY()));
        }
    }

    public void addBody(EntityId eId, PhysicsBody body) {
        body.setUserData(eId.getId());
        world.addBody(body);
        allBodies.put(eId, body);
        if (collisionSettings.containsId(eId)) {
            updateCollisionSettings(collisionSettings.getEntity(eId));
        }
    }

    public void removeBody(EntityId eId) {
        PhysicsBody body = allBodies.remove(eId);
        world.removeBody(body);
    }

    private void updateCollisionSettings() {
        if (collisionSettings.applyChanges()) {
            collisionSettings.getAddedEntities().forEach(this::updateCollisionSettings);
            collisionSettings.getChangedEntities().forEach(this::updateCollisionSettings);
        }
    }

    private void updateCollisionSettings(Entity e) {
        PhysicsBody body = allBodies.get(e.getId());
        if (body != null) {
            CollisionSettingsComponent settings = e.get(CollisionSettingsComponent.class);
            body.setCollisionEnabled(settings.collisionEnabled);
            body.setCollisionEventsEnabled(settings.collisionEventsEnabled);
        }
    }

    private void updateDynamicBodies() {
        dynamicBodyEntities.applyChanges();
        for (Entity e : dynamicBodyEntities.getAddedEntities()) {
            addDynamicBody(e);
        }
        for (Entity e : dynamicBodyEntities.getRemovedEntities()) {
            removeDynamicBody(e);
        }
    }

    private void addDynamicBody(Entity e) {
        Position position = e.get(Position.class);
        Size size = e.get(Size.class);
        DynamicBody body = new DynamicBody(size.x, size.y, position.x, position.y);
        body.setUserData(e.getId().getId());
        DynamicBodyController controller = pendingControllers.remove(e.getId());
        body.setController(controller); //Is tolerant to null, so we don't have to check here
        dynamicBodies.put(e.getId(), body);
        addBody(e.getId(), body);
    }

    private void removeDynamicBody(Entity e) {
        DynamicBody body = dynamicBodies.remove(e.getId());
        removeBody(e.getId());
        DynamicBodyController controller = body.getController();
        if (controller != null) {
            controller.setBody(null);
            pendingControllers.put(e.getId(), controller);
        }
    }

    public void setDynamicBodyController(EntityId eId, DynamicBodyController controller) {
        DynamicBody body = dynamicBodies.get(eId);
        if (body != null) {
            body.setController(controller);
        } else {
            pendingControllers.put(eId, controller);
        }
    }

    public void removeDynamicBodyController(EntityId eId, DynamicBodyController controller) {
        DynamicBody body = dynamicBodies.get(eId);
        if (body != null) {
            if (body.getController() == controller) {
                body.setController(null);
            }
        } else {
            if (pendingControllers.get(eId) == controller) {
                pendingControllers.remove(eId);
            }
        }
    }


    private class StaticBoxContainer extends EntityContainer<StaticBox> {
        @SuppressWarnings("unchecked")
        StaticBoxContainer(EntityData ed) {
            super(ed, Size.class, Position.class, StaticCollider.class);
        }

        @Override
        protected StaticBox addObject(Entity e) {
            Position position = e.get(Position.class);
            Size size = e.get(Size.class);
            StaticBox collider = new StaticBox(position.x, position.y, size.x, size.y);
            addBody(e.getId(), collider);
            return collider;
        }

        @Override
        protected void updateObject(StaticBox object, Entity e) {

        }

        @Override
        protected void removeObject(StaticBox collider, Entity e) {
            removeBody(e.getId());
        }
    }
}
