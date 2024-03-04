package io.github.whoisamyy.components.phyiscs;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import io.github.whoisamyy.components.Component;

import java.util.HashSet;
import java.util.Set;

public class RigidBody2D extends Component {
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

    public RigidBody2D(RigidBody2DBuilder builder) {
        this.world = builder.getWorld();

        BodyDef bodyDef = new BodyDef();
        bodyDef.bullet = builder.isBullet();
        bodyDef.type = builder.getType();
        bodyDef.gravityScale = builder.getGravityScale();
        bodyDef.position.set(builder.getPosition());
        bodyDef.linearVelocity.set(builder.getLinearVelocity());
        bodyDef.active = builder.isActive();
        bodyDef.angle = builder.getAngle();
        bodyDef.allowSleep = builder.isAllowSleep();
        bodyDef.angularDamping = builder.getAngularDamping();
        bodyDef.linearDamping = builder.getLinearDamping();
        bodyDef.fixedRotation = builder.isFixedRotation();
        bodyDef.awake = builder.isAwake();

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = builder.getShape();
        fixtureDef.isSensor = builder.isSensor();
        fixtureDef.density = builder.getDensity();
        fixtureDef.friction = builder.getFriction();
        fixtureDef.restitution = builder.getRestitution();

        this.fixture = (this.body = this.world.createBody(bodyDef)).createFixture(fixtureDef);
    }

    @Override
    public void update() {
        transform.setPosition(body.getWorldCenter());
        this.fixture.setUserData(this);
    }

    @Override
    public void die() {
        world.destroyBody(body);
    }

    public void setPosition(Vector2 position) {
        body.setTransform(position, 0);
        if (transform!=null) transform.setPosition(position);
    }

    @FunctionalInterface
    public interface CollisionEnterHandler {
        void onCollisionEnter(RigidBody2D other);
    }

    @FunctionalInterface
    public interface CollisionExitHandler {
        void onCollisionExit(RigidBody2D other);
    }

    @FunctionalInterface
    public interface CollisionPreSolveHandler {
        void onCollisionPreSolve(RigidBody2D other, Manifold manifold);
    }

    @FunctionalInterface
    public interface CollisionPostSolveHandler {
        void onCollisionPreSolve(RigidBody2D other, ContactImpulse impulse);
    }

    Set<CollisionExitHandler> collisionExitHandlers = new HashSet<>();
    Set<CollisionEnterHandler> collisionEnterHandlers = new HashSet<>();
    Set<CollisionPreSolveHandler> collisionPreSolveHandlers = new HashSet<>();
    Set<CollisionPostSolveHandler> collisionPostSolveHandlers = new HashSet<>();

    public void addCollisionPostSolveHandler(CollisionPostSolveHandler handler) {
        collisionPostSolveHandlers.add(handler);
    }
    public void addCollisionPreSolveHandler(CollisionPreSolveHandler handler) {
        collisionPreSolveHandlers.add(handler);
    }

    public void removeCollisionPostSolveHandler(CollisionPostSolveHandler handler) {
        collisionPostSolveHandlers.remove(handler);
    }
    public void removeCollisionPreSolveHandler(CollisionPreSolveHandler handler) {
        collisionPreSolveHandlers.remove(handler);
    }

    public void addCollisionEnterHandler(CollisionEnterHandler handler) {
        collisionEnterHandlers.add(handler);
    }

    public void removeCollisionEnterHandler(CollisionEnterHandler handler) {
        collisionEnterHandlers.remove(handler);
    }

    public void addCollisionExitHandler(CollisionExitHandler handler) {
        collisionExitHandlers.add(handler);
    }

    public void removeCollisionExitHandler(CollisionExitHandler handler) {
        collisionExitHandlers.remove(handler);
    }

    public void onCollisionPreSolve(RigidBody2D other, Manifold manifold) {
        collisionPreSolveHandlers.forEach(handler -> handler.onCollisionPreSolve(other, manifold));
    }

    public void onCollisionPostSolve(RigidBody2D other, ContactImpulse impulse) {
        collisionPostSolveHandlers.forEach(handler -> handler.onCollisionPreSolve(other, impulse));
    }

    public void onCollisionExit(RigidBody2D other) {
        collisionExitHandlers.forEach(handler -> handler.onCollisionExit(other));
    }

    public void onCollisionEnter(RigidBody2D other) {
        collisionEnterHandlers.forEach(handler -> handler.onCollisionEnter(other));
    }

    public static class RigidBody2DBuilder {
        private RigidBody2D body;

