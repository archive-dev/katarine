package io.github.whoisamyy.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import io.github.whoisamyy.components.Camera2D;
import io.github.whoisamyy.components.RigidBody2D;
import io.github.whoisamyy.components.SpriteComponent;
import io.github.whoisamyy.components.Transform2D;
import io.github.whoisamyy.editor.Editor;
import io.github.whoisamyy.logging.LogLevel;
import io.github.whoisamyy.logging.Logger;
import io.github.whoisamyy.objects.GameObject;
import io.github.whoisamyy.test.components.CameraController;
import io.github.whoisamyy.test.objects.AtomLi;
import io.github.whoisamyy.utils.Utils;

public class Game extends Editor {
    public static Game instance;

    public static final float angstrom = 1;

    boolean a = true;

    public Game(int width, int height) {
        super(width, height);
        if (instance==null)
            instance = this;
    }

    @Override
    public void create() {
        setEditorMode(false);
        try {
            Utils.setStaticFieldValue(GameObject.class, "game", instance);
            Utils.setStaticFieldValue(GameObject.class, "editor", null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);

        }
        setDebugRender(false);
        super.create();
        world.setGravity(new Vector2(0, 0));

        cam = GameObject.instantiate(GameObject.class);
        cam.addComponent(new Transform2D());
        cam.addComponent(new Camera2D(this.getWidth(), this.getHeight(), batch));

        for (GameObject go : getGameObjects()) {
            go.create();
        }
        a = true;
    }

    @Override
    public void render() {
        super.render();
        if (Gdx.input.isKeyJustPressed(Input.Keys.R) && a) {
            a = false;
            create();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
