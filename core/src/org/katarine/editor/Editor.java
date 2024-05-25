package org.katarine.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.katarine.components.Camera2D;
import org.katarine.components.Component;
import org.katarine.components.Sprite;
import org.katarine.core.Window;
import org.katarine.editor.components.CursorHandler;
import org.katarine.editor.components.EditorCamera;
import org.katarine.editor.components.EditorObjectComponent;
import org.katarine.editor.objects.Grid;
import org.katarine.Game;
import org.katarine.logging.LogLevel;
import org.katarine.logging.Logger;
import org.katarine.objects.GameObject;
import org.katarine.objects.Scene;
import org.katarine.ui.Button;
import org.katarine.ui.TextLabel;
import org.katarine.ui.imgui.AppendableGui;
import org.katarine.ui.imgui.Gui;
import org.katarine.ui.imgui.ImGui;
import org.katarine.ui.imgui.Panel;
import org.katarine.utils.Utils;
import org.katarine.utils.input.AbstractInputHandler;
import org.katarine.utils.input.Input;
import org.katarine.utils.serialization.Assets;
import org.katarine.utils.serialization.ObjectRepresentation;
import org.katarine.utils.serialization.Yaml;
import org.katarine.utils.structs.UniquePriorityQueue;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.*;

public class Editor extends Window {
    private static final Logger logger = new Logger(Editor.class.getTypeName());
    public static Editor editorInstance;
    protected Lwjgl3Application app;

    private boolean editorMode = true, debugRender = true;

    private final UniquePriorityQueue<GameObject> editorObjects = new UniquePriorityQueue<>(new GameObjectComparator());
    private final UniquePriorityQueue<GameObject> gameObjects = new UniquePriorityQueue<>(new GameObjectComparator());

    public Scene getCurrentScene() { // TODO: add scene management
        return editor;
    }

    private static class GameObjectComparator implements Comparator<GameObject> {
        @Override
        public int compare(GameObject o1, GameObject o2) {
            return Integer.compare(o1.updateOrder, o2.updateOrder);
        }
    }

    public static ArrayDeque<GameObject> gameObjectsCreationQueue = new ArrayDeque<>(32);
    public static ArrayDeque<GameObject> gameObjectsDestroyQueue = new ArrayDeque<>(32);

    public static ArrayDeque<Component> componentsCreationQueue = new ArrayDeque<>(64); // 64 because there is always more components than game objects

    public Scene editor;

    private float width, height; // if width and height are sizes of window
    private float editorWidth, editorHeight; // editorWidth and editorHeight are sizes of editor space in window
    private boolean paused = false;

    protected SpriteBatch batch;
    protected SpriteBatch uiBatch;
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

    private boolean created = false;

    public final void preCreate() {
        if (created) return;
        logger.setLogLevel(LogLevel.DEBUG);
        Gdx.input.setInputProcessor(new Input());

        batch = new SpriteBatch();
        uiBatch = new SpriteBatch();
        shapeDrawer = new ShapeDrawer(batch, new TextureRegion(new Texture(Assets.get("whitepx.png"))));
        uiShapeDrawer = new ShapeDrawer(uiBatch, new TextureRegion(new Texture(Assets.get("whitepx.png"))));
        shapeDrawer.setDefaultLineWidth(1 / Utils.PPU);

        batch.enableBlending();
        uiBatch.enableBlending();

        editor = new Scene("Scene");
        editor.init();
        editor.removeComponent(EditorObjectComponent.class);
        editor.addComponent(CursorHandler.instance());
        editor.create();

        created = true;
    }

    @Override
    public void create() {
        long t1;
        t1 = System.currentTimeMillis();
        preCreate();

        ImGui.init();
        if (editorMode) {

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
            uiObject.addComponent(new TextLabel());

            GameObject sprite = GameObject.instantiate(GameObject.class, uiObject);
            sprite.addComponent(new Sprite(new Texture(Assets.get("bucket.png")), 1, 1));

            GameObject.instantiate(GameObject.class).addComponent(new Button());

            ImGui.addPanel(inspectorPanel);
            ImGui.addPanel(sceneHierarchy);

            Gui gui1 = new SceneViewGui().generateGameObjectTree(editor);
            Gui gui2 = SceneViewGui.getCtxMenu();
            AppendableGui gui = new AppendableGui();
            gui.add(gui2);
            gui.add(gui1);
            sceneHierarchy.setGui(gui);

            editorObjects.forEach(GameObject::create);
        } else {
            mainCamera = cam.getComponent(Camera2D.class).getCamera();
        }


        screenViewport = new ScreenViewport(mainCamera);
        screenViewport.setUnitsPerPixel(1/Utils.PPU);

        mainCamera.position.set(0, 0, 0);
        mainCamera.zoom = 1;


        logger.debug("Created in "+(System.currentTimeMillis() - t1) + "ms");
    }

    final Panel inspectorPanel = new Panel("Inspector");
    final Panel sceneHierarchy = new Panel("Scene");

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


            editor.render();
            editorObjects.forEach(GameObject::render);

            if (EditorObjectComponent.selection.size() == 1) {
                inspectorPanel.setGui(InspectorGui.generate(go = EditorObjectComponent.selection.toArray(GameObject[]::new)[0]));
                SceneViewGui.selectedGameObject = go;
            }
            else
                inspectorPanel.setGui(null);
        } else {
            gameObjects.forEach(GameObject::render);
        }
        uiBatch.end();
        batch.end();

        if (AbstractInputHandler.InputHandler.areKeysPressed(
                com.badlogic.gdx.Input.Keys.CONTROL_LEFT,
                com.badlogic.gdx.Input.Keys.S
        )) {
            final Yaml<GameObject> yaml = new Yaml<>();
            for (GameObject obj : editorObjects) {
                var s = yaml.toString(ObjectRepresentation.fromMap(obj.getClass(), obj.getFields()));
                Assets.save(obj.getName() + ".obj.yml", s);
            }
        }

        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.D)) {
            logger.setLogLevel(LogLevel.DEBUG);
            List<String> list = new ArrayList<>();
            for (GameObject gameObject : EditorObjectComponent.selection) {
                list.add(gameObject.getName());
            }
            logger.debug(Arrays.toString(list.toArray(new String[0])));
            if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.T)) {
                logger.debug(Thread.activeCount() + " threads");
            }
        }

        ImGui.render();

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
        throw new NullPointerException("No GameObject found with id: "+id);
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

    public final void setApp(Lwjgl3Application app) {
        if (this.app==null)
            this.app = app;
    }

    public final Lwjgl3Application getApp() {
        return app;
    }
}
