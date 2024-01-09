package io.github.whoisamyy.test.components;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import io.github.whoisamyy.components.Component;
import io.github.whoisamyy.components.RigidBody2D;
import io.github.whoisamyy.test.Game;
import io.github.whoisamyy.utils.input.MouseClickEvent;

public class CircleController extends Component {

    RigidBody2D rb;

    @Override
    public void awake() {
        if ((rb = gameObject.getComponent(RigidBody2D.class))==null) throw new NullPointerException("no rigidbody((");
    }

    @Override
    public void update() {
        if (rb.body.getTransform().getPosition().y<=0) rb.body.setTransform(new Vector2(rb.body.getTransform().getPosition().x, Game.getHeight()), 0);

        onKeyJustPressed(Input.Keys.LEFT, ()->{
            System.out.println("LEFT");
            rb.body.applyForceToCenter(new Vector2(-10*100, 0), true);
        });

        onKeyJustPressed(Input.Keys.RIGHT, ()->{
            System.out.println("RIGHT");
            rb.body.applyForceToCenter(new Vector2(10*100, 0), true);
        });

        onKeyJustPressed(Input.Keys.UP, ()->{
            System.out.println("UP");
            rb.body.applyForceToCenter(new Vector2(0, 20*100), true);
        });

        MouseClickEvent event = getMouseClickEvent();
        if (event!=null && event.getButton()==Input.Buttons.LEFT) {
            rb.body.setTransform(event.getMousePosition(), 0);
            rb.body.setLinearVelocity(new Vector2());
        }

        MouseClickEvent dragEvent = getMouseDragEvent();
        if (dragEvent!=null) {
            rb.body.setTransform(dragEvent.getMousePosition(), 0);
            System.out.println(rb.body.getLinearVelocity());
        }

        MouseClickEvent scrollEvent = getMouseScrollEvent();
        if (scrollEvent!=null) {
            rb.body.getLinearVelocity().set(new Vector2(0, 20));
        }
    }
}
