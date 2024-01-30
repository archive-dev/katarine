package io.github.whoisamyy.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Transform;

public class Transform2D extends Component {
    public Vector2 pos = null;
    private float posX;
    private float posY;

    public Transform2D() {}

    public Transform2D(Vector2 pos) {
        this.pos = pos;
        posX = pos.x;
        posY = pos.y;
    }

    @Override
    public void awake() {
        if (pos == null) pos = new Vector2();
        posX = pos.x;
        posY = pos.y;
    }

    @Override
    public void update() {
        posX=pos.x;
        posY=pos.y;
    }

    public void setPosition(Vector2 pos) {
        this.pos = pos;
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }
}
