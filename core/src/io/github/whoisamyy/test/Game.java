package io.github.whoisamyy.test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.whoisamyy.components.RigidBody2D;
import io.github.whoisamyy.components.SpriteComponent;
import io.github.whoisamyy.components.Transform2D;
import io.github.whoisamyy.listeners.CollisionsListener;
import io.github.whoisamyy.objects.GameObject;
import io.github.whoisamyy.test.components.CircleController;
import io.github.whoisamyy.utils.Utils;
import io.github.whoisamyy.utils.input.Input;

import java.util.ArrayList;
import java.util.List;

public class Game extends ApplicationAdapter {
	public static Game instance;
	static SpriteBatch batch;
	OrthographicCamera camera;
	World world;
	Box2DDebugRenderer renderer;

	public List<GameObject> gameObjects = new ArrayList<>();

	private int width, height;

	public float getScreenToWorld() {
		return Utils.PPM;
	}

	public Game(int windowWidth, int windowHeight) {
		this.width = (int) (windowWidth / getScreenToWorld());
		this.height = (int) (windowHeight / getScreenToWorld());
		if (instance!=null) return;
		instance = this;
	}

	@Override
	public void create () {
		Gdx.input.setInputProcessor(new Input());

		batch = new SpriteBatch();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, width, height);

		//Box2D.init();
		world = new World(new Vector2(0, -9.8f), false);
		world.setContactListener(new CollisionsListener());
		renderer = new Box2DDebugRenderer();

		BodyDef def = new BodyDef();
		def.bullet=true;
		def.type = BodyDef.BodyType.DynamicBody;

		FixtureDef fdef = new FixtureDef();
		fdef.friction = 0.5f;
		fdef.density = 5f;
		fdef.restitution = 0.5f;

		CircleShape shape = new CircleShape();
		shape.setRadius(0.5f);

		fdef.shape = shape;
		shape.dispose();

		GameObject exm = GameObject.instantiate(GameObject.class);
		exm.addComponent(new Transform2D());
		exm.addComponent(new RigidBody2D(world, def, fdef));
		exm.getComponent(RigidBody2D.class).setPosition(new Vector2(10, 10));
		exm.addComponent(new CircleController());

		exm.addComponent(new SpriteComponent(batch, new Texture(Gdx.files.internal("bucket.png")), 1, 1)); // работает


		for (GameObject go : gameObjects) {
			go.create();
		}
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		camera.update();
		batch.begin();
		batch.setProjectionMatrix(camera.combined);
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

	public void addGameObject(GameObject gameObject) {
		gameObjects.add(gameObject);
	}

	public List<GameObject> getGameObjects() {
		return gameObjects;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	//public static boolean
}
