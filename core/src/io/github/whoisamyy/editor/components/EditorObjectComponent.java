package io.github.whoisamyy.editor.components;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import io.github.whoisamyy.components.Component;
import io.github.whoisamyy.components.Resizable;
import io.github.whoisamyy.components.Transform2D;
import io.github.whoisamyy.editor.Editor;
import io.github.whoisamyy.katarine.annotations.EditorObject;
import io.github.whoisamyy.katarine.annotations.NotInstantiatable;
import io.github.whoisamyy.logging.LogLevel;
import io.github.whoisamyy.objects.GameObject;
import io.github.whoisamyy.utils.Utils;
import io.github.whoisamyy.utils.input.AbstractInputHandler;
import io.github.whoisamyy.utils.input.MouseClickEvent;
import io.github.whoisamyy.utils.render.RectOwner;
import io.github.whoisamyy.utils.render.shapes.RectShape;
import io.github.whoisamyy.utils.serialization.annotations.HideInInspector;

import java.util.HashSet;
import java.util.Objects;

@HideInInspector
public class EditorObjectComponent extends Component {
    public static HashSet<GameObject> selection = new HashSet<>(16);
    private static EditorCamera ec;
    public boolean canMove = true;
    boolean selected = false;
    public boolean resizable = false;
    public boolean movable = true;
    public boolean selectable = true;
    public static boolean selectionNN = true;
    private final Vector2 rectPos = new Vector2();
    ObjectRect rect;
    @EditorObject
    @NotInstantiatable
    public class ObjectRect extends RectShape { //TODO: ничего не делать
        private final Transform2D worldPos;
        private final Vector2 screenPos = new Vector2();
        private final Vector2 relativeWorldPos = new Vector2();

        private static final Color GREEN = Color.GREEN.cpy().add(0, 0, 0, -0.3f);
        private static final Color CYAN = Color.CYAN.cpy().add(0, 0, 0, -0.3f);
        private static final Color RED = Color.RED.cpy().add(0, 0, 0, -0.3f);

        int currentEdge = -1;
        boolean resizing = false;

        float startDistance = 1;
        Vector2 startScale = new Vector2(1, 1);

        Vector2 deltaMove = new Vector2();
        private float lineWidth = 0.25f;

        public ObjectRect(float x, float y, Transform2D worldPos) {
            super(x, y, CYAN);
            this.worldPos = worldPos;
        }

