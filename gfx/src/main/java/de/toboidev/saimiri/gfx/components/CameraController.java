package de.toboidev.saimiri.gfx.components;

import com.jme3.renderer.Camera;
import com.simsilica.es.PersistentComponent;

/**
 * If either viewWidth or viewHeight is initialized as 0, it will be set according to the other value to preserve aspect ratio of the camera.
 *
 * @author Eike Foede <toboi@toboidev.de>
 */
public class CameraController implements PersistentComponent
{
    public final float viewWidth;
    public final float viewHeight;
    public final String cameraId;

    /**
     * This will control camera "default" and set width. Height will be set accordingly, preserving aspect ratio of the Camera.
     * @param viewWidth
     */
    public CameraController(float viewWidth)
    {
        this(viewWidth, 0);
    }

    /**
     * This will control camera "default"
     * @param viewWidth
     * @param viewHeight
     */
    public CameraController(float viewWidth, float viewHeight)
    {
        this(viewWidth, viewHeight, "default");
    }

    /**
     *
     * @param viewWidth
     * @param viewHeight
     * @param cameraId The identifier of the camera to specify. See {@link de.toboidev.saimiri.es.render.CameraControllerState#putCamera(String, Camera)}
     */
    public CameraController(float viewWidth, float viewHeight, String cameraId)
    {
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        this.cameraId = cameraId;
    }

}
