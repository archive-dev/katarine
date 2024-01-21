package io.github.whoisamyy.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class RigidBody2D extends Component {
    Transform2D transform;

    public Body body;
    public Fixture fixture;

    public RigidBody2D(World world, BodyDef bodyDef, Shape shape, float density) {
        this.body = world.createBody(bodyDef);
        this.fixture = this.body.createFixture(shape, density);
        shape.dispose();
    }

    public RigidBody2D(World world, BodyDef bodyDef, FixtureDef fixtureDef) {
        this.body = world.createBody(bodyDef);
        this.fixture = this.body.createFixture(fixtureDef);
    }

    @Override
    public void awake() {
        try {
            transform = gameObject.getComponent(Transform2D.class);
        } catch (NullPointerException e) {
            transform = gameObject.addComponent(new Transform2D());
        }
    }

    @Override
    public void update() {
        transform.setPosition(body.getWorldCenter());
    }

    @Override
    public void die() {
        super.die();
    }

    public void setPosition(Vector2 position) {
        body.setTransform(position, 0);
        if (transform!=null) transform.setPosition(position);
    }
}
