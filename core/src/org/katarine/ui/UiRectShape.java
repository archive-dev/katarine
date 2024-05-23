package org.katarine.ui;

import com.badlogic.gdx.math.Vector2;
import org.katarine.editor.Editor;
import org.katarine.utils.render.shapes.RectShape;
import org.katarine.ui.imgui.HideInInspector;

public class UiRectShape extends UiObject {
    public final Vector2 rectSize = new Vector2(1, 1);

    UiRect uiRect;

    @HideInInspector
    public static class UiRect extends RectShape {
        Vector2 pos;

        public UiRect(float w, float h, Vector2 pos) {
            super(w, h);
            this.pos = pos;
        }

        @Override
        public void draw() {
            super.draw();
            this.x = pos.x;
            this.y = pos.y;
        }
    }

    static boolean isEditor;

    static {
        isEditor = Editor.getEditorInstance()!=null;
    }

    @Override
    public void start() {
        uiRect = new UiRect(rectSize.x, rectSize.y, uiPosition);
    }
}