        Shape shape;
        World world;
        FixtureDef fixtureDef;
        Fixture fixture;
        float density = 1f;
        float mass = 1f;
        float friction = 0.2f;
        float restitution = 0.6f;
        boolean isSensor = false;

        public BodyDef.BodyType type = BodyDef.BodyType.StaticBody;
        public final Vector2 position = new Vector2();
        public float angle = 0;
        public final Vector2 linearVelocity = new Vector2();
        public float angularVelocity = 0;
        public float linearDamping = 0;
        public float angularDamping = 0;
        public boolean allowSleep = true;
        public boolean awake = true;
        public boolean fixedRotation = false;
        public boolean bullet = false;
        public boolean active = true;
        public float gravityScale = 1;

        public BodyDef.BodyType getType() {
            return type;
        }

        public RigidBody2DBuilder setType(BodyDef.BodyType type) {
            this.type = type;
            return this;
        }

        public Vector2 getPosition() {
            return position;
        }

        public float getAngle() {
            return angle;
        }

        public RigidBody2DBuilder setAngle(float angle) {
            this.angle = angle;
            return this;
        }

        public Vector2 getLinearVelocity() {
            return linearVelocity;
        }

        public float getAngularVelocity() {
            return angularVelocity;
        }

        public RigidBody2DBuilder setAngularVelocity(float angularVelocity) {
            this.angularVelocity = angularVelocity;
            return this;
        }

        public float getLinearDamping() {
            return linearDamping;
        }

        public RigidBody2DBuilder setLinearDamping(float linearDamping) {
            this.linearDamping = linearDamping;
            return this;
        }

        public float getAngularDamping() {
            return angularDamping;
        }

        public RigidBody2DBuilder setAngularDamping(float angularDamping) {
            this.angularDamping = angularDamping;
            return this;
        }

        public boolean isAllowSleep() {
            return allowSleep;
        }

        public RigidBody2DBuilder setAllowSleep(boolean allowSleep) {
            this.allowSleep = allowSleep;
            return this;
        }

        public boolean isAwake() {
            return awake;
        }

        public RigidBody2DBuilder setAwake(boolean awake) {
            this.awake = awake;
            return this;
        }

        public boolean isFixedRotation() {
            return fixedRotation;
        }

        public RigidBody2DBuilder setFixedRotation(boolean fixedRotation) {
            this.fixedRotation = fixedRotation;
            return this;
        }

        public boolean isBullet() {
            return bullet;
        }

        public RigidBody2DBuilder setBullet(boolean bullet) {
            this.bullet = bullet;
            return this;
        }

        public boolean isActive() {
            return active;
        }

        public RigidBody2DBuilder setActive(boolean active) {
            this.active = active;
            return this;
        }

        public float getGravityScale() {
            return gravityScale;
        }

        public RigidBody2DBuilder setGravityScale(float gravityScale) {
            this.gravityScale = gravityScale;
            return this;
        }

        public RigidBody2D getBody() {
            return body;
        }

        public Shape getShape() {
            return shape;
        }

        public RigidBody2DBuilder setShape(Shape shape) {
            this.shape = shape;
            return this;
        }

        public World getWorld() {
            return world;
        }

        public RigidBody2DBuilder setWorld(World world) {
            this.world = world;
            return this;
        }

        public FixtureDef getFixtureDef() {
            return fixtureDef;
        }

        public RigidBody2DBuilder setFixtureDef(FixtureDef fixtureDef) {
            this.fixtureDef = fixtureDef;
            return this;
        }

        public Fixture getFixture() {
            return fixture;
        }

        public RigidBody2DBuilder setFixture(Fixture fixture) {
            this.fixture = fixture;
            return this;
        }

        public float getDensity() {
            return density;
        }

        public RigidBody2DBuilder setDensity(float density) {
            this.density = density;
            return this;
        }

        public float getMass() {
            return mass;
        }

        public RigidBody2DBuilder setMass(float mass) {
            this.mass = mass;
            return this;
        }

        public float getFriction() {
            return friction;
        }

        public RigidBody2DBuilder setFriction(float friction) {
            this.friction = friction;
            return this;
        }

        public float getRestitution() {
            return restitution;
        }

        public RigidBody2DBuilder setRestitution(float restitution) {
            this.restitution = restitution;
            return this;
        }

        public boolean isSensor() {
            return isSensor;
        }

        public RigidBody2DBuilder setSensor(boolean sensor) {
            isSensor = sensor;
            return this;
        }

        public RigidBody2D build() {
            body = new RigidBody2D(this);
            return body;
        }
    }
}
