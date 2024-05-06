package org.katarine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.katarine.components.Camera2D;
import org.katarine.components.Sprite;
import org.katarine.components.Transform2D;
import org.katarine.editor.Editor;
import org.katarine.objects.GameObject;
import org.katarine.utils.Utils;

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
