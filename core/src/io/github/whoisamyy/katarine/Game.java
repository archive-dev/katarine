package io.github.whoisamyy.katarine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import io.github.whoisamyy.components.Camera2D;
import io.github.whoisamyy.components.Sprite;
import io.github.whoisamyy.components.Transform2D;
import io.github.whoisamyy.editor.Editor;
import io.github.whoisamyy.objects.GameObject;
import io.github.whoisamyy.utils.Utils;

public class Game extends Editor {
    public static Game gameInstance;

    public Game() {
        this(1280, 720);
    }

    public Game(int width, int height) {
        super(width, height);
        if (gameInstance == null)
            gameInstance = this;
    }

    @Override
    public void create() {
        setEditorMode(false);

        try {
            Utils.setStaticFieldValue(GameObject.class, "game", gameInstance);
            Utils.setStaticFieldValue(GameObject.class, "editor", null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        setDebugRender(false);
        super.preCreate();

        cam = GameObject.instantiate(GameObject.class);
        cam.addComponent(new Transform2D());
        cam.addComponent(new Camera2D(this.getWidth(), this.getHeight(), batch, uiBatch));

        super.create();

        GameObject.instantiate(GameObject.class).addComponent(new Sprite(new Texture(Gdx.files.internal("bucket.png")), 1, 1));

        world.setGravity(new Vector2(0, 0));

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
