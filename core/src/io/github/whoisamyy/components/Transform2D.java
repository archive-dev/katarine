package io.github.whoisamyy.components;

import com.badlogic.gdx.math.Vector2;
import io.github.whoisamyy.utils.serialization.annotations.Range;

public class Transform2D extends Component {
    public final Vector2 pos = new Vector2();
    @Range.FloatRange(min = 0, max = 360)
    public float rotation = 0;
    public final Vector2 scale = new Vector2(1, 1);

    public Transform2D() {}

    public Transform2D(Vector2 pos) {
        this.pos.set(pos);
    }

    public void setRelativePosition(Vector2 pos) {
        this.gameObject.relativePosition.set(pos);
    }

    public float x() {
        return pos.x;
    }

    public float y() {
        return pos.y;
    }
}
