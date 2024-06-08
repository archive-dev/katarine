package org.katarine.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.katarine.components.Sprite;
import org.katarine.editor.imgui.ImGui;
import org.katarine.editor.systems.EditorSystem;
import org.katarine.logging.LogLevel;
import org.katarine.logging.Logger;
import org.katarine.scenes.Scene;
import org.katarine.scenes.SceneManager;
import org.katarine.systems.EditorCameraSystem;
import org.katarine.utils.input.InputSystem;

final class EditorSceneManager extends SceneManager {
    private final Scene baseScene = new Scene();

    private SpriteBatch spriteBatch;

//    private final Panel inspectorPanel = new Panel("Inspector");
//    private final Panel sceneHierarchy = new Panel("Scene");

    @Override
    protected void awake() {
        addScene(baseScene);
        loadScene(baseScene);

        baseScene.registerSystem(new EditorSystem());
        baseScene.registerSystem(new EditorCameraSystem());
        baseScene.registerSystem(new Grid());
//        baseScene.registerSystem(new ImGuiSystem());
        System.out.println(baseScene.getSystemManager().getSystems());
        spriteBatch = baseScene.registerSystem(new EditorRenderingSystem()).getSpriteBatch();

        SceneHierarchy sceneHierarchy = new SceneHierarchy(baseScene);
        ImGui.addPanel(sceneHierarchy.generatePanel());

        super.awake();
    }

    @Override
    protected void start() {
        Sprite sprite = new Sprite();
        sprite.setBatch(spriteBatch);
        sprite.setTexture(new Texture(Gdx.files.internal("assets/bucket.png")));

        baseScene.createGameObject().addComponent(sprite);

        super.start();

        ImGui.init();

        logger.setLogLevel(LogLevel.DEBUG).debug(baseScene.getGameObjectTree());
    }

    private final Logger logger = new Logger();

    @Override
    protected synchronized void update() {
        if (InputSystem.isKeyJustPressed(Input.Keys.F)) {

            logger.setLogLevel(LogLevel.DEBUG).debug("F pressed");

//            baseScene.getEntityManager().getGameObjectTree().forEach(go -> {
//                if (go == null) return;
//                logger.setLogLevel(LogLevel.DEBUG).debug(go.getName());
//                logger.setLogLevel(LogLevel.DEBUG).debug(go.getComponents());
//                logger.setLogLevel(LogLevel.DEBUG).debug(go.transform.pos);
//            });
        }
        super.update();

        ImGui.render();
    }
}
