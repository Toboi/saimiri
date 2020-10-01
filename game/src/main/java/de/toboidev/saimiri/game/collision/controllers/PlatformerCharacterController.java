package de.toboidev.saimiri.game.collision.controllers;

import de.toboidev.saimiri.game.collision.DynamicBodyController;

public class PlatformerCharacterController extends DynamicBodyController {


    //SETTINGS
    private double jumpSpeed = 450; //How fast the character will initially move upwards when jumping
    private double horizontalSpeed = 250; //The maximum horizontal speed
    private double gravity = 1500; //The player gets this much accelerated downwards per tick

    //STATE
    private boolean left = false, right = false, jump = false; //The current control state
    private double upSpeed = 0; //The current upwards speed
    private boolean onGround = false;

    public double getJumpSpeed() {
        return jumpSpeed;
    }

    public void setJumpSpeed(double jumpSpeed) {
        this.jumpSpeed = jumpSpeed;
    }

    public double getHorizontalSpeed() {
        return horizontalSpeed;
    }

    public void setHorizontalSpeed(double horizontalSpeed) {
        this.horizontalSpeed = horizontalSpeed;
    }

    public double getGravity() {
        return gravity;
    }

    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    @Override
    public void tick(float tpf) {

        //Horizontal movement
        //The current horizontal speed
        double currentSpeed = left ? -horizontalSpeed : 0;
        currentSpeed += right ? horizontalSpeed : 0;

        body.requestMovement(currentSpeed * tpf, true);


        //Vertical movement
        if (jump) {
            if (onGround) {
                upSpeed = jumpSpeed;
            }
            jump = false;
        }
        double requestedMovement = upSpeed * tpf;
        double actualMovement = body.requestMovement(requestedMovement, false);
        if (actualMovement > requestedMovement) {
            //Hitting floor
            onGround = true;
            upSpeed = -gravity * tpf;
        } else if (actualMovement < requestedMovement) {
            //Hitting ceiling
            onGround = false;
            upSpeed = -gravity * tpf;
        } else {
            upSpeed -= gravity * tpf;
            onGround = false;
        }
    }


    public void setMoveLeft(boolean move) {
        left = move;
    }

    public void setMoveRight(boolean move) {
        right = move;
    }

    public void requestJump() {
        jump = true;
    }

}
