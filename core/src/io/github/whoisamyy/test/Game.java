package io.github.whoisamyy.test;

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
        super.create();
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
