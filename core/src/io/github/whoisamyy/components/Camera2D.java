package io.github.whoisamyy.components;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Camera2D extends Component {
    private Transform2D transform2D;
    private OrthographicCamera camera;

    private float width, height, zoom;
    private SpriteBatch batch;

    public Camera2D(float width, float height, SpriteBatch batch) {
        this.width = width;
        this.height = height;
        this.batch = batch;
        camera = new OrthographicCamera(this.width, this.height);
        camera.setToOrtho(false, this.width, this.height);
    }

    @Override
    public void start() {
        transform2D = gameObject.getComponent(Transform2D.class);
    }

    @Override
    public void update() {
        zoom = camera.zoom;
        camera.position.set(transform2D.pos, 0);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
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
