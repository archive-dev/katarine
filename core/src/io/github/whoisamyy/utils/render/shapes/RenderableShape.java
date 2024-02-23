package io.github.whoisamyy.utils.render.shapes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.whoisamyy.editor.Editor;
import io.github.whoisamyy.katarine.Game;
import io.github.whoisamyy.utils.math.shapes.Shape;

public abstract class RenderableShape extends Shape {
    public RenderableShape() {
        this(0, 0);
    }

    public RenderableShape(float x, float y) {
        super(x, y);
        try {
            Game.getInstance().getShapes().add(this);
            shapeRenderer = Game.getInstance().getShapeRenderer();
        } catch (NullPointerException e) {
            Editor.getInstance().getShapes().add(this);
            shapeRenderer = Editor.getInstance().getShapeRenderer();
        }
    }

    public int blendSourceFactor = GL20.GL_SRC_ALPHA;
    public int blendDestinationFactor = GL20.GL_ONE_MINUS_SRC_ALPHA;
    public int blendFunc = GL20.GL_FUNC_ADD;

    protected ShapeRenderer shapeRenderer;

    protected abstract void draw();

    public void render() {
//        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(blendSourceFactor, blendDestinationFactor);
        Gdx.gl.glBlendEquation(blendFunc);
        draw();
//        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    public void setShapeRenderer(ShapeRenderer shapeRenderer) {
        this.shapeRenderer = shapeRenderer;
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
