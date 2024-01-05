package io.github.whoisamyy.test.components;

import com.badlogic.gdx.math.Vector2;
import io.github.whoisamyy.components.Component;
import io.github.whoisamyy.objects.GameObject;
import io.github.whoisamyy.objects.RigidBody2D;

public class ExampleComponent extends Component {
    public RigidBody2D rb;

    @Override
    public void start() {
        rb = ((RigidBody2D) gameObject);
    }

    @Override
    public void update() {
        //((RigidBody2D) gameObject).getBody().applyForceToCenter(new Vector2(10f, 0), true);
        rb.getBody().applyForceToCenter(10000000, 0, true);
    }

    @Override
    public void die() {

    }
}
