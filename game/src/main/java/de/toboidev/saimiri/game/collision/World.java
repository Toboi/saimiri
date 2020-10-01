package de.toboidev.saimiri.game.collision;

import java.util.ArrayList;

public class World {

    private final ArrayList<StaticBody> staticBodies = new ArrayList<>();
    private final ArrayList<DynamicBody> dynamicBodies = new ArrayList<>();
    private final ArrayList<WorldTickListener> tickListeners = new ArrayList<>();
    private final ArrayList<CollisionListener> collisionListeners = new ArrayList<>();
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
        class potentialCollision {
            StaticBody body;
            double distance;

            public potentialCollision(StaticBody body, double distance) {
                this.body = body;
                this.distance = distance;
            }
        }

        ArrayList<potentialCollision> potentialCollisionEvents = new ArrayList<>();

        //Find all potentially colliding StaticBodies and calculate actual movement distance
        for (StaticBody obstructor : staticBodies) {
            double blockedDistance = obstructor.limitMovement(body, distance, horizontal);

            //Check for collision
            if (Math.abs(blockedDistance) < Math.abs(distance)) {

                //The movement is only limited when both bodies have collision enabled
                if (body.isCollisionEnabled() && obstructor.isCollisionEnabled()) {
                    distance = blockedDistance;
                }

                potentialCollisionEvents.add(new potentialCollision(obstructor, blockedDistance));
            }
        }

        //Now that we know how far the DynamicBody will be moving,
        //filter the potential collisions for the actual ones and notify CollisionListeners
        if (body.isCollisionEventsEnabled()) {
            for (potentialCollision c : potentialCollisionEvents) {
                if (Math.abs(c.distance) <= Math.abs(distance) && c.body.isCollisionEventsEnabled()) {
                    notifyCollisionListeners(body, c.body);
                }
            }
        }

        //Actually move the DynamicBody
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

    public void addCollisionListener(CollisionListener collisionListener) {
        collisionListeners.add(collisionListener);
    }

    public void removeCollisionListener(CollisionListener collisionListener) {
        collisionListeners.remove(collisionListener);
    }

    private void notifyCollisionListeners(DynamicBody dynamicBody, StaticBody staticBody) {
        for (CollisionListener listener : collisionListeners) {
            listener.collision(dynamicBody, staticBody);
        }
    }
}