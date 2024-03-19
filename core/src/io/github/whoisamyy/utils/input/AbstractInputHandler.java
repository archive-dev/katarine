package io.github.whoisamyy.utils.input;

import com.badlogic.gdx.Gdx;
import io.github.whoisamyy.core.KObject;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractInputHandler extends KObject {
    static MouseClickEvent touchDownEvent;
    static MouseClickEvent touchUpEvent;
    static MouseClickEvent dragEvent;
    @NotNull
    static MouseClickEvent moveEvent = new MouseClickEvent(0, 0);
    static MouseClickEvent scrollEvent;

    protected final boolean isKeyPressed(int keycode) {
        return Gdx.input.isKeyPressed(keycode);
    }

    protected final boolean areKeysPressed(int... keycodes) {
        int[] preKeys;
        System.arraycopy(keycodes, 0, preKeys = new int[keycodes.length-1], 0, keycodes.length-1);
        for (int k : preKeys) {
            if (!isKeyPressed(k)) {
                return false;
            }
        }
        return isKeyJustPressed(keycodes[keycodes.length - 1]);
    }

    protected final boolean isKeyJustPressed(int keycode) {
        return Gdx.input.isKeyJustPressed(keycode);
    }

    protected final void onKeyPressed(int keycode, Action action) {
        if (isKeyPressed(keycode)) action.execute();
    }

    protected final void onKeyJustPressed(int keycode, Action action) {
        if (isKeyJustPressed(keycode)) action.execute();
    }

    protected final boolean isButtonPressed(int button) {
        return Gdx.input.isButtonPressed(button);
    }

    protected final boolean isButtonJustPressed(int button) {
        return Gdx.input.isButtonJustPressed(button);
    }

    protected final MouseClickEvent getMouseClickEvent() {
        return touchDownEvent;
    }

    protected final MouseClickEvent getMouseUnClickEvent() {
        return touchUpEvent;
    }

    protected final MouseClickEvent getMouseDragEvent() {
        return dragEvent;
    }
    protected final MouseClickEvent getMouseMoveEvent() {
        return moveEvent;
    }

    protected final MouseClickEvent getMouseScrollEvent() {
        return scrollEvent;
    }


    // static versions
    public static MouseClickEvent getTouchDownEvent() {
        return touchDownEvent;
    }

    public static MouseClickEvent getTouchUpEvent() {
        return touchUpEvent;
    }

    public static MouseClickEvent getDragEvent() {
        return dragEvent;
    }

    @NotNull
    public static MouseClickEvent getMoveEvent() {
        return moveEvent;
    }

    public static MouseClickEvent getScrollEvent() {
        return scrollEvent;
    }

    public static class InputHandler {
        public static boolean isKeyPressed(int keycode) {
            return Gdx.input.isKeyPressed(keycode);
        }

        public static boolean areKeysPressed(int... keycodes) {
            int[] preKeys;
            System.arraycopy(keycodes, 0, preKeys = new int[keycodes.length-1], 0, keycodes.length-1);
            for (int k : preKeys) {
                if (!isKeyPressed(k)) {
                    return false;
                }
            }
            return isKeyJustPressed(keycodes[keycodes.length - 1]);
        }

        public static boolean isKeyJustPressed(int keycode) {
            return Gdx.input.isKeyJustPressed(keycode);
        }

        public static void onKeyPressed(int keycode, Action action) {
            if (isKeyPressed(keycode)) action.execute();
        }

        public static void onKeyJustPressed(int keycode, Action action) {
            if (isKeyJustPressed(keycode)) action.execute();
        }

        public static boolean isButtonPressed(int button) {
            return Gdx.input.isButtonPressed(button);
        }

        public static boolean isButtonJustPressed(int button) {
            return Gdx.input.isButtonJustPressed(button);
        }
    }
}
