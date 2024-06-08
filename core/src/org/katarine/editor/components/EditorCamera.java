package org.katarine.editor.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import org.katarine.components.Camera2D;
import org.katarine.annotations.EditorObject;
import org.katarine.utils.Utils;
import org.katarine.utils.input.InputSystem;
import org.katarine.utils.input.MouseClickEvent;

import java.util.Objects;

import static org.katarine.utils.input.InputHandler.onKeyJustPressed;

@EditorObject
public class EditorCamera extends Camera2D {
    private MouseClickEvent mouseDragEvent;
    private MouseClickEvent mouseScrollEvent;
    private MouseClickEvent mouseClickEvent;

    public boolean move = true;

    public EditorCamera() {}

    @Override
    public void update() {
        if (mouseDragEvent!=null && Objects.equals(mouseDragEvent.isDrag(), true) && Gdx.input.isButtonPressed(Input.Buttons.MIDDLE)) {
            getCamera().translate(mouseDragEvent.getDragDelta().cpy().scl(-getCamera().zoom));

            logger.debug(getTransform().pos.toString());
        }
        if (mouseScrollEvent!=null && Objects.equals(mouseScrollEvent.isScroll(), true)) {
            getCamera().zoom += 0.1f*mouseScrollEvent.getScrollAmountY() * getCamera().zoom;

            //fixing floating point issue
            float scale = (float) Math.pow(10, 2);
            getCamera().zoom = (float) (Math.ceil(getCamera().zoom * scale) / scale);

            getCamera().zoom = Utils.clamp(getCamera().zoom, 50f, 0.1f);
            logger.debug(String.valueOf(getCamera().zoom));
        }

        onKeyJustPressed(Input.Keys.R, ()-> {
            logger.debug(getCamera().zoom);
            getTransform().pos.set(0, 0);
            getCamera().position.set(0,0,0);
            getCamera().zoom = 1;
        });

        super.update();

        mouseDragEvent = InputSystem.getDragEvent();
        mouseScrollEvent = InputSystem.getMouseScrollEvent();
    }
}
