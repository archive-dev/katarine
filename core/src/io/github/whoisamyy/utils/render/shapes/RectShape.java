package io.github.whoisamyy.utils.render.shapes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.github.whoisamyy.editor.Editor;
import io.github.whoisamyy.katarine.Game;
import io.github.whoisamyy.utils.Utils;

public class RectShape extends RenderableShape {
    protected float w, h, scaleX = 1, scaleY = 1;

    protected Color color1, color2, color3, color4;

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
        this.color1 = Color.WHITE;
        this.color2 = color1;
        this.color3 = color2;
        this.color4 = color3;
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
        this.color1 = color;
        this.color2 = color1;
        this.color3 = color2;
        this.color4 = color3;
    }

    /**
     * Constructs a filled rectangle shape with color gradient (bottom to top) of color1 and color2
     * @param w width
     * @param h height
     * @param color1 color 1
     * @param color2 color 2
     */
    public RectShape(float w, float h, Color color1, Color color2) {
        this(w, h);
        this.color1 = color1;
        this.color2 = color1;
        this.color3 = color2;
        this.color4 = color2;
    }

    public RectShape(float width, float height, Color color1, Color color2, Color color3, Color color4) {
        this(width, height);
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.color4 = color4;
    }

    @Override
    public void draw() {
        getShapeRenderer().set(ShapeRenderer.ShapeType.Line);
        float offsetX, offsetY;
        if (Editor.getInstance() != null) {
            offsetX = Editor.getInstance().getWidth() / 2;
            offsetY = Editor.getInstance().getHeight() / 2;
        } else {
            offsetX = Game.getInstance().getWidth()/2;
            offsetY = Game.getInstance().getHeight()/2;
        }

        shapeRenderer.rect((x-w/2*scaleX)+offsetX, (y-h/2*scaleY)+offsetY, w*scaleX, h*scaleY, color1, color2, color3, color4); // wtf how does it even work
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

    public final Color getColor1() {
        return color1;
    }

    public final void setColor1(Color color1) {
        this.color1 = color1;
    }

    public final Color getColor2() {
        return color2;
    }

    public final void setColor2(Color color2) {
        this.color2 = color2;
    }

    public final Color getColor3() {
        return color3;
    }

    public final void setColor3(Color color3) {
        this.color3 = color3;
    }

    public final Color getColor4() {
        return color4;
    }

    public final void setColor4(Color color4) {
        this.color4 = color4;
    }

    public boolean isPointInRect(float x, float y) {
        return x > getVector2Points()[0].x && x < getVector2Points()[1].x &&
                y > getVector2Points()[0].y && y < getVector2Points()[2].y;
    }

    public boolean isPointInRect(Vector2 point) {
        return isPointInRect(point.x, point.y);
    }
}
