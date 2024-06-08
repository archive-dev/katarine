package org.katarine.utils.input;

import com.badlogic.gdx.Gdx;
import org.katarine.editor.imgui.ImGui;

@Deprecated(forRemoval = true)
public final class InputHandler {
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
