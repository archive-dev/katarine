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
            shapeDrawer = Game.getEditorInstance().getShapeDrawer();
        } catch (NullPointerException e) {
            shapeDrawer = Editor.getEditorInstance().getShapeDrawer();
        }
    }

    public RenderableShape(float x, float y, boolean ui) {
        this(x, y);
        if (ui) {
            try {
                shapeDrawer = Game.getEditorInstance().getUiShapeDrawer();
            } catch (NullPointerException e) {
                shapeDrawer = Editor.getEditorInstance().getUiShapeDrawer();
            }
        }
    }

    public ShapeDrawer shapeDrawer;

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
