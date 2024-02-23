package io.github.whoisamyy.utils.render.shapes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import io.github.whoisamyy.editor.Editor;
import io.github.whoisamyy.katarine.Game;

public class CircleShape extends RenderableShape {
    public float r;

    protected Color color = Color.WHITE;

    public CircleShape(float r, Color color) {
        this.r = r;
        this.color = color;
    }

    public CircleShape(float x, float y, float r, Color color) {
        super(x, y);
        this.r = r;
        this.color = color;
    }

    public CircleShape(float r) {
        this(0, 0, r);
    }

    public CircleShape(float x, float y, float r) {
        super(x, y);
        this.r = r;
    }

    @Override
    protected void draw() {
        shapeRenderer.setColor(color);
        float offsetX, offsetY;
        if (Editor.getInstance() != null) {
            offsetX = Editor.getInstance().getWidth() / 2;
            offsetY = Editor.getInstance().getHeight() / 2;
        } else {
            offsetX = Game.getInstance().getWidth()/2;
            offsetY = Game.getInstance().getHeight()/2;
        }

        shapeRenderer.circle(x+offsetX, y+offsetY, r, 128);
        shapeRenderer.setColor(Color.WHITE);
    }

    @Override
    public boolean isPointInShape(float x, float y) {
        return (x - this.x) * (x - this.x) + (y - this.y) * (y - this.y) <= r * r;
    }

    @Override
    public boolean isPointInShape(Vector2 point) {
        return isPointInShape(point.x, point.y);
    }
}
