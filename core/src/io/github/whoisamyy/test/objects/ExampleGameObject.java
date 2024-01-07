package io.github.whoisamyy.test.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import io.github.whoisamyy.objects.RigidBody2D;

import java.util.Objects;

public class ExampleGameObject extends RigidBody2D {

    //sadly but these constructors are necessary because of reflection issues
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
        //System.out.println("Hi to "+contact.getId() +" from "+getId()); a
    }
}
