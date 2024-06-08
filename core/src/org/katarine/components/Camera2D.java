package org.katarine.components;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import org.katarine.annotations.EditorObject;
import org.katarine.rendering.RenderingSystem;
import org.katarine.systems.EditorCameraSystem;
import org.katarine.utils.Utils;

@EditorObject
public class Camera2D extends Component {
    private volatile OrthographicCamera camera;

    private float width,
            height,
            zoom = 1f;
    private volatile
            SpriteBatch batch;

    public Camera2D() {}


    @Override
    protected void awake() {
        batch = getSystemManager().getSystem(RenderingSystem.class).getSpriteBatch();
        camera = getSystemManager().getSystem(EditorCameraSystem.class).getCurrentCamera();

        camera.setToOrtho(false);
    }

    @Override
    public void update() {
        zoom = camera.zoom;
        getTransform().pos.set(new Vector2(camera.position.x, camera.position.y));

        camera.update();
//        batch.setProjectionMatrix(camera.combined);
//        uiBatch.setProjectionMatrix(mainCamera.combined);
    }

    public void zoom(float amount) {
        camera.zoom += amount;
    }

    public void resize(int newWidth, int newHeight) {
        this.width = newWidth/ Utils.PPU;
        this.height = newHeight/ Utils.PPU;
        camera.setToOrtho(false, newWidth, newHeight);
    }

    public void setPosition(Vector2 pos) {
        camera.position.set(pos, camera.position.z);
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public float getZoom() {
        return zoom;
    }

    public static Vector2 screenToWorld(float x, float y, Camera2D cam) {
        Vector3 v3 = cam.getCamera().unproject(new Vector3(x, y, 0));
        return new Vector2(v3.x, v3.y);
    }

    public static Vector2 screenToWorld(Vector2 v2, Camera2D cam) {
        return screenToWorld(v2.x, v2.y, cam);
    }

    public static Vector2 worldToScreen(float x, float y, Camera2D cam) {
        Vector3 v3 = cam.getCamera().project(new Vector3(x, y, 0));
        return new Vector2(v3.x, v3.y);
    }

    public static Vector2 worldToScreen(Vector2 v2, Camera2D cam) {
        return worldToScreen(v2.x, v2.y, cam);
    }
}
