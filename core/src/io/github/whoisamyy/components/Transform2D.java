package io.github.whoisamyy.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Transform;

public class Transform2D extends Component {
    public Vector2 pos = null;

    public Transform2D() {}

    public Transform2D(Vector2 pos) {
        this.pos = pos;
    }

    @Override
    public void awake() {
        if (pos == null) pos = new Vector2();
    }

    public void setPosition(Vector2 pos) {
        this.pos = pos;
    }

    public float getPosX() {
        return pos.x;
    }

    public float getPosY() {
        return pos.y;
    }
}
