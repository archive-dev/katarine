package io.github.whoisamyy.editor.components;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import io.github.whoisamyy.components.*;
import io.github.whoisamyy.editor.Editor;
import io.github.whoisamyy.logging.LogLevel;
import io.github.whoisamyy.objects.GameObject;
import io.github.whoisamyy.utils.Utils;
import io.github.whoisamyy.utils.input.AbstractInputHandler;
import io.github.whoisamyy.utils.input.MouseClickEvent;
import io.github.whoisamyy.utils.render.shapes.RectShape;
import io.github.whoisamyy.utils.serialization.annotations.HideInInspector;

import java.util.HashSet;

@HideInInspector
public class EditorObjectComponent extends Component {
    public static HashSet<GameObject> selection = new HashSet<>(16);
    private static EditorCamera ec;

    private final Vector2 rectPos = new Vector2();
    ObjectRect rect;
    public class ObjectRect extends RectShape {
        private final Transform2D worldPos;
        private final Vector2 screenPos = new Vector2();
        private final Vector2 relativeWorldPos = new Vector2();

        Vector2 deltaMove = new Vector2();

        public ObjectRect(float x, float y, Transform2D worldPos) {
            super(x, y, Color.CYAN);
            this.worldPos = worldPos;
        }

        @Override
        public void draw() {
            MouseClickEvent drag = AbstractInputHandler.getDragEvent();
            MouseClickEvent clickEvent = AbstractInputHandler.getTouchDownEvent();

            if (selected && InputHandler.areKeysPressed(Input.Keys.ALT_LEFT, Input.Keys.S)) {
                gameObject.relativePosition.x = Math.round(gameObject.relativePosition.x);
                gameObject.relativePosition.y = Math.round(gameObject.relativePosition.y);
            }

            if (selected && drag!=null && InputHandler.isButtonPressed(Input.Buttons.LEFT)) {
                deltaMove.add(drag.getDragDelta().cpy().scl(ec.getZoom()));
                if (InputHandler.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                    if (deltaMove.x >= 0.25f/ec.getZoom() || deltaMove.x <= -0.25f/ec.getZoom()) {
                        gameObject.relativePosition.x+= (deltaMove.x > 0 ? 1 : -1) * 0.25f;
                        deltaMove.x = 0;
                    }
                    if (deltaMove.y >= 0.25f/ec.getZoom() || deltaMove.y <= -0.25f/ec.getZoom()) {
                        gameObject.relativePosition.y+=(deltaMove.y > 0 ? 1 : -1) * 0.25f;
                        deltaMove.y = 0;
                    }
                } else
                    gameObject.relativePosition.add(drag.getDragDelta().cpy().scl(ec.getCamera().zoom));
            }

            if (clickEvent!=null) {
                //new Logger().setLogLevel(LogLevel.DEBUG).debug(drag + " " + clickEvent);
                Vector2 mousePos = clickEvent.getMouseScreenPos().scl(1/Utils.PPU);

                // p1.x < x < p2.x
                // p1.y < y < p3.y

                if (isPointInRect(mousePos)) {
                    canMoveAny = true;
                    selection.add(gameObject);
                    selected = true;
                    setColor1(Color.GREEN);
                    setColor2(Color.GREEN);
                    setColor3(Color.GREEN);
                    setColor4(Color.GREEN);
                } else if (!InputHandler.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                    selection.remove(gameObject);
                    selected = false;
                    setColor1(Color.CYAN);
                    setColor2(Color.CYAN);
                    setColor3(Color.CYAN);
                    setColor4(Color.CYAN);
                } else {
                    if (selection.size()==1) {
                        selection.remove(gameObject);
                        selected = false;
                        setColor1(Color.CYAN);
                        setColor2(Color.CYAN);
                        setColor3(Color.CYAN);
                        setColor4(Color.CYAN);
                    }
                }
            }

            Vector2 v = Camera2D.worldToScreen(worldPos.pos.cpy().add(relativeWorldPos), ec);

            this.screenPos.set(v.scl(1/Utils.PPU));

            setScaleX(1/ec.getZoom());
            setScaleY(1/ec.getZoom());

            x = screenPos.x;
            y = screenPos.y;

            super.draw();
        }
    }

    static boolean canMoveAny = false;
    boolean selected = false;

    @Override
    public void awake() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.35f, 0.35f);

        rectPos.set(transform.pos.x, transform.pos.y);

        logger.setLogLevel(LogLevel.DEBUG);
    }

    @Override
    public void start() {
        if (Editor.getInstance()!=null) {
            ec = Editor.getInstance().getCam().getComponent(EditorCamera.class);
        }

        ec = Editor.getInstance().getCam().getComponent(EditorCamera.class);

//        rect = new ObjectRect(1, 1);
        try {
            Sprite s = gameObject.getComponentExtender(Sprite.class);
            rect = new ObjectRect(s.getSpriteWidth(), s.getSpriteHeight(), transform);
            rect.setX(rectPos.x);
            rect.setY(rectPos.y);
        } catch (NullPointerException e) {
            try {
                Text text = gameObject.getComponentExtender(Text.class);
                rect = new ObjectRect(text.getTextWidth(), text.getTextHeight(), transform);
                rect.relativeWorldPos.add(text.getTextWidth()/2, -text.getTextHeight()/2);
            } catch (NullPointerException ex) {
                rect = new ObjectRect(1, 1, transform);
            }
        }
    }

    @Override
    public void update() {
        if (areKeysPressed(Input.Keys.SHIFT_LEFT, Input.Keys.D)) {
            selected = false;
            selection.clear();
        }
        if (isKeyJustPressed(Input.Keys.D)) {
            logger.debug(selection);
        }
    }
}