        @Override
        public void draw() {
            lineWidth = transform.scale.len()/10;
            lineWidth = Utils.clamp(lineWidth, 0.2f, 0.05f);

            MouseClickEvent drag = AbstractInputHandler.getDragEvent();
            MouseClickEvent moveEvent = AbstractInputHandler.getMoveEvent();
            MouseClickEvent clickEvent = AbstractInputHandler.getTouchDownEvent();

            if (selected) {
                setColor(GREEN);
            } else {
                setColor(CYAN);
            }
            if (!canMove) {
                setColor(RED);
            }

            if (selected && InputHandler.areKeysPressed(Input.Keys.ALT_LEFT, Input.Keys.S)) {
                gameObject.relativePosition.x = Math.round(gameObject.relativePosition.x);
                gameObject.relativePosition.y = Math.round(gameObject.relativePosition.y);
            }

            if (selected && drag!=null && moveEvent!=null && InputHandler.isButtonPressed(Input.Buttons.LEFT) &&
                    !InputHandler.isButtonPressed(Input.Buttons.RIGHT) && canMove &&
                    !isPointOnEdge(moveEvent.getMousePosition()) && movable) {

                deltaMove.sub(drag.getDragDelta().cpy().scl(ec.getZoom()));
                if (InputHandler.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                    if (deltaMove.x >= 0.25f/ec.getZoom() || deltaMove.x <= -0.25f/ec.getZoom()) {
                        gameObject.relativePosition.x-= (deltaMove.x > 0 ? 1 : -1) * 0.25f;
                        deltaMove.x = 0;
                    }
                    if (deltaMove.y >= 0.25f/ec.getZoom() || deltaMove.y <= -0.25f/ec.getZoom()) {
                        gameObject.relativePosition.y-=(deltaMove.y > 0 ? 1 : -1) * 0.25f;
                        deltaMove.y = 0;
                    }
                } else
                    gameObject.relativePosition.add(drag.getDragDelta().cpy().scl(ec.getCamera().zoom));
            }
            if (selected && (drag!=null || moveEvent!=null) && InputHandler.isButtonPressed(Input.Buttons.LEFT) && canMove && isPointOnEdge(moveEvent.getMousePosition())) {
                currentEdge = getEdgeOfPoint(Objects.requireNonNullElse(drag, moveEvent).getMousePosition());
                if (!resizing) {
                    resizing = true;
                    startDistance = transform.pos.dst(moveEvent.getMousePosition());
                    startScale = transform.scale.cpy();
                }
            } else {
                currentEdge = -1;
                resizing = false;
            }

            if (resizing && resizable) {
                float endDistance = moveEvent.getMousePosition().dst(transform.pos);
                float scaleF = endDistance / startDistance;
                logger.setLogLevel(LogLevel.DEBUG);
                logger.debug("scaleF = " + scaleF + ";" + " linew = " + lineWidth);

                transform.scale.set(startScale.cpy().scl(scaleF));

                switch (currentEdge) {
                    case 0, 2 -> {
                        transform.scale.set(startScale.cpy().scl(scaleF, 1));
                    }
                    case 1, 3 -> {
                        transform.scale.set(startScale.cpy().scl(1, scaleF));
                    }
                    case 4, 7, 6, 5 -> {
                        transform.scale.set(startScale.cpy().scl(scaleF));
                    }
                }
                transform.scale.x = Utils.clamp(transform.scale.x, Float.MAX_VALUE, 0f);
                transform.scale.y = Utils.clamp(transform.scale.y, Float.MAX_VALUE, 0f);
            }

            if (clickEvent!=null && clickEvent.getButton()==Input.Buttons.LEFT && !gameObject.getClass().isAnnotationPresent(ForbidSelection.class)) {
                //new Logger().setLogLevel(LogLevel.DEBUG).debug(drag + " " + clickEvent);
                Vector2 mousePos = clickEvent.getMousePosition();
                // p1.x < x < p2.x
                // p1.y < y < p3.y
                if (selectable) {
                    if (isPointInRect(mousePos)) {
                        if (selection.isEmpty() && !InputHandler.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                            selection.add(gameObject);
                            selected = true;
                        } else if (InputHandler.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                            selection.add(gameObject);
                            selected = true;
                        }
                    } else if (!InputHandler.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        selection.remove(gameObject);
                        selected = false;
                    } else {
                        if (selection.size() == 1) {
                            selection.remove(gameObject);
                            selected = false;
                        }
                    }
                }
            }

            super.draw();
        }

        @Override
        public void update() {
            draw();
            super.update();
        }

        public boolean isPointOnEdge(float x, float y) {
            // mx my
            // x1-lw < mx < x1+lw
            Vector2[] points = getVector2Points();

            if (!isPointInShape(x, y)) return false;

            return  (points[0].x-this.lineWidth <= x && x <= points[0].x+this.lineWidth) ||
                    (points[2].x-this.lineWidth <= x && x <= points[2].x+this.lineWidth) ||
                    (points[0].y-this.lineWidth <= y && y <= points[0].y+this.lineWidth) ||
                    (points[2].y-this.lineWidth <= y && y <= points[2].y+this.lineWidth);
        }

        public boolean isPointOnEdge(Vector2 point) {
            return isPointOnEdge(point.x, point.y);
        }


