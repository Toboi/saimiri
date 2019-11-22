package de.toboidev.saimiri.gfx.render;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityContainer;
import com.simsilica.es.EntityData;
import de.toboidev.saimiri.es.EntityDataProvider;
import de.toboidev.saimiri.es.components.Position;
import de.toboidev.saimiri.es.components.Rotation;
import de.toboidev.saimiri.gfx.components.CameraController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Eike Foede <toboi@toboidev.de>
 */
public class CameraControllerState extends BaseAppState
{
    public static final float FRUSTUM_NEAR = -1000;
    public static final float FRUSTUM_FAR = 1000;

    private final Map<String, Camera> cameras = new HashMap<>();
    private CameraContainer container;

    public static void setupCamera(Camera cam, float width, float height)
    {
        cam.setParallelProjection(true);
        if (height == 0)
        {
            height = width / cam.getWidth() * cam.getHeight();
        }
        else if (width == 0)
        {
            width = height / cam.getHeight() * cam.getWidth();
        }

        float halfWidth = width / 2.0f;
        float halfHeight = height / 2.0f;

        cam.setFrustum(FRUSTUM_NEAR, FRUSTUM_FAR, -halfWidth, halfWidth, halfHeight, -halfHeight);
        cam.setAxes(Vector3f.UNIT_X.mult(-1), Vector3f.UNIT_Y, Vector3f.UNIT_Z.mult(-1));
        cam.setLocation(Vector3f.ZERO);
    }

    @Override protected void initialize(Application app)
    {
        cameras.put("default", app.getCamera());
        EntityData ed = getState(EntityDataProvider.class).getEntityData();
        container = new CameraContainer(ed);
    }

    public Camera putCamera(String identifier, Camera camera)
    {
        return cameras.put(identifier, camera);
    }

    @Override protected void cleanup(Application app)
    {
    }

    @Override protected void onEnable()
    {
        container.start();
    }

    @Override public void update(float tpf)
    {
        container.update();
    }

    @Override protected void onDisable()
    {
        container.stop();
    }

    private class CameraContainer extends EntityContainer<Camera>
    {
        @SuppressWarnings("unchecked") CameraContainer(EntityData ed)
        {
            super(ed, CameraController.class, Position.class, Rotation.class);
        }

        @Override protected Camera addObject(Entity e)
        {
            CameraController cc = e.get(CameraController.class);

            Camera cam = cameras.get(cc.cameraId);

            // Fallback to avoid crashing on invalid cameras
            if (cam == null)
            {
                //TODO_Log ERROR Did not find camera XY!
                cam = new Camera(1, 1);
            }
            cam.setParallelProjection(true);

            float width = cc.viewWidth;
            float height = cc.viewHeight;

            setupCamera(cam, width, height);
            updateObject(cam, e);

            return cam;
        }

        @Override protected void updateObject(Camera cam, Entity e)
        {
            Position position = e.get(Position.class);
            Rotation rotation = e.get(Rotation.class);

            cam.setLocation(new Vector3f(position.x, position.y, 0));
            Quaternion q = new Quaternion();
            q.fromAngles(0, FastMath.PI, rotation.radians);
            cam.setRotation(q);
        }

        @Override protected void removeObject(Camera object, Entity e)
        {
        }
    }
}
