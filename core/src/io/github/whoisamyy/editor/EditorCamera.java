package io.github.whoisamyy.editor;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import io.github.whoisamyy.components.Camera2D;
import io.github.whoisamyy.logging.LogLevel;
import io.github.whoisamyy.logging.Logger;
import io.github.whoisamyy.utils.EditorObject;
import io.github.whoisamyy.utils.Utils;
import io.github.whoisamyy.utils.input.MouseClickEvent;

import java.util.Objects;

@EditorObject
public class EditorCamera extends Camera2D {
    private MouseClickEvent dragEvent;
    private MouseClickEvent scrollEvent;
    private MouseClickEvent clickEvent;
    private Vector2 pos;

    private Logger logger = new Logger().setLogLevel(LogLevel.DEBUG);

    public EditorCamera(float width, float height, SpriteBatch batch) {
        super(width, height, batch);
    }

    @Override
    public void update() {
        if (dragEvent!=null && Objects.equals(dragEvent.isDrag(), true)) {
            getCamera().translate(dragEvent.getDragDelta().scl(-getCamera().zoom));

            logger.debug(getTransform2D().pos.toString());
        }
        if (scrollEvent!=null && Objects.equals(scrollEvent.isScroll(), true)) {
            getCamera().zoom += scrollEvent.getScrollAmountY()/5f;

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

        dragEvent = getMouseDragEvent();
        scrollEvent = getMouseScrollEvent();
    }
}
