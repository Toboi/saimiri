package de.toboidev.saimiri.game.components;

import com.simsilica.es.EntityComponent;

public class TopDownInput implements EntityComponent {

    public final boolean left;
    public final boolean right;
    public final boolean up;
    public final boolean down;

    public TopDownInput(boolean left, boolean right, boolean up, boolean down) {
        this.left = left;
        this.right = right;
        this.up = up;
        this.down = down;
    }

    public TopDownInput() {
        this(false, false, false, false);
    }

    public TopDownInput withLeft(boolean left) {
        return new TopDownInput(left, right, up, down);
    }

    public TopDownInput withRight(boolean right) {
        return new TopDownInput(left, right, up, down);
    }

    public TopDownInput withUp(boolean up) {
        return new TopDownInput(left, right, up, down);
    }

    public TopDownInput withDown(boolean down) {
        return new TopDownInput(left, right, up, down);
    }
}
