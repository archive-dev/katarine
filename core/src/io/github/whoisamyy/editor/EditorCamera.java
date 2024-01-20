package io.github.whoisamyy.editor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import io.github.whoisamyy.components.Camera2D;
import io.github.whoisamyy.logging.LogLevel;
import io.github.whoisamyy.logging.Logger;
import io.github.whoisamyy.utils.Utils;
import io.github.whoisamyy.utils.input.MouseClickEvent;

import java.util.Objects;

public class EditorCamera extends Camera2D {
    private MouseClickEvent dragEvent;
    private MouseClickEvent scrollEvent;
    private Vector2 pos;

    private Logger logger = new Logger().setLogLevel(LogLevel.DEBUG);

    public EditorCamera(float width, float height, SpriteBatch batch) {
        super(width, height, batch);
    }

    @Override
    public void update() {
        if (dragEvent!=null && Objects.equals(dragEvent.isDrag(), true)) {
            getTransform2D().pos.sub(dragEvent.getDragDelta().scl(getCamera().zoom));
            logger.debug(dragEvent.getDragDelta().toString());
        }
        if (scrollEvent!=null && Objects.equals(scrollEvent.isScroll(), true)) {
            getCamera().zoom += scrollEvent.getScrollAmountY();
            getCamera().zoom = Utils.clamp(getCamera().zoom, 20f, 1f);
            logger.debug(String.valueOf(getCamera().zoom));
        }


        super.update();

        dragEvent = getMouseDragEvent();
        scrollEvent = getMouseScrollEvent();
    }
}
