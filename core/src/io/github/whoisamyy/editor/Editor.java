package io.github.whoisamyy.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.whoisamyy.components.Camera2D;
import io.github.whoisamyy.components.Component;
import io.github.whoisamyy.core.Window;
import io.github.whoisamyy.editor.components.CursorHandler;
import io.github.whoisamyy.editor.components.EditorCamera;
import io.github.whoisamyy.editor.components.EditorObjectComponent;
import io.github.whoisamyy.editor.objects.Grid;
import io.github.whoisamyy.katarine.Game;
import io.github.whoisamyy.logging.LogLevel;
import io.github.whoisamyy.logging.Logger;
import io.github.whoisamyy.objects.GameObject;
import io.github.whoisamyy.ui.TextLabel;
import io.github.whoisamyy.ui.imgui.ImGui;
import io.github.whoisamyy.utils.Utils;
import io.github.whoisamyy.utils.input.AbstractInputHandler;
import io.github.whoisamyy.utils.input.Input;
import io.github.whoisamyy.utils.structs.UniquePriorityQueue;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Editor extends Window {
    private static final Logger logger = new Logger(Editor.class.getTypeName());
    public static Editor editorInstance;

    private boolean editorMode = true, debugRender = true;

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

    public GameObject editor;

    private float width, height; // if width and height are sizes of window
    private float editorWidth, editorHeight; // editorWidth and editorHeight are sizes of editor space in window
    private boolean paused = false;

    protected SpriteBatch batch;
    protected SpriteBatch uiBatch;
    protected World world;
    protected Box2DDebugRenderer renderer;
    protected OrthographicCamera mainCamera;
    protected OrthographicCamera uiCamera;
    protected GameObject cam;

    private Grid grid;
    private ShapeDrawer shapeDrawer;
    private ShapeDrawer uiShapeDrawer;
    private ScreenViewport screenViewport;

    public static float getScreenToWorld() {
        return Utils.PPU;
    }

    public Editor() {
        this(1280, 720);
        this.editorHeight = this.height;
        this.editorWidth = this.width;
    }

    public Editor(int width, int height) {
        super();
        this.width = width / Utils.PPU;
        this.height = height / Utils.PPU;

        this.editorHeight = this.height;
        this.editorWidth = this.width;
        if (editorInstance ==null) editorInstance = this;
    }

    @Override
    public void create() {
        long t1;
        t1 = System.currentTimeMillis();
        logger.setLogLevel(LogLevel.DEBUG);
        Gdx.input.setInputProcessor(new Input());

        world = new World(new Vector2(0, -9.8f), false);
        batch = new SpriteBatch();
        uiBatch = new SpriteBatch();
        renderer = new Box2DDebugRenderer();
        shapeDrawer = new ShapeDrawer(batch, new TextureRegion(new Texture(Gdx.files.internal("whitepx.png"))));
        uiShapeDrawer = new ShapeDrawer(uiBatch, new TextureRegion(new Texture(Gdx.files.internal("whitepx.png"))));
        shapeDrawer.setDefaultLineWidth(1 / Utils.PPU);

        batch.enableBlending();
        uiBatch.enableBlending();

        if (editorMode) {
            editor = GameObject.instantiate(GameObject.class);
            editor.removeComponent(EditorObjectComponent.class);
            editor.addComponent(CursorHandler.instance());
            editor.create();

            grid = GameObject.instantiate(Grid.class);
            grid.removeComponent(EditorObjectComponent.class);
            editorObjects.remove(grid);
            grid.create();
            editor.addChild(grid);

            cam = GameObject.instantiate(GameObject.class);
            cam.addComponent(new EditorCamera(width, height, batch, uiBatch));
            cam.removeComponent(EditorObjectComponent.class);

            if (editorMode) {
                mainCamera = cam.getComponent(EditorCamera.class).getCamera();
            }

            GameObject uiObject = GameObject.instantiate(GameObject.class);
            uiObject.addComponent(new TextLabel(true));

            editorObjects.forEach(GameObject::create);


        } else {
            mainCamera = cam.getComponent(Camera2D.class).getCamera();
        }

        mainCamera.position.set(0, 0, 0);

        screenViewport = new ScreenViewport(mainCamera);
        screenViewport.setUnitsPerPixel(1/Utils.PPU);

        ImGui.init();

        logger.debug("Created in "+(System.currentTimeMillis() - t1) + "ms");
    }

    @Override
    public void render() {
        if (paused) return;
        shapeDrawer.setDefaultLineWidth(1f / Utils.PPU * mainCamera.zoom);
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

        uiBatch.begin();
        batch.begin();
        if (editorMode) {
            shapeDrawer.filledRectangle(getMainCamera().position.x- getMainCamera().zoom*screenViewport.getWorldWidth()/2,
                    getMainCamera().position.y- getMainCamera().zoom*screenViewport.getWorldHeight()/2,
                    this.getWidth() * getMainCamera().zoom, this.getHeight() * getMainCamera().zoom,
                    new Color(0x0a0a0aff),
                    new Color(0x0a0a0aff),
                    new Color(0x1F1F1Fff),
                    new Color(0x1F1F1Fff));
            grid.render();
        }
        if (editorMode) {
            editorObjects.forEach(GameObject::render);
        } else {
            gameObjects.forEach(GameObject::render);
        }
        uiBatch.end();
        batch.end();

        ImGui.render();

        if (debugRender)
            renderer.render(world, mainCamera.combined);
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
        mainCamera.update(true);
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
        uiBatch.dispose();
        ImGui.dispose();
    }

    public static Editor getEditorInstance() {
        if (editorInstance == null) editorInstance = new Editor(1280, 720);

        return editorInstance;
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

    public final SpriteBatch getUiBatch() {
        return uiBatch;
    }

    public World getWorld() {
        return world;
    }

    public Box2DDebugRenderer getRenderer() {
        return renderer;
    }

    public OrthographicCamera getMainCamera() {
        return mainCamera;
    }

    public GameObject getCam() {
        return cam;
    }

    @SuppressWarnings("SameParameterValue")
    protected void setEditorMode(boolean editorMode) {
        this.editorMode = editorMode;
    }
    public void setDebugRender(boolean debugRender) {
        this.debugRender = debugRender;
    }

    public final GameObject getGameObjectById(long id) {
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

    public ShapeDrawer getShapeDrawer() {
        return shapeDrawer;
    }

    public final ShapeDrawer getUiShapeDrawer() {
        return uiShapeDrawer;
    }

    public final ScreenViewport getScreenViewport() {
        return screenViewport;
    }
}
