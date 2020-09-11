package de.toboidev.saimiri.game.collision;

/**
 * A dynamic, moving body.
 * Has a collision shape of a box
 */
public class DynamicBody extends PhysicsBody{

    private final double width;
    private final double height;
    private DynamicBodyController controller;

    //Current position (Center)
    private double x;//Horizontal
    private double y;//Vertical

    public DynamicBody(double width, double height, double x, double y) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public DynamicBody(double width, double height) {
        this(width, height, 0, 0);
    }

    public void forceX(double x) {
        this.x = x;
    }

    public void forceY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double requestMovement(double amount, boolean horizontal) {
        return world.requestMovement(this, amount, horizontal);
    }

    void changePosition(double amount, boolean horizontal) {
        if (horizontal) {
            x += amount;
        } else {
            y += amount;
        }
    }

    public double getPosition(boolean horizontal) {
        return horizontal ? x : y;
    }

    public double getSize(boolean horizontal) {
        return horizontal ? width : height;
    }

    public double getExtent(boolean horizontal) {
        return getSize(horizontal) / 2.0f;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    void tick(float tpf) {
        if (controller != null) {
            controller.tick(tpf);
        }
    }

    public DynamicBodyController getController() {
        return controller;
    }

    public void setController(DynamicBodyController controller) {
        this.controller = controller;
        if (controller != null) {
            controller.setBody(this);

        }
    }
}
