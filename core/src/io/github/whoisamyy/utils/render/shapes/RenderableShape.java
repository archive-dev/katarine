package io.github.whoisamyy.utils.render.shapes;

import io.github.whoisamyy.editor.Editor;
import io.github.whoisamyy.katarine.Game;
import io.github.whoisamyy.utils.math.shapes.Shape;
import space.earlygrey.shapedrawer.ShapeDrawer;

public abstract class RenderableShape extends Shape {
    public RenderableShape() {
        this(0, 0);
    }

    public RenderableShape(float x, float y) {
        super(x, y);
        try {
            shapeDrawer = Game.getInstance().getShapeDrawer();
        } catch (NullPointerException e) {
            shapeDrawer = Editor.getInstance().getShapeDrawer();
        }
    }

    protected ShapeDrawer shapeDrawer;

    protected abstract void draw();

    public void render() {
        draw();
    }

    public ShapeDrawer getShapeDrawer() {
        return shapeDrawer;
    }

    public void setShapeDrawer(ShapeDrawer shapeDrawer) {
        this.shapeDrawer = shapeDrawer;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
