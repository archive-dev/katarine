package org.katarine.ui;

import org.katarine.components.Camera2D;
import org.katarine.components.Component;
import org.katarine.components.DrawableComponent;
import org.katarine.editor.Editor;
import org.katarine.editor.components.EditorCamera;
import org.katarine.editor.components.EditorObjectComponent;
import org.katarine.Game;
import org.katarine.logging.LogLevel;
import org.katarine.objects.GameObject;
import org.katarine.ui.imgui.SerializeField;

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
