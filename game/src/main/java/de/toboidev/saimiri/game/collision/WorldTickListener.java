package de.toboidev.saimiri.game.collision;

public interface WorldTickListener {
    void preWorldTick(float tpf);

    void postWorldTick(float tpf);
}
