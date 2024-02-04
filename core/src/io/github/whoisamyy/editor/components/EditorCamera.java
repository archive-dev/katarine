package io.github.whoisamyy.editor.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import io.github.whoisamyy.components.Camera2D;
import io.github.whoisamyy.components.TriggerBox;
import io.github.whoisamyy.logging.Logger;
import io.github.whoisamyy.utils.EditorObject;
import io.github.whoisamyy.utils.Utils;
import io.github.whoisamyy.utils.input.MouseClickEvent;

import java.util.Objects;

@EditorObject
public class EditorCamera extends Camera2D {
    private MouseClickEvent mouseDragEvent;
    private MouseClickEvent mouseScrollEvent;
    private MouseClickEvent mouseClickEvent;

    public boolean move = true;

    private Logger logger = new Logger();

    public EditorCamera(float width, float height, SpriteBatch batch) {
        super(width, height, batch);
    }

    @Override
    public void start() {

    }

    @Override
    public void update() {
        if (mouseDragEvent!=null && Objects.equals(mouseDragEvent.isDrag(), true) && Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            getCamera().translate(mouseDragEvent.getDragDelta().scl(-getCamera().zoom));

            logger.debug(getTransform().pos.toString());
        }
        if (mouseScrollEvent!=null && Objects.equals(mouseScrollEvent.isScroll(), true)) {
            getCamera().zoom += 0.2f*mouseScrollEvent.getScrollAmountY();

            //fixing floating point issue
            float scale = (float) Math.pow(10, 1);
            float result = (float) (Math.ceil(getCamera().zoom * scale) / scale);
            getCamera().zoom = result;

            getCamera().zoom = Utils.clamp(getCamera().zoom, 20f, 1f);
            logger.debug(String.valueOf(getCamera().zoom));
        }

        onKeyJustPressed(Input.Keys.R, ()-> {
            getCamera().position.set(0,0,0);
            getCamera().zoom = 1;
        });

        super.update();

        mouseDragEvent = getMouseDragEvent();
        mouseScrollEvent = getMouseScrollEvent();
    }
}
