package de.toboidev.saimiri.game.systems;

import com.simsilica.es.Entity;
import com.simsilica.es.EntityContainer;
import com.simsilica.es.EntityData;
import com.simsilica.sim.AbstractGameSystem;
import de.toboidev.saimiri.game.collision.WorldTickListener;
import de.toboidev.saimiri.game.collision.controllers.PlatformerCharacterController;
import de.toboidev.saimiri.game.components.PlatformerCharacterComponent;
import de.toboidev.saimiri.game.components.PlatformerInput;

public class PlatformerCharacterSystem extends AbstractGameSystem implements WorldTickListener {

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


    private class CharacterContainer extends EntityContainer<PlatformerCharacterController> {
        @SuppressWarnings("unchecked") CharacterContainer(EntityData ed) {
            super(ed, PlatformerCharacterComponent.class, PlatformerInput.class);
        }

        @Override protected PlatformerCharacterController addObject(Entity e) {
            PlatformerCharacterComponent characterComponent = e.get(PlatformerCharacterComponent.class);
            PlatformerCharacterController controller = new PlatformerCharacterController();
            controller.setJumpSpeed(characterComponent.jumpSpeed);
            controller.setHorizontalSpeed(characterComponent.horizontalSpeed);
            controller.setGravity(characterComponent.gravity);
            updateObject(controller, e);
            collisionSystem.setDynamicBodyController(e.getId(), controller);
            return controller;
        }

        @Override protected void updateObject(PlatformerCharacterController controller, Entity e) {
            PlatformerInput input = e.get(PlatformerInput.class);
            if (input.jump) {
                controller.requestJump();
                ed.setComponent(e.getId(), input.withJump(false));
            }
            controller.setMoveLeft(input.left);
            controller.setMoveRight(input.right);
        }

        @Override protected void removeObject(PlatformerCharacterController controller, Entity e) {
            collisionSystem.removeDynamicBodyController(e.getId(), controller);
        }
    }
}
