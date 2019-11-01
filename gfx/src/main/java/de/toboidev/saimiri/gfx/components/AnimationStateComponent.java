package de.toboidev.saimiri.gfx.components;

import com.simsilica.es.PersistentComponent;

public class AnimationStateComponent implements PersistentComponent {
    public final int currentImage;

    public AnimationStateComponent(int currentImage) {
        this.currentImage = currentImage;
    }

    public AnimationStateComponent() {
        this(0);
    }
}
