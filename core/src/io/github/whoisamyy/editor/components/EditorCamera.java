package io.github.whoisamyy.editor.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.whoisamyy.components.Camera2D;
import io.github.whoisamyy.katarine.annotations.EditorObject;
import io.github.whoisamyy.utils.Utils;
import io.github.whoisamyy.utils.input.MouseClickEvent;

import java.util.Objects;

@EditorObject
public class EditorCamera extends Camera2D {
    private MouseClickEvent mouseDragEvent;
    private MouseClickEvent mouseScrollEvent;
    private MouseClickEvent mouseClickEvent;

    public boolean move = true;

    public EditorCamera(float width, float height, SpriteBatch batch, SpriteBatch uiBatch) {
        super(width, height, batch, uiBatch);
    }

    @Override
    public void update() {
        if (mouseDragEvent!=null && Objects.equals(mouseDragEvent.isDrag(), true) && Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
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
            transform.pos.set(0, 0);
            getCamera().position.set(0,0,0);
            getCamera().zoom = 1;
        });

        super.update();

        mouseDragEvent = getMouseDragEvent();
        mouseScrollEvent = getMouseScrollEvent();
    }
}
