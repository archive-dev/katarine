package io.github.whoisamyy.utils.render.shapes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.github.whoisamyy.editor.Editor;
import io.github.whoisamyy.katarine.Game;
import io.github.whoisamyy.utils.Utils;

public class RectShape extends RenderableShape {
    protected float w, h, scaleX = 1, scaleY = 1;

    protected Color color;

    public RectShape() {
        this(1, 1);
    }

    /**
     *
     * @param w width
     * @param h height
     */
    public RectShape(float w, float h) {
        super();
        this.w = w;
        this.h = h;
        this.color = Color.WHITE;
    }

    @Override
    public boolean isPointInShape(float x, float y) {
        return isPointInRect(x, y);
    }

    @Override
    public boolean isPointInShape(Vector2 point) {
        return isPointInRect(point);
    }

    public RectShape(float width, float height, Color color) {
        this(width, height);
        this.color = color;
    }

    @Override
    public void draw() {
        scaleX = transform.scale.x;
        scaleY = transform.scale.y;
        shapeDrawer.rectangle((x-w/2*scaleX), (y-h/2*scaleY), w*scaleX, h*scaleY, color);
    }

    public final float[] getVertices() {
        return new float[] {x-w/2*scaleX, y-h/2*scaleY,
                x+w/2*scaleX, y-h/2*scaleY,
                x+w/2*scaleX, y+h/2*scaleY,
                x-w/2*scaleX, y+h/2*scaleY};
    }

    public final Vector2[] getVector2Points() {
        return Utils.getVector2Points(getVertices());
    }

    public final float[][] getPoints() {
        return Utils.getPoints(getVertices());
    }

    public final float getW() {
        return w;
    }

    public final void setW(float w) {
        this.w = w;
    }

    public final float getH() {
        return h;
    }

    public final void setH(float h) {
        this.h = h;
    }

    public final float getScaleX() {
        return scaleX;
    }

    public final void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public final float getScaleY() {
        return scaleY;
    }

    public final void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public final Color getColor() {
        return color;
    }

    public final void setColor(Color color) {
        this.color = color;
    }

    public boolean isPointInRect(float x, float y) {
        return x > getVector2Points()[0].x && x < getVector2Points()[1].x &&
                y > getVector2Points()[0].y && y < getVector2Points()[2].y;
    }

    public boolean isPointInRect(Vector2 point) {
        return isPointInRect(point.x, point.y);
    }
}
