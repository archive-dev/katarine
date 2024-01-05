package io.github.whoisamyy.objects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import io.github.whoisamyy.components.Component;
import io.github.whoisamyy.components.Transform2D;

import java.util.Objects;

//you can use this as example for creating javadocs for your GameObject classes
/**
 * Wrapper for {@link Body} class.
 * Available params constructor params for {@link GameObject#instantiate(Class, Object...)} invocation are: <br>
 * <pre>{@code World, BodyDef}</pre>
 * <pre>{@code World, BodyDef, Vector2}</pre>
 * <pre>{@code World, BodyDef, FixtureDef}</pre>
 * <pre>{@code World, BodyDef, FixtureDef, Vector2}</pre>
 * <pre>{@code World, BodyDef, Shape, float}</pre>
 */
public abstract class RigidBody2D extends GameObject {
    private Transform transform;
    private Body body;
    private BodyDef bodyDef;
    private World world;
    private Fixture fixture;
    private FixtureDef fixtureDef;
    private Shape shape;
    private float density;

    public RigidBody2D(){
        super();
    }

    public RigidBody2D(World world, BodyDef def) {
        this.bodyDef = def;
        this.world = world;
        this.body = this.world.createBody(this.bodyDef);
    }

    public RigidBody2D(World world, BodyDef def, Vector2 position) {
        this.bodyDef = def;
        this.world = world;
        this.body = this.world.createBody(this.bodyDef);
        if (!Objects.equals(position, new Vector2()) &&
                Objects.equals(def.position, new Vector2())) {
            this.body.getPosition().set(position);
        }
    }

    public RigidBody2D(World world, BodyDef def, FixtureDef fixtureDef) {
        this.bodyDef = def;
        this.world = world;
        this.fixtureDef = fixtureDef;
        this.body = this.world.createBody(this.bodyDef);
        this.fixture = this.body.createFixture(this.fixtureDef);
    }

    public RigidBody2D(World world, BodyDef def, Shape shape, Float density) {
        this.bodyDef = def;
        this.world = world;
        this.shape = shape;
        this.density = density;
        this.body = this.world.createBody(this.bodyDef);
        this.fixture = this.body.createFixture(this.shape, this.density);
        this.shape.dispose();
    }

    public RigidBody2D(World world, BodyDef def, Shape shape) {
        this.bodyDef = def;
        this.world = world;
        this.shape = shape;
        this.density = 1f;
        this.body = this.world.createBody(this.bodyDef);
        this.fixture = this.body.createFixture(this.shape, this.density);
        this.shape.dispose();
    }

    public RigidBody2D(World world, BodyDef def, FixtureDef fixtureDef, Vector2 position) {
        this.bodyDef = def;
        this.world = world;
        this.fixtureDef = fixtureDef;
        this.body = this.world.createBody(this.bodyDef);
        this.fixture = this.body.createFixture(this.fixtureDef);
        if (!Objects.equals(position, new Vector2()) &&
                Objects.equals(def.position, new Vector2())) {
            this.body.getPosition().set(position);
        }
    }

    @Override
    public void init() {
        if (initialized) return;
        addComponent(new Transform2D());
        this.body.setUserData(getId());
        start();
        for (Component c : components) {
            c.start();
            //System.out.println("Initialized component "+c.getClass().getName());
        }
        //System.out.println("Initialized GameObject"+this.getClass().getName());
        initialized = true;
    }

    @Override
    public final void render() {
        getComponent(Transform2D.class).setTransform(getBody().getWorldCenter());
        update();
        for (Component c : components) {
            c.update();
        }
    }

    public abstract void onContactStart(RigidBody2D contact);
    public abstract void onContactEnd(RigidBody2D contact);

    public abstract void onPreResolve(RigidBody2D contact, Manifold manifold);
    public abstract void onPostResolve(RigidBody2D contact, ContactImpulse impulse);

    public Transform getTransform() {
        return transform;
    }

    public Body getBody() {
        return body;
    }

    public BodyDef getBodyDef() {
        return bodyDef;
    }

    public World getWorld() {
        return world;
    }

    public Fixture getFixture() {
        return fixture;
    }

    public FixtureDef getFixtureDef() {
        return fixtureDef;
    }

    public Shape getShape() {
        return shape;
    }

    public float getDensity() {
        return density;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public void setBodyDef(BodyDef bodyDef) {
        this.bodyDef = bodyDef;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void setFixture(Fixture fixture) {
        this.fixture = fixture;
    }

    public void setFixtureDef(FixtureDef fixtureDef) {
        this.fixtureDef = fixtureDef;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public void setDensity(float density) {
        this.density = density;
    }
}
