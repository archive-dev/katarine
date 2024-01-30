package io.github.whoisamyy.editor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import io.github.whoisamyy.components.Camera2D;
import io.github.whoisamyy.components.Sprite;
import io.github.whoisamyy.components.Transform2D;
import io.github.whoisamyy.components.TriggerBox;
import io.github.whoisamyy.logging.LogLevel;
import io.github.whoisamyy.logging.Logger;
import io.github.whoisamyy.objects.GameObject;
import io.github.whoisamyy.test.Game;
import io.github.whoisamyy.utils.Utils;
import io.github.whoisamyy.utils.input.Input;
import io.github.whoisamyy.editor.objects.Grid;

import java.util.LinkedList;

public class Editor extends ApplicationAdapter {
    private static Logger logger = new Logger(Editor.class.getTypeName());
    public static Editor instance;

    private boolean editorMode = true, debugRender = true;

    private LinkedList<GameObject> editorObjects = new LinkedList<>();
    private LinkedList<GameObject> gameObjects = new LinkedList<>();

    private float width, height;
    private boolean paused = false;

    protected SpriteBatch batch;
    protected World world;
    protected Box2DDebugRenderer renderer;
    protected OrthographicCamera camera;
    protected GameObject cam;

    Grid grid;
    ShapeRenderer shapeRenderer;
    ExtendViewport extendViewport;
    MouseCursor cursor;
    TriggerBox cursorBox;

    public static float getScreenToWorld() {
        return Utils.PPU;
    }

    public Editor() {
        this(1280, 720);
    }

    public Editor(int width, int height) {
        this.width = width / Utils.PPU;
        this.height = height / Utils.PPU;

        if (instance==null) instance = this;

    }

    @Override
    public void create() {
        Gdx.input.setInputProcessor(new Input());

        world = new World(new Vector2(0, -9.8f), false);
        batch = new SpriteBatch();
        renderer = new Box2DDebugRenderer();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        cursor = GameObject.instantiate(MouseCursor.class);
        cursorBox = cursor.getComponent(TriggerBox.class);

        if (editorMode) {
            grid = GameObject.instantiate(Grid.class);
            editorObjects.remove(grid); //FUCK YOU!
            grid.create();

            cam = GameObject.instantiate(GameObject.class);
            cam.addComponent(new Transform2D());
            cam.addComponent(new EditorCamera(width, height, batch));


            GameObject exmpl = GameObject.instantiate(GameObject.class);
            exmpl.addComponent(new Transform2D());
            exmpl.addComponent(new Sprite(batch, new Texture(Gdx.files.internal("bucket.png")), 5, 5));
            exmpl.getComponent(Transform2D.class).setPosition(new Vector2(0, height));

            GameObject exmpl2 = GameObject.instantiate(GameObject.class);
            exmpl2.addComponent(new Transform2D());
            exmpl2.addComponent(new Sprite(batch, new Texture(Gdx.files.internal("bucket.png")), 5, 5));
            exmpl2.getComponent(Transform2D.class).setPosition(new Vector2(0, height-5));

            GameObject exmpl3 = GameObject.instantiate(GameObject.class);
            exmpl3.addComponent(new Transform2D());
            exmpl3.addComponent(new Sprite(batch, new Texture(Gdx.files.internal("bucket.png")), 5, 5));
            exmpl3.getComponent(Transform2D.class).setPosition(new Vector2(0, height-10));

            GameObject exmpl4 = GameObject.instantiate(GameObject.class);
            exmpl4.addComponent(new Transform2D());
            exmpl4.addComponent(new Sprite(batch, new Texture(Gdx.files.internal("bucket.png")), 5, 5));
            exmpl4.getComponent(Transform2D.class).setPosition(new Vector2(0, height-15));


            editorObjects.forEach(GameObject::create);
        }

        if (camera==null)
            extendViewport = new ExtendViewport(1280, 720);
        else
            extendViewport = new ExtendViewport(1280, 720, camera);
    }

    @Override
    public void render() {
        if (paused) return;

        if (editorMode)
            camera = cam.getComponent(EditorCamera.class).getCamera();
        else
            camera = cam.getComponent(Camera2D.class).getCamera();

        ScreenUtils.clear(0, 0, 0, 1);

        if (editorMode) {
            shapeRenderer.begin();

            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(0, 0, Editor.instance.getWidth()*Utils.PPU, Editor.instance.getHeight()*Utils.PPU, new Color(0x0a0a0aff), new Color(0x0a0a0aff), new Color(0x1F1F1Fff), new Color(0x1F1F1Fff));
            shapeRenderer.end();
        }

        batch.begin();

        if (editorMode)
            grid.render();

        if (editorMode) {
            for (GameObject go : editorObjects) {
                go.render();
            }
        } else {
            for (GameObject go : gameObjects) {
                go.render();
            }
        }
        batch.end();

        if (debugRender)
            renderer.render(world, camera.combined);
        if (!editorMode) {
            world.step(1 / 240f, 6, 1);
        }

        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.D)) {
            logger.setLogLevel(LogLevel.DEBUG);
            logger.debug(String.valueOf(Thread.activeCount()));
        }
    }

    @Override
    public void pause() {
        logger.debug("paused");
        this.paused = true;
    }

    @Override
    public void resume() {
        logger.debug("unpaused");
        this.paused = false;
    }

    @Override
    public void dispose() {
        if (!editorMode) {
            for (GameObject go : gameObjects) {
                go.dispose();
            }
        }
        batch.dispose();
    }

    private void edit() {
        if (!editorMode) return;
        for (GameObject go : editorObjects) {
            try {
                Sprite sprite = go.getComponent(Sprite.class);


            } catch (NullPointerException ignored) {}
        }
    }

    public static Editor getInstance() {
        if (instance == null) instance = new Editor(1280, 720);

        return instance;
    }

    public boolean isEditorMode() {
        return editorMode;
    }

    public boolean isDebugRender() {
        return debugRender;
    }

    public LinkedList<GameObject> getEditorObjects() {
        return editorObjects;
    }

    public LinkedList<GameObject> getGameObjects() {
        return gameObjects;
    }

    public void addGameObject(GameObject go) {
        gameObjects.add(go);
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

    public World getWorld() {
        return world;
    }

    public Box2DDebugRenderer getRenderer() {
        return renderer;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public GameObject getCam() {
        return cam;
    }

    public TriggerBox getCursorBox() {
        return cursorBox;
    }

    protected void setEditorMode(boolean editorMode) {
        this.editorMode = editorMode;
    }
    public void setDebugRender(boolean debugRender) {
        this.debugRender = debugRender;
    }

    public GameObject getGameObjectById(long id) {
        if (this instanceof Game)
            for (GameObject go : gameObjects) {
                if (go.getId()==id) return go;
            }
        else
            for (GameObject go : editorObjects) {
                if (go.getId()==id) return go;
            }
        throw new NullPointerException("No GameObject with id "+id);
    }
}
