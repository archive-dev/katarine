package org.katarine;

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
