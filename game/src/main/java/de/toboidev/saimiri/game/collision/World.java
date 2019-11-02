package de.toboidev.saimiri.game.collision;

import java.util.ArrayList;

public class World {

    private final ArrayList<StaticBody> staticBodies = new ArrayList<>();
    private final ArrayList<DynamicBody> dynamicBodies = new ArrayList<>();
    private final ArrayList<WorldTickListener> tickListeners = new ArrayList<>();
    public double COLLISION_WIDTH = 0.0001f;

    /**
     * Tries to move a dynamic body in the given direction the given amount
     *
     * @param body       The moving body
     * @param distance   The distance that the body wants to move
     * @param horizontal Whether it is a horizontal or vertical movement
     * @return The actually traveled distance
     */
    double requestMovement(DynamicBody body, double distance, boolean horizontal) {

        if (distance > 0) {
            for (StaticBody obstructor : staticBodies) {
                distance = Math.min(distance, obstructor.limitMovement(body, distance, horizontal));
            }
        } else {

            for (StaticBody obstructor : staticBodies) {
                distance = Math.max(distance, obstructor.limitMovement(body, distance, horizontal));
            }
        }
        body.changePosition(distance, horizontal);
        return distance;
    }

    public void tick(float tpf) {
        for (WorldTickListener tickListener : tickListeners) {
            tickListener.preWorldTick(tpf);
        }

        for (DynamicBody db : dynamicBodies) {
            db.tick(tpf);
        }

        for (WorldTickListener tickListener : tickListeners) {
            tickListener.postWorldTick(tpf);
        }
    }

    public void addBody(DynamicBody body) {
        body.setWorld(this);
        dynamicBodies.add(body);
    }

    public void addBody(StaticBody body) {
        body.setWorld(this);
        staticBodies.add(body);
    }

    public void removeBody(DynamicBody body) {
        dynamicBodies.remove(body);
        body.setWorld(null);
    }

    public void removeBody(StaticBody body) {
        staticBodies.remove(body);
        body.setWorld(null);
    }

    public void addTickListener(WorldTickListener tickListener) {
        tickListeners.add(tickListener);
    }

    public void removeTickListener(WorldTickListener tickListener) {
        tickListeners.remove(tickListener);
    }
}