package de.toboidev.saimiri.game.systems;

import com.simsilica.es.*;
import com.simsilica.sim.AbstractGameSystem;
import com.simsilica.sim.SimTime;
import de.toboidev.saimiri.es.components.Position;
import de.toboidev.saimiri.es.components.Size;
import de.toboidev.saimiri.game.collision.DynamicBody;
import de.toboidev.saimiri.game.collision.DynamicBodyController;
import de.toboidev.saimiri.game.collision.World;
import de.toboidev.saimiri.game.collision.staticbodies.StaticBox;
import de.toboidev.saimiri.game.components.DynamicBodyComponent;
import de.toboidev.saimiri.game.components.StaticCollider;

import java.util.HashMap;
import java.util.Map;

public class CollisionSystem extends AbstractGameSystem {

    private final World world;
    private StaticBoxContainer staticBoxContainer;
    private EntitySet dynamicBodyEntities;
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

    @Override protected void initialize() {
        ed = getSystem(EntityData.class, true);
    }

    @Override public void start() {
        staticBoxContainer = new StaticBoxContainer(ed);
        staticBoxContainer.start();

        dynamicBodies = new HashMap<>();
        pendingControllers = new HashMap<>();
        dynamicBodyEntities = ed.getEntities(Position.class, Size.class, DynamicBodyComponent.class);
        for (Entity e : dynamicBodyEntities) {
            addDynamicBody(e);
        }
    }

    @Override public void update(SimTime time) {
        staticBoxContainer.update();

        updateDynamicBodies();

        world.tick((float) time.getTpf());

        for (Entity e : dynamicBodyEntities) {
            DynamicBody body = dynamicBodies.get(e.getId());
            ed.setComponent(e.getId(), new Position((float) body.getX(), (float) body.getY()));
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
        DynamicBodyController controller = pendingControllers.remove(e.getId());
        body.setController(controller); //Is tolerant to null, so we don't have to check here
        world.addBody(body);
        dynamicBodies.put(e.getId(), body);
    }

    private void removeDynamicBody(Entity e) {
        DynamicBody body = dynamicBodies.remove(e.getId());
        world.removeBody(body);
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

    @Override public void stop() {
        staticBoxContainer.stop();
        for (Entity e : dynamicBodyEntities) {
            removeDynamicBody(e);
        }
        dynamicBodyEntities.release();
    }

    @Override protected void terminate() {
    }

    private class StaticBoxContainer extends EntityContainer<StaticBox> {
        @SuppressWarnings("unchecked") StaticBoxContainer(EntityData ed) {
            super(ed, Size.class, Position.class, StaticCollider.class);
        }

        @Override protected StaticBox addObject(Entity e) {
            Position position = e.get(Position.class);
            Size size = e.get(Size.class);
            StaticBox collider = new StaticBox(position.x, position.y, size.x, size.y);
            world.addBody(collider);
            return collider;
        }

        @Override protected void updateObject(StaticBox object, Entity e) {

        }

        @Override protected void removeObject(StaticBox collider, Entity e) {
            world.removeBody(collider);
        }
    }
}
