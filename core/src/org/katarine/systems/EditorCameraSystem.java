package org.katarine.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import org.katarine.logging.LogLevel;
import org.katarine.logging.Logger;
import org.katarine.utils.Utils;
import org.katarine.utils.input.InputSystem;
import org.katarine.utils.input.MouseClickEvent;

import java.util.Objects;

public class EditorCameraSystem extends CameraSystem {
    private MouseClickEvent mouseDragEvent, mouseScrollEvent;
    private final Logger logger = new Logger(LogLevel.DEBUG);

    @Override
    protected void update() {
        OrthographicCamera currentEditorCamera = getCurrentCamera();

        if (mouseDragEvent!=null && Objects.equals(mouseDragEvent.isDrag(), true) && Gdx.input.isButtonPressed(Input.Buttons.MIDDLE)) {
            currentEditorCamera.translate(mouseDragEvent.getDragDelta().scl(-currentEditorCamera.zoom));
        }
        if (mouseScrollEvent!=null && Objects.equals(mouseScrollEvent.isScroll(), true)) {
            currentEditorCamera.zoom += 0.1f*mouseScrollEvent.getScrollAmountY() * currentEditorCamera.zoom;

            //fixing floating point issue
            float scale = (float) Math.pow(10, 2);
            currentEditorCamera.zoom = (float) (Math.ceil(currentEditorCamera.zoom * scale) / scale);

            currentEditorCamera.zoom = Utils.clamp(currentEditorCamera.zoom, 50f, 0.1f);
        }

//        currentEditorCamera.position.z = currentEditorCamera.zoom;

        super.update();

        mouseDragEvent = InputSystem.getDragEvent();
        mouseScrollEvent = InputSystem.getMouseScrollEvent();
    }
}
