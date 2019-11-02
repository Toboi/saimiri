package de.toboidev.saimiri.game.collision.controllers;

import de.toboidev.saimiri.game.collision.DynamicBodyController;

public class TopDownCharacterController extends DynamicBodyController {

    //SETTINGS
    private double speed = 200; //The maximum speed

    //CONTROL STATE
    private boolean left = false, right = false, up = false, down = false;

    public TopDownCharacterController() {
    }

    public TopDownCharacterController(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setMoveLeft(boolean move) {
        left = move;
    }

    public void setMoveRight(boolean move) {
        right = move;
    }

    public void setMoveUp(boolean move) {
        up = move;
    }

    public void setMoveDown(boolean move) {
        down = move;
    }


    @Override public void tick(float tpf) {

        //Horizontal movement
        double horizontalSpeed = left ? -speed : 0;
        horizontalSpeed += right ? speed : 0;

        body.requestMovement(horizontalSpeed * tpf, true);


        double verticalSpeed = up ? speed : 0;
        verticalSpeed += down ? -speed : 0;

        body.requestMovement(verticalSpeed * tpf, false);

    }
}
