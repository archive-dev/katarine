package io.github.whoisamyy.test;

import com.badlogic.gdx.math.Vector2;
import io.github.whoisamyy.components.Camera2D;
import io.github.whoisamyy.components.Transform2D;
import io.github.whoisamyy.editor.Editor;
import io.github.whoisamyy.objects.GameObject;
import io.github.whoisamyy.utils.Utils;

public class Game extends Editor {
    public static Game instance;

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
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
