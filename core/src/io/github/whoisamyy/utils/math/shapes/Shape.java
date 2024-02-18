package io.github.whoisamyy.utils.math.shapes;

import io.github.whoisamyy.utils.render.shapes.RenderableShape;

public abstract class Shape {
    public float x = 0, y = 0;

    public Shape(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
