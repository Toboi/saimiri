package de.toboidev.saimiri.game.collision;

public abstract class DynamicBodyController {
    protected DynamicBody body;

    public void setBody(DynamicBody body) {
        this.body = body;
    }

    public abstract void tick(float tpf);
}
