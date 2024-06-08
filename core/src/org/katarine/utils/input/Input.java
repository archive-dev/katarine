package org.katarine.utils.input;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import org.katarine.utils.Utils;

public class Input extends InputAdapter {
    Vector2 dragPos = new Vector2();
    Vector2 dragDelta = new Vector2();

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        dragPos.set(screenX, screenY);

        MouseClickEvent event = new MouseClickEvent(screenX, screenY, button, true);
        try {
            Utils.setStaticFieldValue(InputSystem.class, "touchDownEvent", event);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        MouseClickEvent event = new MouseClickEvent(screenX, screenY, button, false);
        try {
            Utils.setStaticFieldValue(InputSystem.class, "touchUpEvent", event);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        MouseClickEvent event = new MouseClickEvent(screenX, screenY, dragDelta, true);
        dragDelta = new Vector2(screenX, screenY).sub(dragPos);
        try {
            Utils.setStaticFieldValue(InputSystem.class, "dragEvent", event);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        dragPos.set(screenX, screenY);
        mouseMoved(screenX, screenY);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        try {
            MouseClickEvent e = Utils.getStaticFieldValue(InputSystem.class, "moveEvent");
            if (e==null) {
                e = new MouseClickEvent(screenX, screenY);
                Utils.setStaticFieldValue(InputSystem.class, "moveEvent", e);
                e = Utils.getStaticFieldValue(InputSystem.class, "moveEvent");
            }
            e.setMouseX(screenX);
            e.setMouseY(screenY);
            e.getMousePosition().set(new Vector2(screenX, screenY));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        MouseClickEvent event = new MouseClickEvent(true, amountX, amountY);
        try {
            Utils.setStaticFieldValue(InputSystem.class, "scrollEvent", event);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return false;
    }
}
