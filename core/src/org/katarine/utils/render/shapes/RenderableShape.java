package org.katarine.utils.render.shapes;

import org.katarine.rendering.RenderingSystem;
import org.katarine.utils.math.shapes.Shape;
import space.earlygrey.shapedrawer.ShapeDrawer;

public abstract class RenderableShape extends Shape {
    public RenderableShape() {
        this(0, 0);
    }

    public RenderableShape(float x, float y) {
        super(x, y);
    }

    public ShapeDrawer shapeDrawer;

    @Override
    protected void awake() {
        shapeDrawer = getSystemManager().getSystem(RenderingSystem.class).getShapeDrawer();
    }

    protected abstract void draw();

    @Override
    public final void update() {
        super.update();
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
