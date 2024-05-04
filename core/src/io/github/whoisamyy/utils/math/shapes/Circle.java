package io.github.whoisamyy.utils.math.shapes;

import com.badlogic.gdx.math.Vector2;

public class Circle extends Shape {
    public float r = 1;

    public Circle(float x, float y, float r) {
        super(x, y);
        this.r = r;
    }

    public Circle() {
        super(0, 0);
    }

    public Circle(float x, float y) {
        super(x, y);
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
