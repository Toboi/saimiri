package de.toboidev.saimiri.gfx.animation;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityContainer;
import com.simsilica.es.EntityData;
import de.toboidev.saimiri.es.EntityDataProvider;
import de.toboidev.saimiri.gfx.components.AnimationBaseComponent;
import de.toboidev.saimiri.gfx.components.AnimationStateComponent;
import de.toboidev.saimiri.gfx.render.SpriteRenderState;

public class SpriteAnimationRenderState extends BaseAppState {

    private EntityData ed;
    private SpriteRenderState spriteRenderState;
    private AnimationContainer container;

    @Override protected void initialize(Application app) {
        ed = getState(EntityDataProvider.class).getEntityData();
        spriteRenderState = getState(SpriteRenderState.class);
        container = new AnimationContainer(ed);
    }

    @Override protected void cleanup(Application app) {
    }

    @Override protected void onEnable() {
        container.start();
    }

    @Override public void update(float tpf) {
        container.update();
    }

    @Override protected void onDisable() {
        container.stop();
    }

    private class AnimationContainer extends EntityContainer<AnimationControl> {
        @SuppressWarnings("unchecked") AnimationContainer(EntityData ed) {
            super(ed, AnimationStateComponent.class, AnimationBaseComponent.class);
        }

        @Override protected AnimationControl addObject(Entity e) {
            AnimationBaseComponent animationComponent = e.get(AnimationBaseComponent.class);
            AnimationControl animControl = new AnimationControl(animationComponent.imagesX, animationComponent.imagesY, animationComponent.padding);
            updateObject(animControl, e);
            spriteRenderState.attachControl(e.getId(), animControl);
            return animControl;
        }

        @Override protected void updateObject(AnimationControl animControl, Entity e) {
            AnimationStateComponent animState = e.get(AnimationStateComponent.class);
            animControl.setCurrentImage(animState.currentImage);
        }

        @Override protected void removeObject(AnimationControl animControl, Entity e) {
            spriteRenderState.removeControl(e.getId(), animControl);
        }
    }
}
