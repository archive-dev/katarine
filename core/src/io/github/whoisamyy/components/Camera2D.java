package io.github.whoisamyy.components;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import io.github.whoisamyy.logging.Logger;
import io.github.whoisamyy.utils.EditorObject;

@EditorObject
public class Camera2D extends Component {
    private Transform2D transform2D;
    private OrthographicCamera camera;

    private float width, height, zoom;
    private SpriteBatch batch;
    private static Logger logger = new Logger(Camera2D.class.getTypeName());

    public Camera2D(float width, float height, SpriteBatch batch) {
        this.width = width;
        this.height = height;
        this.batch = batch;
        camera = new OrthographicCamera(this.width, this.height);
        camera.setToOrtho(false, this.width, this.height);
    }

    @Override
    public void start() {
        transform2D = gameObject.transform;
        logger.debug(""+transform2D);
    }

    @Override
    public void update() {
        zoom = camera.zoom;
        transform2D.pos.set(new Vector2(camera.position.x, camera.position.y));

        camera.update();
        batch.setProjectionMatrix(camera.combined);
    }

    public void zoom(float amount) {
        camera.zoom += amount;
    }

    public void resize(float newWidth, float newHeight) {
        this.width = newWidth;
        this.height = newHeight;
        camera.setToOrtho(false, newWidth, newHeight);
    }

    public void setPosition(Vector2 pos) {
        camera.position.set(pos, camera.position.z);
    }

    public Transform2D getTransform2D() {
        return transform2D;
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
}
