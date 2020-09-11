package de.toboidev.saimiri.game.collision;

public abstract class PhysicsBody {
    protected World world;

    void setWorld(World world) {
        this.world = world;
    }

    /**
     * Only if this is true on two colliding bodies, registered CollisionListeners will be notified of the collision.
     */
    private boolean collisionResponseEnabled = false;

    public boolean isCollisionResponseEnabled() {
        return collisionResponseEnabled;
    }

    public void setCollisionResponseEnabled(boolean collisionResponseEnabled) {
        this.collisionResponseEnabled = collisionResponseEnabled;
    }
}
