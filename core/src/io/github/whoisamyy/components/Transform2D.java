package io.github.whoisamyy.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Transform;

public class Transform2D extends Component {
    public Vector2 pos = new Vector2();
    private float posX;
    private float posY;

    @Override
    public void start() {
        //rb = gameObject.getComponent(RigidBody2D.class);
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
