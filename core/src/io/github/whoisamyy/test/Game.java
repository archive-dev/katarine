package io.github.whoisamyy.test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import io.github.whoisamyy.components.SpriteComponent;
import io.github.whoisamyy.listeners.CollisionsListener;
import io.github.whoisamyy.objects.GameObject;
import io.github.whoisamyy.objects.RigidBody2D;
import io.github.whoisamyy.test.components.ExampleComponent;
import io.github.whoisamyy.test.objects.ExampleGameObject;

import java.util.ArrayList;
import java.util.List;

public class Game extends ApplicationAdapter {
	SpriteBatch batch;
	OrthographicCamera camera;
	World world;
	Box2DDebugRenderer renderer;

	private static List<GameObject> gameObjects = new ArrayList<>();

	private final int width, height;

	public Game(int windowWidth, int windowHeight) {
		this.width = windowWidth;
		this.height = windowHeight;
	}

	@Override
	public void create () {
		batch = new SpriteBatch();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, width, height);

		//Box2D.init();
		world = new World(new Vector2(0, -100), true);
		world.setContactListener(new CollisionsListener());
		renderer = new Box2DDebugRenderer();

		// vars init
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(50, 250);

		CircleShape circle = new CircleShape();
		circle.setRadius(32f);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 0.5f;
		fixtureDef.friction = 0f;
		fixtureDef.restitution = 0.6f;


		circle.dispose();
		//

		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.type = BodyDef.BodyType.StaticBody;
		groundBodyDef.position.set(0, 0);

		PolygonShape groundBox = new PolygonShape();
		//groundBox.setAsBox(camera.viewportWidth, 1f);
		groundBox.set(new Vector2[]{new Vector2(), new Vector2(0, 200),
				new Vector2(width, 0)
		});

		//

		BodyDef exampleDef = new BodyDef();
		exampleDef.type = BodyDef.BodyType.DynamicBody;
		exampleDef.position.set(200, 250);

		PolygonShape shape = new PolygonShape();
		shape.set(new Vector2[]{new Vector2(), new Vector2(0, 200),
								new Vector2(200, 200), new Vector2(200, 0)});

		FixtureDef exampleFixtureDef = new FixtureDef();
		exampleFixtureDef.shape = shape;
		exampleFixtureDef.density = 5f;
		exampleFixtureDef.friction = 0.3f;
		exampleFixtureDef.restitution = 0;


		// creating bodies
		ExampleGameObject circleBody = GameObject.instantiate(ExampleGameObject.class, world, bodyDef, fixtureDef);
		ExampleGameObject groundBody = GameObject.instantiate(ExampleGameObject.class, world, groundBodyDef, groundBox);
		ExampleGameObject exampleBody = GameObject.instantiate(ExampleGameObject.class, world, exampleDef, exampleFixtureDef);
		circleBody.addComponent(new ExampleComponent());
		circleBody.addComponent(new SpriteComponent(batch, new Texture(Gdx.files.internal("bucket.png"))));

		gameObjects.add(circleBody);
		gameObjects.add(groundBody);
		gameObjects.add(exampleBody);

		for (GameObject go : gameObjects) {
			go.init();
		}
		System.out.println("Initialized gameObjects: ");
		for (GameObject go : gameObjects) {
			System.out.println("\t"+go.getClass().getTypeName());
		}
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0.2f, 1);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for (GameObject go : gameObjects) {
			go.render();
		}
		batch.end();
		renderer.render(world, camera.combined);
		world.step(1/240f, 6, 2);
	}
	
	@Override
	public void dispose () {
		for (GameObject go : gameObjects) {
			go.dispose();
		}
		batch.dispose();
	}

	public static void addGameObject(GameObject gameObject) {
		gameObjects.add(gameObject);
	}

	public static List<GameObject> getGameObjects() {
		return gameObjects;
	}

	public static RigidBody2D getRigidBody2DByFixture(Fixture fixture) {
		for (GameObject go : getGameObjects()) {
			if (go instanceof RigidBody2D) {
				RigidBody2D rb = ((RigidBody2D) go);

				if (rb.getFixture().equals(fixture)) return rb;
			}
		}
		return null;
	}

	//public static boolean
}
