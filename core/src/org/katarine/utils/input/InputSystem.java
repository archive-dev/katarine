package org.katarine.utils.input;

import com.badlogic.gdx.Gdx;
import org.katarine.annotations.NotInstantiatable;
import org.katarine.editor.imgui.ImGui;
import org.katarine.systems.System;
import org.katarine.utils.serialization.annotations.DontSerialize;

import javax.annotation.Nonnull;

//@DontSerialize
@NotInstantiatable
public final class InputSystem extends System {
    private InputSystem() {}

    @DontSerialize
    private static volatile MouseClickEvent touchDownEvent;
    @DontSerialize
    private static volatile MouseClickEvent touchUpEvent;
    @DontSerialize
    private static volatile MouseClickEvent dragEvent;

    @Nonnull
    @DontSerialize
    private static final MouseClickEvent moveEvent = new MouseClickEvent(0, 0);
    @DontSerialize
    private static volatile MouseClickEvent scrollEvent;

    public static boolean isKeyPressed(int keycode) {
        if (ImGui.controlsInput) return false;
        return Gdx.input.isKeyPressed(keycode);
    }

    public static boolean areKeysPressed(int... keycodes) {
        if (ImGui.controlsInput) return false;
        int[] preKeys;
        java.lang.System.arraycopy(keycodes, 0, preKeys = new int[keycodes.length-1], 0, keycodes.length-1);
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

    public static MouseClickEvent getMouseClickEvent() {
        return touchDownEvent;
    }

    public static MouseClickEvent getMouseUnClickEvent() {
        return touchUpEvent;
    }

    public static MouseClickEvent getMouseDragEvent() {
        return dragEvent;
    }
    public static MouseClickEvent getMouseMoveEvent() {
        return moveEvent;
    }

    public static MouseClickEvent getMouseScrollEvent() {
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

}

