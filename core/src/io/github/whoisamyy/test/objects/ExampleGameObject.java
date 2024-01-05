package io.github.whoisamyy.test.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import io.github.whoisamyy.objects.RigidBody2D;

public class ExampleGameObject extends RigidBody2D {
    public ExampleGameObject(){
        super();
    }

    public ExampleGameObject(World world, BodyDef def) {
        super(world, def);
    }

    public ExampleGameObject(World world, BodyDef def, Vector2 position) {
        super(world, def, position);
    }

    public ExampleGameObject(World world, BodyDef def, FixtureDef fixtureDef) {
        super(world, def, fixtureDef);
    }

    public ExampleGameObject(World world, BodyDef def, Shape shape, Float density) {
        super(world, def, shape, density);
    }

    public ExampleGameObject(World world, BodyDef def, Shape shape) {
        super(world, def, shape);
    }

    public ExampleGameObject(World world, BodyDef def, FixtureDef fixtureDef, Vector2 position) {
        super(world, def, fixtureDef, position);
    }

    @Override
    public void onContactStart(RigidBody2D contact) {
        System.out.println("contacted with "+contact.getId());
    }

    @Override
    public void onContactEnd(RigidBody2D contact) {
        System.out.println("ended contact with "+contact.getId());
    }

    @Override
    public void onPreResolve(RigidBody2D contact, Manifold manifold) {
        System.out.println("pre resolved contact with "+contact.getId());
    }

    @Override
    public void onPostResolve(RigidBody2D contact, ContactImpulse impulse) {
        System.out.println("post resolved contact with "+contact.getId());
    }

    @Override
    protected void start() {

    }

    @Override
    protected void update() {

    }

    @Override
    protected void die() {

    }
}
