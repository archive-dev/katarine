package io.github.whoisamyy.utils.input;

import com.badlogic.gdx.Gdx;

public abstract class AbstractInputHandler {
    private static MouseClickEvent touchDownEvent;
    private static MouseClickEvent touchUpEvent;
    private static MouseClickEvent dragEvent;
    private static MouseClickEvent moveEvent = new MouseClickEvent(0, 0);
    private static MouseClickEvent scrollEvent;

    protected final boolean isKeyPressed(int keycode) {
        return Gdx.input.isKeyPressed(keycode);
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
        MouseClickEvent event = touchDownEvent;
        touchDownEvent = null;
        return event;
    }

    protected final MouseClickEvent getMouseUnClickEvent() {
        MouseClickEvent event = touchUpEvent;
        touchUpEvent = null;
        return event;
    }

    protected final MouseClickEvent getMouseDragEvent() {
        MouseClickEvent event = dragEvent;
        dragEvent = null;
        return event;
    }

    protected final MouseClickEvent getMouseMoveEvent() {
        MouseClickEvent event = moveEvent;
        moveEvent = null;
        return event;
    }

    protected final MouseClickEvent getMouseScrollEvent() {
        MouseClickEvent event = scrollEvent;
        scrollEvent = null;
        return event;
    }
}
