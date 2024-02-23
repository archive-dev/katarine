package io.github.whoisamyy.utils.math.shapes;

import com.badlogic.gdx.math.Vector2;
import io.github.whoisamyy.editor.Editor;
import io.github.whoisamyy.katarine.Game;
import io.github.whoisamyy.utils.Utils;

public class Rect extends Shape {
    public float w, h, scaleX = 1, scaleY = 1;

    public Rect(float x, float y, float w, float h) {
        super(x, y);
        this.w = w;
        this.h = h;
    }

    public Rect(float w, float h) {
        this(0, 0, w, h);
    }

    @Override
    public boolean isPointInShape(float x, float y) {
        return isPointInRect(x, y);
    }

    @Override
    public boolean isPointInShape(Vector2 point) {
        return isPointInRect(point);
    }

    public final float[] getVertices() {
        float scrh;
        if (Editor.getInstance()==null)
            scrh = Game.getInstance().getHeight();
        else
            scrh = Editor.getInstance().getHeight();

        return new float[] {x-w/2*scaleX, scrh-y-h/2*scaleY,
                x+w/2*scaleX, scrh-y-h/2*scaleY,
                x+w/2*scaleX, scrh-y+h/2*scaleY,
                x-w/2*scaleX, scrh-y+h/2*scaleY};
    }

    public final Vector2[] getVector2Points() {
        return Utils.getVector2Points(getVertices());
    }

    public final float[][] getPoints() {
        return Utils.getPoints(getVertices());
    }

    public boolean isPointInRect(float x, float y) {
        return x > getVector2Points()[0].x && x < getVector2Points()[1].x &&
                y > getVector2Points()[0].y && y < getVector2Points()[2].y;
    }

    public boolean isPointInRect(Vector2 point) {
        return isPointInRect(point.x, point.y);
    }
}
