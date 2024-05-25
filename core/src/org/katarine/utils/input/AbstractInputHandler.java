package org.katarine.utils.input;

import com.badlogic.gdx.Gdx;
import org.katarine.core.KObject;
import org.katarine.ui.imgui.ImGui;
import org.katarine.utils.serialization.annotations.DontSerialize;

import javax.annotation.Nonnull;

//@DontSerialize
public abstract class AbstractInputHandler implements KObject {
    @DontSerialize
    private static MouseClickEvent touchDownEvent;
    @DontSerialize
    private static MouseClickEvent touchUpEvent;
    @DontSerialize
    private static MouseClickEvent dragEvent;

    @Nonnull
    @DontSerialize
    private static MouseClickEvent moveEvent = new MouseClickEvent(0, 0);
    @DontSerialize
    private static MouseClickEvent scrollEvent;

    protected final boolean isKeyPressed(int keycode) {
        if (ImGui.controlsInput) return false;
        return Gdx.input.isKeyPressed(keycode);
    }

    protected final boolean areKeysPressed(int... keycodes) {
        if (ImGui.controlsInput) return false;
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
        if (ImGui.controlsInput) return false;
        return Gdx.input.isKeyJustPressed(keycode);
    }

    protected final void onKeyPressed(int keycode, Action action) {
        if (ImGui.controlsInput) return;
        if (isKeyPressed(keycode)) action.execute();
    }

    protected final void onKeyJustPressed(int keycode, Action action) {
        if (ImGui.controlsInput) return;
        if (isKeyJustPressed(keycode)) action.execute();
    }

    protected final boolean isButtonPressed(int button) {
        if (ImGui.controlsInput) return false;
        return Gdx.input.isButtonPressed(button);
    }

    protected final boolean isButtonJustPressed(int button) {
        if (ImGui.controlsInput) return false;
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

    @Nonnull
    public static MouseClickEvent getMoveEvent() {
        return moveEvent;
    }

    public static MouseClickEvent getScrollEvent() {
        return scrollEvent;
    }

    public static final class InputHandler {
        private InputHandler(){}

        public static boolean isKeyPressed(int keycode) {
            if (ImGui.controlsInput) return false;
            return Gdx.input.isKeyPressed(keycode);
        }

        public static boolean areKeysPressed(int... keycodes) {
            if (ImGui.controlsInput) return false;
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
            if (ImGui.controlsInput) return false;
            return Gdx.input.isKeyJustPressed(keycode);
        }

        public static void onKeyPressed(int keycode, Action action) {
            if (ImGui.controlsInput) return;
            if (isKeyPressed(keycode)) action.execute();
        }

        public static void onKeyJustPressed(int keycode, Action action) {
            if (ImGui.controlsInput) return;
            if (isKeyJustPressed(keycode)) action.execute();
        }

        public static boolean isButtonPressed(int button) {
            if (ImGui.controlsInput) return false;
            return Gdx.input.isButtonPressed(button);
        }

        public static boolean isButtonJustPressed(int button) {
            if (ImGui.controlsInput) return false;
            return Gdx.input.isButtonJustPressed(button);
        }
    }
}
