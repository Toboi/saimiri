package de.toboidev.saimiri.game.components;

import com.simsilica.es.EntityComponent;

public class PlatformerInput implements EntityComponent {
    public final boolean left;
    public final boolean right;
    public final boolean jump;

    public PlatformerInput(boolean left, boolean right, boolean jump) {
        this.left = left;
        this.right = right;
        this.jump = jump;
    }

    public PlatformerInput() {
        this(false, false, false);
    }

    public PlatformerInput withLeft(boolean left) {
        return new PlatformerInput(left, right, jump);
    }

    public PlatformerInput withRight(boolean right) {
        return new PlatformerInput(left, right, jump);
    }

    public PlatformerInput withJump(boolean jump) {
        return new PlatformerInput(left, right, jump);
    }
}
