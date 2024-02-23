package io.github.whoisamyy.utils.math.shapes;

import com.badlogic.gdx.math.Vector2;

public abstract class Shape {
    public float x = 0, y = 0;

    public Shape(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public abstract boolean isPointInShape(float x, float y);
    public abstract boolean isPointInShape(Vector2 point);
}
