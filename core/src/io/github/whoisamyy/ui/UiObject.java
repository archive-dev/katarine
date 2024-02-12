package io.github.whoisamyy.ui;

import com.badlogic.gdx.math.Vector2;
import io.github.whoisamyy.components.Component;
import io.github.whoisamyy.editor.Editor;
import io.github.whoisamyy.katarine.Game;
import io.github.whoisamyy.objects.GameObject;

public class UiObject extends Component {
    protected final Vector2 uiPosition = new Vector2();

    protected Canvas canvas;

    @Override
    public void start() {
        if (canvas!=null) return;
        GameObject go = gameObject;
        while (go.getParent()!=null) {
            try {
                canvas = go.getComponent(Canvas.class);
                break;
            } catch (NullPointerException ignored) {}
            go = go.getParent();
        }
    }

    @Override
    public void update() {
        if (Editor.getInstance()!=null || Game.getInstance().isEditorMode()) return;
        gameObject.relativePosition.set(uiPosition);
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public Vector2 getUiPosition() {
        return uiPosition;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }
}
