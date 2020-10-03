package de.toboidev.saimiri.game.collision;

public abstract class PhysicsBody {
    protected World world;

    public void setWorld(World world) {
        this.world = world;
    }

    /**
     * Only if this is true on two colliding bodies, registered CollisionListeners will be notified of the collision.
     */
    private boolean collisionEventsEnabled = false;

    public boolean isCollisionEventsEnabled() {
        return collisionEventsEnabled;
    }

    public void setCollisionEventsEnabled(boolean collisionEventsEnabled) {
        this.collisionEventsEnabled = collisionEventsEnabled;
    }

    /**
     * If this is false, this object will not collide with other objects, only trigger collision events.
     */
    private boolean collisionEnabled = true;

    public boolean isCollisionEnabled() {
        return collisionEnabled;
    }

    public void setCollisionEnabled(boolean collisionEnabled) {
        this.collisionEnabled = collisionEnabled;
    }

    /**
     * Can store whatever the user needs
     */
    private Object userData;

    public Object getUserData() {
        return userData;
    }

    public void setUserData(Object userData) {
        this.userData = userData;
    }
}
