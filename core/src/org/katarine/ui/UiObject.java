package org.katarine.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import org.katarine.components.DrawableComponent;
import org.katarine.editor.Editor;
import org.katarine.Game;
import org.katarine.objects.GameObject;

public class UiObject extends DrawableComponent {
    protected final Vector2 uiPosition = new Vector2();

    public Batch batch;

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
    protected final void draw() {
        update();
    }

    @Override
    public void update() {
        if (Editor.getEditorInstance()!=null || Game.getEditorInstance().isEditorMode()) return;
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
