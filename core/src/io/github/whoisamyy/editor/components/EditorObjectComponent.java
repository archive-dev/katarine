package io.github.whoisamyy.editor.components;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import io.github.whoisamyy.components.Component;
import io.github.whoisamyy.components.Sprite;
import io.github.whoisamyy.components.Text;
import io.github.whoisamyy.components.Transform2D;
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

    private Vector2 rectPos = new Vector2();
    ObjectRect rect;
    private class ObjectRect extends RectShape {
        private Transform2D worldPos;
        private Vector2 screenPos = new Vector2();
        private Vector2 relativeWorldPos = new Vector2();

        public ObjectRect(float x, float y, Transform2D worldPos) {
            super(x, y, Color.CYAN);
            this.worldPos = worldPos;
        }

        @Override
        public void draw() {
            MouseClickEvent drag = AbstractInputHandler.getDragEvent();
            MouseClickEvent clickEvent = AbstractInputHandler.getTouchDownEvent();

            if (selected && drag!=null && InputHandler.isButtonPressed(Input.Buttons.LEFT)) {
                gameObject.relativePosition.add(drag.getDragDelta().cpy().scl(ec.getCamera().zoom));
            }

            if (clickEvent!=null) {
                //new Logger().setLogLevel(LogLevel.DEBUG).debug(drag + " " + clickEvent);
                Vector2 mousePos = clickEvent.getMouseScreenPos().scl(1/Utils.PPU);

                // p1.x < x < p2.x
                // p1.y < y < p3.y

                if (mousePos.x > getVector2Points()[0].x && mousePos.x < getVector2Points()[1].x &&
                        mousePos.y > getVector2Points()[0].y && mousePos.y < getVector2Points()[2].y
                ) {
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


            Vector3 v3 = Editor.instance.getCamera().project(new Vector3(new Vector2(worldPos.pos.x+relativeWorldPos.x, worldPos.pos.y+relativeWorldPos.y), 0));
            this.screenPos.set(v3.x/Utils.PPU, v3.y/Utils.PPU);

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
        MouseClickEvent drag = AbstractInputHandler.getDragEvent();

//        if (selection.size() > 1 && drag!=null)
//            gameObject.relativePosition.add(drag.getDragDelta().scl(ec.getCamera().zoom));

        if (areKeysPressed(Input.Keys.SHIFT_LEFT, Input.Keys.D)) {
            selected = false;
            selection.clear();
        }
        if (isKeyJustPressed(Input.Keys.D)) {
            logger.debug(selection);
        }
    }
}
