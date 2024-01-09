package io.github.whoisamyy.utils.input;

import com.badlogic.gdx.InputAdapter;
import io.github.whoisamyy.utils.Utils;

public class Input extends InputAdapter {
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        MouseClickEvent event = new MouseClickEvent(screenX, screenY, button, true);
        try {
            Utils.setStaticFieldValue(AbstractInputHandler.class, "touchDownEvent", event);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        MouseClickEvent event = new MouseClickEvent(screenX, screenY, button, false);
        try {
            Utils.setStaticFieldValue(AbstractInputHandler.class, "touchUpEvent", event);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        MouseClickEvent event = new MouseClickEvent(screenX, screenY, true);
        try {
            Utils.setStaticFieldValue(AbstractInputHandler.class, "dragEvent", event);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        MouseClickEvent event = new MouseClickEvent(screenX, screenY);
        try {
            Utils.setStaticFieldValue(AbstractInputHandler.class, "moveEvent", event);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        MouseClickEvent event = new MouseClickEvent(true, amountX, amountY);
        try {
            Utils.setStaticFieldValue(AbstractInputHandler.class, "scrollEvent", event);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return false;
    }
}
