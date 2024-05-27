package org.katarine.utils.math.shapes;

import com.badlogic.gdx.math.Vector2;
import org.katarine.components.Component;

public abstract class Shape extends Component {
    public float x = 0, y = 0;

    public Shape() {}

    public Shape(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public abstract boolean isPointInShape(float x, float y);
    public abstract boolean isPointInShape(Vector2 point);

    @Override
    public void update() {
        this.x = transform.pos.x;
        this.y = transform.pos.y;
    }
}