        /**
         *
         * @param x point x
         * @param y point y
         * @return returns -1 if point is not on edge; 0 if point is on left edge; 1 if point is on bottom edge;
         * 2 if point is on right edge; 3 if point is on top edge; 4 if point is on left bottom corner;
         * 5 if point is on left top corner; 6 if point is on right top corner; 7 if point is on left bottom corner.
         */
        public int getEdgeOfPoint(float x, float y) {
            if (!isPointOnEdge(x, y)) return -1;

            if (x > getVector2Points()[0].x-this.lineWidth && x < getVector2Points()[0].x+this.lineWidth
                    && y > getVector2Points()[0].y-this.lineWidth && y < getVector2Points()[0].y+this.lineWidth) return 4;
            if (x > getVector2Points()[2].x-this.lineWidth && x < getVector2Points()[2].x+this.lineWidth
                    && y > getVector2Points()[2].y-this.lineWidth && y < getVector2Points()[2].y+this.lineWidth) return 6;

            if (x > getVector2Points()[0].x-this.lineWidth && x < getVector2Points()[0].x+this.lineWidth
                    && y > getVector2Points()[2].y-this.lineWidth && y < getVector2Points()[2].y+this.lineWidth) return 5;
            if (x > getVector2Points()[2].x-this.lineWidth && x < getVector2Points()[2].x+this.lineWidth
                    && y > getVector2Points()[0].y-this.lineWidth && y < getVector2Points()[0].y+this.lineWidth) return 7;

            if (x > getVector2Points()[0].x-this.lineWidth && x < getVector2Points()[0].x+this.lineWidth) return 0;
            if (y > getVector2Points()[0].y-this.lineWidth && y < getVector2Points()[0].y+this.lineWidth) return 1;
            if (x > getVector2Points()[2].x-this.lineWidth && x < getVector2Points()[2].x+this.lineWidth) return 2;
            if (y > getVector2Points()[2].y-this.lineWidth && y < getVector2Points()[2].y+this.lineWidth) return 3;
            return -1;
        }

        /**
         *
         * @param point Vector2 of tested point
         * @return returns -1 if point is not on edge; 0 if point is on left edge; 1 if point is on bottom edge;
         * 2 if point is on right edge; 3 if point is on top edge; 4 if point is on left bottom corner;
         * 5 if point is on left top corner; 6 if point is on right top corner; 7 if point is on left bottom corner.
         */
        public int getEdgeOfPoint(Vector2 point) {
            return getEdgeOfPoint(point.x, point.y);
        }

        @Override
        public boolean isPointInShape(float x, float y) {

            Vector2[] points = getVector2Points();

            return points[0].x-lineWidth  < x && x < points[1].x+lineWidth &&
                    points[0].y-lineWidth < y && y < points[2].y+lineWidth;
        }

        @Override
        public boolean isPointInShape(Vector2 point) {
            return this.isPointInShape(point.x, point.y);
        }
    }

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
        } else return; // название класса говорит за себя

        ec = Editor.getInstance().getCam().getComponent(EditorCamera.class);

//        rect = new ObjectRect(1, 1);
        try {
            RectOwner c = gameObject.getExtendedComponent(RectOwner.class);
            if (c instanceof Resizable) resizable = true;
            rect = new ObjectRect(c.getRect().w, c.getRect().h, transform);
            gameObject.addComponent(rect);
        } catch (NullPointerException ignored) {}
    }



    @Override
    public void update() {
        if (areKeysPressed(Input.Keys.SHIFT_LEFT, Input.Keys.D)) {
            selected = false;
            selection.clear();
        }
        if (areKeysPressed(Input.Keys.ALT_LEFT, Input.Keys.B) && selected) {
            canMove = !canMove;
        }

        if (isKeyJustPressed(Input.Keys.D)) {
            logger.debug(selection);
        }
    }

    public final boolean isSelected() {
        return selected;
    }

    public final boolean isResizable() {
        return resizable;
    }
}
