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
import io.github.whoisamyy.components.Component;
import io.github.whoisamyy.components.Sprite;
import io.github.whoisamyy.ui.*;
import io.github.whoisamyy.editor.components.EditorCamera;
import io.github.whoisamyy.editor.components.EditorObjectComponent;
import io.github.whoisamyy.editor.objects.Grid;
import io.github.whoisamyy.katarine.Game;
import io.github.whoisamyy.logging.LogLevel;
import io.github.whoisamyy.logging.Logger;
import io.github.whoisamyy.objects.GameObject;
import io.github.whoisamyy.utils.Utils;
import io.github.whoisamyy.utils.input.AbstractInputHandler;
import io.github.whoisamyy.utils.input.Input;
import io.github.whoisamyy.utils.render.shapes.CircleShape;
import io.github.whoisamyy.utils.render.shapes.RenderableShape;
import io.github.whoisamyy.utils.structs.UniquePriorityQueue;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Editor extends ApplicationAdapter {
    private static final Logger logger = new Logger(Editor.class.getTypeName());
    public static Editor instance;

    private boolean editorMode = true, debugRender = true;

    private final LinkedList<RenderableShape> shapes = new LinkedList<>();
    private final UniquePriorityQueue<GameObject> editorObjects = new UniquePriorityQueue<>(new GameObjectComparator());
    private final UniquePriorityQueue<GameObject> gameObjects = new UniquePriorityQueue<>(new GameObjectComparator());

    private static class GameObjectComparator implements Comparator<GameObject> {
        @Override
        public int compare(GameObject o1, GameObject o2) {
            return Integer.compare(o1.updateOrder, o2.updateOrder);
        }
    }

    public static ArrayDeque<GameObject> gameObjectsCreationQueue = new ArrayDeque<>(32);
    public static ArrayDeque<GameObject> gameObjectsDestroyQueue = new ArrayDeque<>(32);

    public static ArrayDeque<Component> componentsCreationQueue = new ArrayDeque<>(64); // 64 because there is always more components than game objects

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
        long t1;
        t1 = System.currentTimeMillis();
        logger.setLogLevel(LogLevel.DEBUG);
        Gdx.input.setInputProcessor(new Input());

        world = new World(new Vector2(0, -9.8f), false);
        batch = new SpriteBatch();
        renderer = new Box2DDebugRenderer();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

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


            GameObject bucket = GameObject.instantiate(GameObject.class);
            bucket.addComponent(new Sprite(batch, new Texture(Gdx.files.internal("bucket.png")), 5, 5));
            bucket.transform.pos.add(5, 5);

            GameObject exampleText = GameObject.instantiate(GameObject.class);
            exampleText.addComponent(new Text("fonts/Roboto-Medium.ttf", 1, Color.WHITE, 1 / Utils.PPU, Color.BLACK, true));

            GameObject u = GameObject.instantiate(GameObject.class);
            Canvas c = u.addComponent(new Canvas());
            u.addComponent(new UiCircleShape());

            GameObject buttonO = GameObject.instantiate(GameObject.class);
            GameObject button1 = GameObject.instantiate(GameObject.class);
//            Button button = buttonO.addComponent(new Button());
//            button.anchor = Anchor.CENTER;
//            button.fontSize = 1.2f;
//            button.buttonSize.set(5, 2);
//            button.addAction(() -> logger.debug("Click!"));

            TextLabel tl = buttonO.addComponent(new TextLabel());
            tl.text = "watafk\nneeee\nkek";
            tl.labelSize.set(5, 5);
            tl.anchor = Anchor.CENTER;

            Panel panel = button1.addComponent(new Panel());
            panel.color = Color.GOLD;

            CircleShape circle = new CircleShape(0, 0, 1f);


            editorObjects.forEach(GameObject::create);


        } else
            camera = cam.getComponent(Camera2D.class).getCamera();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.setTransformMatrix(camera.view);

        camera.position.set(0, 0, 0);

        screenViewport = new ScreenViewport(camera);
        screenViewport.setUnitsPerPixel(1/Utils.PPU);

        logger.debug("Created in "+(System.currentTimeMillis() - t1) + "ms");
    }

    @Override
    public void render() {
        if (paused) return;

        Component c;
        while ((c = componentsCreationQueue.poll())!=null) {
            c.create();
            c.gameObject.getComponents().add(c);
        }
        GameObject go;
        while ((go = gameObjectsCreationQueue.poll())!=null) {
            go.create();
            if (editorMode)
                editorObjects.add(go);
            else
                gameObjects.add(go);
        }
        while ((go = gameObjectsDestroyQueue.poll())!=null) {
            if (editorMode)
                editorObjects.remove(go);
            else
                gameObjects.remove(go);
        }

        ScreenUtils.clear(0, 0, 0, 0);

        if (editorMode) {
            shapeRenderer.begin();
            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(0, 0, this.getWidth()*2, this.getHeight()*2, new Color(0x0a0a0aff), new Color(0x0a0a0aff),
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
        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.updateMatrices();
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
