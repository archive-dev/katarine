package org.katarine.components.phyiscs;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import org.katarine.components.Component;

import java.util.HashSet;
import java.util.Set;

public class RigidBody2D extends Component {
    public Body body;
    public Fixture fixture;
    public World world;
    private BodyDef bodyDef;
    private FixtureDef fixtureDef;

    @Override
    protected void start() {
        this.body = this.world.createBody(bodyDef);
        this.fixture = this.body.createFixture(fixtureDef);
    }

    @Override
    public void update() {
        transform.setRelativePosition(body.getWorldCenter());
        this.fixture.setUserData(this);
    }

    @Override
    public void die() {
        world.destroyBody(body);
    }

    public void setPosition(Vector2 position) {
        body.setTransform(position, 0);
        if (transform!=null) transform.setRelativePosition(position);
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
}
