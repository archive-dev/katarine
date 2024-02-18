package io.github.whoisamyy.editor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.whoisamyy.components.Camera2D;
import io.github.whoisamyy.components.Sprite;
import io.github.whoisamyy.components.Text;
import io.github.whoisamyy.components.TriggerBox;
import io.github.whoisamyy.editor.components.EditorCamera;
import io.github.whoisamyy.editor.components.EditorObjectComponent;
import io.github.whoisamyy.editor.objects.Grid;
import io.github.whoisamyy.editor.objects.MouseCursor;
import io.github.whoisamyy.katarine.Game;
import io.github.whoisamyy.logging.LogLevel;
import io.github.whoisamyy.logging.Logger;
import io.github.whoisamyy.objects.GameObject;
import io.github.whoisamyy.ui.Canvas;
import io.github.whoisamyy.ui.UiRectShape;
import io.github.whoisamyy.utils.Utils;
import io.github.whoisamyy.utils.input.AbstractInputHandler;
import io.github.whoisamyy.utils.input.Input;
import io.github.whoisamyy.utils.render.shapes.RenderableShape;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Editor extends ApplicationAdapter {
    private static Logger logger = new Logger(Editor.class.getTypeName());
    public static Editor instance;

    private boolean editorMode = true, debugRender = true;

    private LinkedList<RenderableShape> shapes = new LinkedList<>();
    private PriorityQueue<GameObject> editorObjects = new PriorityQueue<>(new GameObjectComparator());
    private PriorityQueue<GameObject> gameObjects = new PriorityQueue<>(new GameObjectComparator());

    private class GameObjectComparator implements Comparator<GameObject> {
        @Override
        public int compare(GameObject o1, GameObject o2) {
            return Integer.compare(o1.updateOrder, o2.updateOrder);
        }
    }

    private float width, height;
    private boolean paused = false;

    protected SpriteBatch batch;
    protected SpriteBatch uiBatch;
    protected World world;
    protected Box2DDebugRenderer renderer;
    protected OrthographicCamera camera;
    protected GameObject cam;

    Grid grid;
    ShapeRenderer shapeRenderer;
    ScreenViewport screenViewport;
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
        logger.setLogLevel(LogLevel.DEBUG);
        Gdx.input.setInputProcessor(new Input());

        world = new World(new Vector2(0, -9.8f), false);
        batch = new SpriteBatch();
        renderer = new Box2DDebugRenderer();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);


        cursor = GameObject.instantiate(MouseCursor.class);
        cursorBox = cursor.getComponent(TriggerBox.class);

        RenderableShape.init(shapeRenderer);

        if (editorMode) {
            grid = GameObject.instantiate(Grid.class);
//            grid.removeComponent(EditorObjectComponent.EditorTriggerBox.class);
            grid.removeComponent(EditorObjectComponent.class);
            editorObjects.remove(grid); //FUCK YOU!
            grid.create();

            cam = GameObject.instantiate(GameObject.class);
            cam.addComponent(new EditorCamera(width, height, batch));
//            cam.removeComponent(EditorObjectComponent.EditorTriggerBox.class);
            cam.removeComponent(EditorObjectComponent.class);

            if (editorMode) {
                camera = cam.getComponent(EditorCamera.class).getCamera();
            }

            shapeRenderer.setProjectionMatrix(camera.combined);

            GameObject bucket = GameObject.instantiate(GameObject.class);
            bucket.addComponent(new Sprite(batch, new Texture(Gdx.files.internal("bucket.png")), 5, 5));
            bucket.transform.pos.add(5, 5);

            GameObject exampleText = GameObject.instantiate(GameObject.class);
            exampleText.addComponent(new Text("fonts/Roboto-Medium.ttf", 1, Color.WHITE, 1 / Utils.PPU, Color.BLACK, true));

            GameObject u = GameObject.instantiate(GameObject.class);
            Canvas c = u.addComponent(new Canvas());

            GameObject rU = GameObject.instantiate(GameObject.class);
            rU.addComponent(new UiRectShape()).setCanvas(c);
            rU.getComponent(UiRectShape.class).getUiPosition().set(2, 2);

            logger.setLogLevel(LogLevel.DEBUG);
            long t1;
            t1 = System.currentTimeMillis();

            editorObjects.forEach(GameObject::create);

            logger.debug(System.currentTimeMillis() - t1);
            logger.debug(shapes.size());

        } else
            camera = cam.getComponent(Camera2D.class).getCamera();

        camera.position.set(0, 0, 0);

        screenViewport = new ScreenViewport(camera);
        screenViewport.setUnitsPerPixel(1/Utils.PPU);
    }

    @Override
    public void render() {
        if (paused) return;

        if (editorMode) {
            GameObject go;
            while ((go = GameObject.creationQueue.poll())!=null) {
                go.create();
                editorObjects.add(go);
            }
            while ((go = GameObject.destroyQueue.poll())!=null) {
                editorObjects.remove(go);
            }
        }
        else {
            GameObject go;
            while ((go = GameObject.creationQueue.poll())!=null) {
                go.create();
                gameObjects.add(go);
            }
            while ((go = GameObject.destroyQueue.poll())!=null) {
                gameObjects.remove(go);
            }
        }
        GameObject.creationQueue.clear();

        ScreenUtils.clear(0, 0, 0, 0);

        if (editorMode) {
            shapeRenderer.begin();
            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(0, 0, getWidth(), getHeight(), new Color(0x0a0a0aff), new Color(0x0a0a0aff),
                    new Color(0x1F1F1Fff), new Color(0x1F1F1Fff));
            shapeRenderer.end();
        }

        batch.begin();
        if (editorMode)
            grid.render();
        if (editorMode) {
            editorObjects.forEach(GameObject::render);
        } else {
            gameObjects.forEach(GameObject::render);
        }
        batch.end();

        //shapes

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendEquation(GL20.GL_FUNC_ADD);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        if (editorMode) {
            shapeRenderer.begin();

            shapes.forEach(RenderableShape::render);

            shapeRenderer.end();
        }

        Gdx.gl.glDisable(GL20.GL_BLEND);

        //

        if (debugRender)
            renderer.render(world, camera.combined);
        if (editorMode) {
            world.clearForces();
        }
        world.step(!editorMode?(1 / 240f):Gdx.graphics.getDeltaTime(), 64, 64); // честно без понятия зачем, но вроде как должно улучшить отзывчивостьъ

        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.D)) {
            logger.setLogLevel(LogLevel.DEBUG);
            if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.T)) {
                logger.debug(Thread.activeCount() + " threads");
            }

        } else if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.U)) {
            for (GameObject go : editorObjects) {
                try {
                    Canvas c = go.getComponent(Canvas.class);
                    c.setShowUI(!c.isShowUI());
                    break;
                } catch (NullPointerException ignored) {}
            }
        }

        try {
            Utils.setStaticFieldValue(AbstractInputHandler.class, "touchDownEvent", null);
            Utils.setStaticFieldValue(AbstractInputHandler.class, "touchUpEvent", null);
            Utils.setStaticFieldValue(AbstractInputHandler.class, "dragEvent", null);
            Utils.setStaticFieldValue(AbstractInputHandler.class, "scrollEvent", null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void resize(int width, int height) {
        screenViewport.update(width, height, true);
        this.width = width/Utils.PPU;
        this.height = height/Utils.PPU;
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

    public PriorityQueue<GameObject> getEditorObjects() {
        return editorObjects;
    }

    public PriorityQueue<GameObject> getGameObjects() {
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

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    public LinkedList<RenderableShape> getShapes() {
        return shapes;
    }
}
