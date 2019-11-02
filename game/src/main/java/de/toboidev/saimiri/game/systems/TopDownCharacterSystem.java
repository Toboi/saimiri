package de.toboidev.saimiri.game.systems;

import com.simsilica.es.Entity;
import com.simsilica.es.EntityContainer;
import com.simsilica.es.EntityData;
import com.simsilica.sim.AbstractGameSystem;
import de.toboidev.saimiri.game.collision.WorldTickListener;
import de.toboidev.saimiri.game.collision.controllers.TopDownCharacterController;
import de.toboidev.saimiri.game.components.TopDownCharacterComponent;
import de.toboidev.saimiri.game.components.TopDownInput;

public class TopDownCharacterSystem extends AbstractGameSystem implements WorldTickListener {

    private CharacterContainer characterContainer;
    private CollisionSystem collisionSystem;
    private EntityData ed;

    @Override protected void initialize() {
        ed = getSystem(EntityData.class, true);
        collisionSystem = getSystem(CollisionSystem.class, true);
        characterContainer = new CharacterContainer(ed);
    }

    @Override public void start() {
        characterContainer.start();
        collisionSystem.getWorld().addTickListener(this);
    }


    @Override public void preWorldTick(float tpf) {
        characterContainer.update();
    }

    @Override public void postWorldTick(float tpf) {

    }

    @Override public void stop() {
        collisionSystem.getWorld().removeTickListener(this);
        characterContainer.stop();
    }

    @Override protected void terminate() {

    }


    private class CharacterContainer extends EntityContainer<TopDownCharacterController> {
        @SuppressWarnings("unchecked") CharacterContainer(EntityData ed) {
            super(ed, TopDownCharacterComponent.class, TopDownInput.class);
        }

        @Override protected TopDownCharacterController addObject(Entity e) {
            TopDownCharacterComponent characterComponent = e.get(TopDownCharacterComponent.class);
            TopDownCharacterController controller = new TopDownCharacterController(characterComponent.speed);
            updateObject(controller, e);
            collisionSystem.setDynamicBodyController(e.getId(), controller);
            return controller;
        }

        @Override protected void updateObject(TopDownCharacterController controller, Entity e) {
            TopDownInput input = e.get(TopDownInput.class);
            controller.setMoveLeft(input.left);
            controller.setMoveRight(input.right);
            controller.setMoveUp(input.up);
            controller.setMoveDown(input.down);
        }

        @Override protected void removeObject(TopDownCharacterController controller, Entity e) {
            collisionSystem.removeDynamicBodyController(e.getId(), controller);
        }
    }
}
