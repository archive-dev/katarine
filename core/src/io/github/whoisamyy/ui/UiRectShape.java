package io.github.whoisamyy.ui;

import com.badlogic.gdx.math.Vector2;
import io.github.whoisamyy.utils.render.shapes.RectShape;

public class UiRectShape extends UiObject {
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
    public void awake() {
        uiRect = new UiRect(1, 1, uiPosition);
    }
}
