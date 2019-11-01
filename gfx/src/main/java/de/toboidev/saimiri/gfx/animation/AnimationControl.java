package de.toboidev.saimiri.gfx.animation;

import com.jme3.material.Material;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.control.AbstractControl;
import com.jme3.texture.Image;

public class AnimationControl extends AbstractControl {

    private int rows;
    private int columns;
    private int currentImage;
    private int paddingPixels;
    private float paddingRelativeX;
    private float paddingRelativeY;

    public AnimationControl(int rows, int columns, int padding, int currentImage) {
        this.rows = rows;
        this.columns = columns;
        this.paddingPixels = padding;
        this.currentImage = currentImage;
    }

    public AnimationControl(int rows, int columns, int padding) {
        this(rows, columns, padding, 0);
    }

    public AnimationControl(int rows, int columns) {
        this(rows, columns, 0);
    }

    public AnimationControl() {
        this(1, 1);
    }

    public void setPadding(int pixels) {
        paddingPixels = pixels;
        updateRelativePadding();
        updateSpatial();
    }

    public int getCurrentImage() {
        return currentImage;
    }

    public void setCurrentImage(int image) {
        currentImage = image;
        updateSpatial();
    }

    @Override protected void controlUpdate(float tpf) {

    }

    @Override protected void controlRender(RenderManager rm, ViewPort vp) {

    }

    @Override public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        updateRelativePadding();
        updateSpatial();
    }

    private void updateSpatial() {
        if (!(spatial instanceof Geometry)) {
            return;
        }
        Geometry g = (Geometry) spatial;

        int row = currentImage / columns;
        int column = currentImage % columns;


        float x0 = column / (float) columns + paddingRelativeX;
        float x1 = (column + 1) / (float) columns - paddingRelativeX;
        float y0 = 1 - ((row + 1) / (float) rows) + paddingRelativeY;
        float y1 = 1 - (row / (float) rows) - paddingRelativeY;
        g.getMesh().setBuffer(VertexBuffer.Type.TexCoord, 2, new float[]{
                x0, y0,
                x0, y1,
                x1, y1,
                x1, y0});
    }

    private void updateRelativePadding() {
        if (paddingPixels == 0) {
            paddingRelativeX = 0;
            paddingRelativeY = 0;
        } else {
            if (spatial == null) {
                return;
            }
            Material mat = ((Geometry) spatial).getMaterial();
            Image image = mat.getTextureParam("Color").getTextureValue().getImage();
            paddingRelativeX = paddingPixels / (float) image.getWidth();
            paddingRelativeY = paddingPixels / (float) image.getHeight();
        }
    }
}
