package de.toboidev.saimiri.game.collision;

/**
 * A static body that limits movement of the dynamic bodies. (E.g. the map)
 */
public abstract class StaticBody extends PhysicsBody{

    /**
     * Check if this static body limits a given movement
     *
     * @param body       The moving body at its start position
     * @param distance   The distance that the moving body wants to move
     * @param horizontal If true, horizontal movement, else vertical
     * @return distance, if the body doesn't obstruct the movement or the maximum distance before the movement is restricted.
     */
    public abstract double limitMovement(DynamicBody body, double distance, boolean horizontal);
}
