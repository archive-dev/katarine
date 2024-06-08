package org.katarine.systems;

import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

public class CameraSystem extends System {
    private final AtomicLong lastID = new AtomicLong(0);

    protected volatile OrthographicCamera currentCamera = new OrthographicCamera();

    private final HashMap<Long, OrthographicCamera> cameras = new HashMap<>();

    @Override
    protected void awake() {
        registerCamera(currentCamera);
    }

    public final void registerCamera(OrthographicCamera cam) {
        cameras.put(lastID.getAndIncrement(), cam);
    }

    public final void unregisterCamera(OrthographicCamera cam) {
        cameras.values().remove(cam);
    }

    public final void setCurrentCamera(OrthographicCamera camera) {
        if (cameras.containsValue(camera))
            currentCamera = camera;
    }

    public final void setCurrentCamera(long id) {
        if (cameras.containsKey(id))
            currentCamera = cameras.get(id);
    }

    public final OrthographicCamera getCurrentCamera() {
        return currentCamera;
    }

    @Override
    protected void update() {
        currentCamera.update();
    }
}
