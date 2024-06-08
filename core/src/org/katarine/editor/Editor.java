package org.katarine.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.graphics.OrthographicCamera;
import org.katarine.core.Window;
import org.katarine.logging.LogLevel;
import org.katarine.logging.Logger;
import org.katarine.scenes.Scene;
import org.katarine.scenes.SceneManager;
import org.katarine.systems.EditorCameraSystem;
import org.katarine.utils.Utils;
import org.katarine.utils.input.Input;
import org.katarine.utils.input.InputSystem;

public class Editor extends Window {
    private static final Logger logger = new Logger(Editor.class.getTypeName());
    public static Editor editorInstance;
    protected Lwjgl3Application app;

    private boolean
            editorMode = true,
            debugRender = true,
            paused = false;

    public Scene editor;

    private volatile float width, height;

    public static float getScreenToWorld() {
        return Utils.PPU;
    }

    public Editor() {
        this(1280, 720);
    }

    public Editor(int width, int height) {
        super();
        this.width = width / Utils.PPU;
        this.height = height / Utils.PPU;
        if (editorInstance ==null) editorInstance = this;
    }

    private boolean created = false;

    protected SceneManager sceneManager;

    public final void preCreate() {
        if (created) return;
        logger.setLogLevel(LogLevel.DEBUG);
        Gdx.input.setInputProcessor(new Input());
    }

    @Override
    public void create() {
        long t1;
        t1 = System.currentTimeMillis();
        preCreate();



        if (editorMode) {
            sceneManager = new EditorSceneManager();

//            ImGui.addPanel(inspectorPanel);
//            ImGui.addPanel(sceneHierarchy);

//            Gui gui1 = new SceneViewGui().generateGameObjectTree();
//            Gui gui2 = SceneViewGui.getCtxMenu();
//            AppendableGui gui = new AppendableGui();
//            gui.add(gui2);
//            gui.add(gui1);
//            sceneHierarchy.setGui(gui);

        } else {
            sceneManager = new SceneManager();
        }
        sceneManager.init();

        sceneManager.create();

        this.created = true;

        logger.debug("Created in "+(System.currentTimeMillis() - t1) + "ms");
    }

    @Override
    public void render() {
        if (paused) return;
        sceneManager.render();


/*
        if (InputHandler.areKeysPressed(
                com.badlogic.gdx.Input.Keys.CONTROL_LEFT,
                com.badlogic.gdx.Input.Keys.S
        )) {
            final Yaml<GameObject> yaml = new Yaml<>(GameObject.class);
            for (GameObject obj : editorObjects) {
                var s = yaml.toString(ObjectRepresentation.fromMap(obj.getClass(), obj.getFields()));
                Assets.save(obj.getName() + ".obj.yml", s);
            }
        }
*/
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.R)) {
            sceneManager.getScene(0).getSystemManager().getSystem(EditorCameraSystem.class).getCurrentCamera().position.set(0, 0, 0);
            ((OrthographicCamera) sceneManager.getScene(0).getSystemManager().getSystem(EditorCameraSystem.class).getCurrentCamera()).zoom = 1;
        }

        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.D)) {
            logger.setLogLevel(LogLevel.DEBUG);

            logger.debug(sceneManager.getScene(0).getSystemManager().getSystem(EditorCameraSystem.class).getCurrentCamera().position);
//            List<String> list = new ArrayList<>();
//            for (GameObject gameObject : EditorObjectComponent.selection) {
//                list.add(gameObject.getName());
//            }
//            logger.debug(Arrays.toString(list.toArray(new String[0])));
//            if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.T)) {
//                logger.debug(Thread.activeCount() + " threads");
//            }
        }


//        ImGui.render();

        try {
            Utils.setStaticFieldValue(InputSystem.class, "touchDownEvent", null);
            Utils.setStaticFieldValue(InputSystem.class, "touchUpEvent", null);
            Utils.setStaticFieldValue(InputSystem.class, "dragEvent", null);
            Utils.setStaticFieldValue(InputSystem.class, "scrollEvent", null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void resize(int width, int height) {
        sceneManager.getScene(0).getSystemManager().getSystem(EditorRenderingSystem.class).getMainViewport().update(width, height, true);
        this.width = width/Utils.PPU;
        this.height = height/Utils.PPU;
        sceneManager.getScene(0).getSystemManager().getSystem(EditorRenderingSystem.class).setScreenHeight(this.height);
        sceneManager.getScene(0).getSystemManager().getSystem(EditorRenderingSystem.class).setScreenWidth(this.width);
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
        sceneManager.dispose();
//        ImGui.dispose();
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


    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }


    @SuppressWarnings("SameParameterValue")
    protected void setEditorMode(boolean editorMode) {
        this.editorMode = editorMode;
    }
    public void setDebugRender(boolean debugRender) {
        this.debugRender = debugRender;
    }


    public final void setApp(Lwjgl3Application app) {
        if (this.app==null)
            this.app = app;
    }

    public final Lwjgl3Application getApp() {
        return app;
    }

    public final boolean isPaused() {
        return paused;
    }
}
