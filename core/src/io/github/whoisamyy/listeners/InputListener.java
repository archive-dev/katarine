package io.github.whoisamyy.listeners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import io.github.whoisamyy.objects.GameObject;
import io.github.whoisamyy.utils.NotInstantiatable;
import io.github.whoisamyy.utils.input.MouseClickEvent;

import static io.github.whoisamyy.utils.Utils.setStaticFieldValue;

@NotInstantiatable
public class InputListener extends GameObject implements InputProcessor {
    @Override
    public boolean keyDown(int keycode) {
        try {
            setStaticFieldValue(GameObject.class, "pressedKey", keycode);
            setStaticFieldValue(GameObject.class, "unpressedKey", -1);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        try {
            setStaticFieldValue(GameObject.class, "unpressedKey", keycode);
            setStaticFieldValue(GameObject.class, "pressedKey", -1);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        try {
            setStaticFieldValue(GameObject.class, "typedChar", character);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        try {
            setStaticFieldValue(GameObject.class, "pressedButton", button);
            MouseClickEvent event = new MouseClickEvent(screenX, screenY, button, true);
            setStaticFieldValue(GameObject.class, "mouseClickEvent", event);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        try {
            setStaticFieldValue(GameObject.class, "unpressedButton", button);
            MouseClickEvent event = new MouseClickEvent(screenX, screenY, button, false);
            setStaticFieldValue(GameObject.class, "mouseClickEvent", event);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        try {
            MouseClickEvent event = new MouseClickEvent(screenX, screenY, true);
            setStaticFieldValue(GameObject.class, "mouseClickEvent", event);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        try {
            MouseClickEvent event = new MouseClickEvent(screenX, screenY);
            setStaticFieldValue(GameObject.class, "mouseClickEvent", event);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        try {
            MouseClickEvent event = new MouseClickEvent(true, amountX, amountY);
            setStaticFieldValue(GameObject.class, "mouseClickEvent", event);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}
