package de.toboidev.saimiri.gfx.components;

import com.simsilica.es.PersistentComponent;

public class AnimationBaseComponent implements PersistentComponent {
    public final int imagesX;
    public final int imagesY;
    public final int padding;

    public AnimationBaseComponent(int imagesX, int imagesY, int padding) {
        this.imagesX = imagesX;
        this.imagesY = imagesY;
        this.padding = padding;
    }

    public AnimationBaseComponent(int imagesX, int imagesY) {
        this(imagesX, imagesY, 0);
    }
}
