package io.github.whoisamyy.ui;

import io.github.whoisamyy.components.Camera2D;
import io.github.whoisamyy.components.Component;
import io.github.whoisamyy.components.DrawableComponent;
import io.github.whoisamyy.editor.Editor;
import io.github.whoisamyy.editor.components.EditorCamera;
import io.github.whoisamyy.editor.components.EditorObjectComponent;
import io.github.whoisamyy.katarine.Game;
import io.github.whoisamyy.logging.LogLevel;
import io.github.whoisamyy.objects.GameObject;
import io.github.whoisamyy.utils.serialization.annotations.SerializeField;

import java.util.HashSet;

public class Canvas extends Component {
    @SerializeField
    protected HashSet<UiObject> uiObjects = new HashSet<>();

    private boolean showUI = true;
    private static Camera2D camera;

    @Override
    public void awake() {
        logger.setLogLevel(LogLevel.DEBUG);
        camera = Editor.getEditorInstance()==null ? Game.getEditorInstance().getCam().getComponentExtender(Camera2D.class)
                : Editor.getEditorInstance().getCam().getComponent(EditorCamera.class);

        gameObject.removeComponent(EditorObjectComponent.class);
//        gameObject.removeComponent(EditorObjectComponent.EditorTriggerBox.class);
    }

    @Override
    public void start() {
        for (GameObject c : gameObject.getChildren()) {
            uiObjects.add(c.getComponentExtender(UiObject.class));
        }
    }

    @Override
    public void update() {
        transform.pos.set(camera.getCamera().position.x, camera.getCamera().position.y);
    }

    public boolean isShowUI() {
        return showUI;
    }

    public void setShowUI(boolean showUI) {
        if (!Editor.getEditorInstance().isEditorMode()) return;
        this.showUI = showUI;
        for (UiObject uiObject : uiObjects) {
            try {
                uiObject.gameObject.getComponentExtenders(DrawableComponent.class).forEach(c -> c.show=showUI);
            } catch (NullPointerException ignored) {}
        }
    }
}
