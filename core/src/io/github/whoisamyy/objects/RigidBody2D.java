package io.github.whoisamyy.objects;

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
public class RigidBody2D extends GameObject {
    protected Transform transform;
    protected Body body;
    protected BodyDef bodyDef;
    protected World world;
    protected Fixture fixture;
    protected FixtureDef fixtureDef;
    protected Shape shape;
    protected float density;

    protected boolean contacting = false;
    protected RigidBody2D previousContact;

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
    public final void init() {
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
        if (isContacting()) {
            whileContacting(getPreviousContact());
        }
        update();
        for (Component c : components) {
            c.update();
        }
    }

    public void onContactStart(RigidBody2D contact) {}
    public void whileContacting(RigidBody2D contact) {}
    public void onContactEnd(RigidBody2D contact) {}

    public void onPreResolve(RigidBody2D contact, Manifold manifold) {};
    public void onPostResolve(RigidBody2D contact, ContactImpulse impulse) {};

    public Transform getTransform() {
        return transform;
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

    public void setContacting(boolean contacting) {
        this.contacting = contacting;
    }

    public void setPreviousContact(RigidBody2D previousContact) {
        this.previousContact = previousContact;
    }

    public boolean isContacting() {
        return contacting;
    }

    public RigidBody2D getPreviousContact() {
        return previousContact;
   }

   public Body getBody() {
        return body;
   }
}
