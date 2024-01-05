package io.github.whoisamyy.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Transform;
import io.github.whoisamyy.objects.RigidBody2D;

public class Transform2D extends Component {
    public Transform transform = new Transform();
    private RigidBody2D rb;

    @Override
    public void start() {
        if (gameObject instanceof RigidBody2D) {
            rb = (RigidBody2D) gameObject;
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void die() {

    }

    public void setTransform(Vector2 pos) {
        this.transform.setPosition(pos);
    }
}
