package io.github.whoisamyy.ui;

import com.badlogic.gdx.math.Vector2;
import io.github.whoisamyy.editor.Editor;
import io.github.whoisamyy.katarine.Game;
import io.github.whoisamyy.utils.render.shapes.RectShape;

public class UiRectShape extends UiObject {
    public final Vector2 rectSize = new Vector2(1, 1);

    UiRect uiRect;
    private static class UiRect extends RectShape {
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

    @Override
    public void start() {
        uiRect = new UiRect(rectSize.x, rectSize.y, uiPosition);

        if (Editor.getInstance()!=null) Editor.getInstance().getShapes().add(uiRect);
        else Game.getInstance().getShapes().add(uiRect);
    }
}
