package io.github.whoisamyy.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class RigidBody2D extends Component {
    protected TriggerBox triggerBox;

    public Body body;
    public Fixture fixture;
    private World world;

    public RigidBody2D(World world, BodyDef bodyDef, Shape shape, float density) {
        this.body = world.createBody(bodyDef);
        this.fixture = this.body.createFixture(shape, density);
        this.world = world;
        shape.dispose();
    }

    public RigidBody2D(World world, BodyDef bodyDef, FixtureDef fixtureDef) {
        this.body = world.createBody(bodyDef);
        this.fixture = this.body.createFixture(fixtureDef);
        this.world = world;
    }

    @Override
    public void awake() {
        try {
            transform = gameObject.getComponent(Transform2D.class);
        } catch (NullPointerException e) {
            transform = gameObject.addComponent(new Transform2D());
        }

        try {
            triggerBox = gameObject.getComponent(TriggerBox.class);
        } catch (NullPointerException e) {
            triggerBox = gameObject.addComponent(new TriggerBox(fixture.getShape()));
        }
    }

    @Override
    public void update() {
        transform.setPosition(body.getWorldCenter());
    }

    @Override
    public void die() {
        world.destroyBody(body);
    }

    public void setPosition(Vector2 position) {
        body.setTransform(position, 0);
        if (transform!=null) transform.setPosition(position);
    }


}
