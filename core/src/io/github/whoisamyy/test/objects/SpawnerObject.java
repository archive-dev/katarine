package io.github.whoisamyy.test.objects;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import io.github.whoisamyy.objects.GameObject;
import io.github.whoisamyy.objects.RigidBody2D;
import io.github.whoisamyy.test.Game;

public class SpawnerObject extends GameObject {
    private CircleShape circle;
    private FixtureDef fixtureDef;
    private World world;

    public SpawnerObject(){
        super();
    }

    public SpawnerObject(World world) {
        this.world = world;
    }

    @Override
    protected void update() {
        onMouseClick(event -> {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            bodyDef.position.set(event.getMouseX(), Game.getHeight()-event.getMouseY());

            System.out.println(event.getMousePosition());

            circle = new CircleShape();
            circle.setRadius(10f);

            fixtureDef = new FixtureDef();
            fixtureDef.shape = circle;
            fixtureDef.density = 1f;
            fixtureDef.friction =0.5f;
            fixtureDef.restitution = 0.5f;


            circle.dispose();

            System.out.println(circle);
            System.out.println(fixtureDef);
            System.out.println(world);

            RigidBody2D rb = instantiate(RigidBody2D.class, world, bodyDef, fixtureDef);
        });
    }
}
