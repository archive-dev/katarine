package org.katarine.editor;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import org.katarine.components.Component;
import org.katarine.components.Resizable;
import org.katarine.annotations.EditorObject;
import org.katarine.annotations.NotInstantiatable;
import org.katarine.editor.components.ForbidSelection;
import org.katarine.objects.GameObject;
import org.katarine.editor.imgui.ImGui;
import org.katarine.systems.EditorCameraSystem;
import org.katarine.utils.Utils;
import org.katarine.utils.input.InputHandler;
import org.katarine.utils.input.InputSystem;
import org.katarine.utils.input.MouseClickEvent;
import org.katarine.utils.render.RectOwner;
import org.katarine.utils.render.shapes.RectShape;
import org.katarine.editor.imgui.HideInInspector;
import org.katarine.utils.serialization.annotations.DontSerialize;

import java.util.HashSet;
import java.util.Objects;

@HideInInspector
@DontSerialize
public class EditorObjectComponent extends Component {
    public static final HashSet<GameObject> selection = new HashSet<>(16);
    private static OrthographicCamera ec;

    public boolean canMove = true;
    public boolean selected = false;
    public boolean resizable = false;
    public boolean movable = true;
    public boolean selectable = true;

    @HideInInspector
    public static boolean selectionNN = true;

    public boolean resizing = false;
    public boolean moving = false;

    private ObjectRect rect;

    @EditorObject
    @NotInstantiatable
    @HideInInspector
    public class ObjectRect extends RectShape {
        private final Vector2 screenPos = new Vector2();
        private final Vector2 relativeWorldPos = new Vector2();

        private static final Color GREEN = Color.GREEN.cpy().add(0, 0, 0, -0.3f);
        private static final Color CYAN = Color.CYAN.cpy().add(0, 0, 0, -0.3f);
        private static final Color RED = Color.RED.cpy().add(0, 0, 0, -0.3f);

        int currentEdge = -1;

        float startDistance = 1;
        private final Vector2 startScale = new Vector2(1, 1);

        Vector2 deltaMove = new Vector2();
        private float lineWidth = 0.25f;

        public ObjectRect(float x, float y) {
            super(x, y, CYAN);
        }

        @Override
        public void draw() {
            lineWidth = getTransform().scale.len()/10*ec.zoom;
            lineWidth = Utils.clamp(lineWidth, 0.1f*ec.zoom, 0.05f*ec.zoom);

            MouseClickEvent drag = InputSystem.getDragEvent();
            MouseClickEvent moveEvent = InputSystem.getMoveEvent();
            MouseClickEvent clickEvent = InputSystem.getTouchDownEvent();

            if (selected) {
                setColor(GREEN);
            } else {
                setColor(CYAN);
            }
            if (!canMove) {
                setColor(RED);
            }

            if (selected && InputHandler.areKeysPressed(Input.Keys.ALT_LEFT, Input.Keys.S)) {
                this.getTransform().pos.x = Math.round(this.getTransform().pos.x);
                this.getTransform().pos.y = Math.round(this.getTransform().pos.y);
            }

            moving = selected && InputHandler.isButtonPressed(Input.Buttons.LEFT) && canMove && isPointInShape(moveEvent.getMousePosition());
            if (moving && drag!=null && !InputHandler.isButtonPressed(Input.Buttons.MIDDLE) &&
                    !isPointOnEdge(moveEvent.getMousePosition()) && movable && !ImGui.controlsInput) {
                deltaMove.sub(drag.getDragDelta().cpy().scl(ec.zoom));
                if (InputHandler.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                    if (deltaMove.x >= 0.25f/ec.zoom || deltaMove.x <= -0.25f/ec.zoom) {
                        this.getTransform().pos.x-= (deltaMove.x > 0 ? 1 : -1) * 0.25f;
                        deltaMove.x = 0;
                    }
                    if (deltaMove.y >= 0.25f/ec.zoom || deltaMove.y <= -0.25f/ec.zoom) {
                        this.getTransform().pos.y-=(deltaMove.y > 0 ? 1 : -1) * 0.25f;
                        deltaMove.y = 0;
                    }
                } else
                    this.getTransform().pos.add(drag.getDragDelta().cpy().scl(ec.zoom));
            }

            if (selected && InputHandler.isButtonPressed(Input.Buttons.LEFT) && canMove && isPointOnEdge(moveEvent.getMousePosition()) && !ImGui.controlsInput) {
                currentEdge = getEdgeOfPoint(Objects.requireNonNullElse(drag, moveEvent).getMousePosition());
                if (!resizing) {
                    resizing = true;
                    startDistance = getTransform().pos.dst(moveEvent.getMousePosition());
                    startScale.set(getTransform().scale.cpy());
                }
            } else {
                currentEdge = -1;
                resizing = false;
            }

            if (resizing && resizable) {
                moving = false;
                float endDistance = moveEvent.getMousePosition().dst(getTransform().pos);
                float scaleF = endDistance / startDistance;

                switch (currentEdge) {
                    case 0, 2 -> getTransform().scale.set(startScale.x*scaleF, startScale.y);
                    case 1, 3 -> getTransform().scale.set(startScale.x, startScale.y*scaleF);
                    case 4, 7, 6, 5 -> getTransform().scale.set(startScale.cpy().scl(scaleF));
                }
                getTransform().scale.x = Utils.clamp(getTransform().scale.x, Float.MAX_VALUE, 0f);
                getTransform().scale.y = Utils.clamp(getTransform().scale.y, Float.MAX_VALUE, 0f);
            }

            if (clickEvent!=null && clickEvent.getButton()==Input.Buttons.LEFT && !getGameObject().getClass().isAnnotationPresent(ForbidSelection.class)) {
                //new Logger().setLogLevel(LogLevel.DEBUG).debug(drag + " " + clickEvent);
                Vector2 mousePos = clickEvent.getMousePosition();
                // p1.x < x < p2.x
                // p1.y < y < p3.y
                if (selectable) {
                    if (isPointInRect(mousePos)) {
                        if (selection.isEmpty() && !InputHandler.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                            selection.add(getGameObject());
                            selected = true;
                        } else if (InputHandler.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                            selection.add(getGameObject());
                            selected = true;
                        }
                    } else if (!InputHandler.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        selection.remove(getGameObject());
                        selected = false;
                    } else {
                        if (selection.size() == 1) {
                            selection.remove(getGameObject());
                            selected = false;
                        }
                    }
                }
            }

            super.draw();
//            super.update();
        }

        public boolean isPointOnEdge(float x, float y) {
            // mx my
            // x1-lw < mx < x1+lw
            Vector2[] points = getVector2Points();

            if (!isPointInShape(x, y)) return false;

            return (points[0].x-this.lineWidth <= x && x <= points[0].x+this.lineWidth) ||
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
         * @return returns -1 if point is not on edge; <br>
         * 0 if point is on left edge; <br>
         * 1 if point is on bottom edge; <br>
         * 2 if point is on right edge; <br>
         * 3 if point is on top edge; <br>
         * 4 if point is on left bottom corner; <br>
         * 5 if point is on left top corner; <br>
         * 6 if point is on right top corner; <br>
         * 7 if point is on left bottom corner.
         */
        public int getEdgeOfPoint(float x, float y) {
            if (!isPointOnEdge(x, y)) return -1;

            if (x > getVector2Points()[0].x-this.lineWidth && x < getVector2Points()[0].x+this.lineWidth &&
                y > getVector2Points()[0].y-this.lineWidth && y < getVector2Points()[0].y+this.lineWidth) return 4;
            if (x > getVector2Points()[2].x-this.lineWidth && x < getVector2Points()[2].x+this.lineWidth &&
                y > getVector2Points()[2].y-this.lineWidth && y < getVector2Points()[2].y+this.lineWidth) return 6;

            if (x > getVector2Points()[0].x-this.lineWidth && x < getVector2Points()[0].x+this.lineWidth &&
                y > getVector2Points()[2].y-this.lineWidth && y < getVector2Points()[2].y+this.lineWidth) return 5;
            if (x > getVector2Points()[2].x-this.lineWidth && x < getVector2Points()[2].x+this.lineWidth &&
                y > getVector2Points()[0].y-this.lineWidth && y < getVector2Points()[0].y+this.lineWidth) return 7;

            if (x > getVector2Points()[0].x-this.lineWidth && x < getVector2Points()[0].x+this.lineWidth) return 0;
            if (y > getVector2Points()[0].y-this.lineWidth && y < getVector2Points()[0].y+this.lineWidth) return 1;
            if (x > getVector2Points()[2].x-this.lineWidth && x < getVector2Points()[2].x+this.lineWidth) return 2;
            if (y > getVector2Points()[2].y-this.lineWidth && y < getVector2Points()[2].y+this.lineWidth) return 3;
            return -1;
        }

        /**
         *
         * @param point position of tested point
         * @return returns -1 if point is not on edge;<br> 0 if point is on left edge;<br> 1 if point is on bottom edge;<br>
         * 2 if point is on right edge;<br> 3 if point is on top edge;<br> 4 if point is on left bottom corner;<br>
         * 5 if point is on left top corner;<br> 6 if point is on right top corner;<br> 7 if point is on left bottom corner.
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
        ec = getSystemManager().getSystem(EditorCameraSystem.class).getCurrentCamera();
        ImGui.guis.put(getGameObject().getName()+".PANEL", new InspectorPanel(getGameObject()));
    }

    @Override
    public void start() {
//        rect = new ObjectRect(1, 1);
        RectOwner c = getGameObject().getComponentSubclass(RectOwner.class);
        if (c != null) {
            if (c instanceof Resizable) resizable = true;
            rect = new ObjectRect(c.getRect().w, c.getRect().h);
            getGameObject().addComponent(rect);
        } else {
            rect = new ObjectRect(1, 1);
            getGameObject().addComponent(rect);
        }


//        Panel panel = new Panel(this.gameObject.getName());
//        io.github.whoisamyy.ui.imgui.ImGui.addPanel(panel);
    }

    @Override
    public void update() {
        if (InputSystem.areKeysPressed(Input.Keys.SHIFT_LEFT, Input.Keys.D) && !ImGui.controlsInput) {
            selected = false;
            selection.clear();
        }
        if (InputSystem.areKeysPressed(Input.Keys.ALT_LEFT, Input.Keys.B) && selected && !ImGui.controlsInput) {
            canMove = !canMove;
        }

        if (InputSystem.isKeyJustPressed(Input.Keys.D) && !ImGui.controlsInput) {
            logger.debug(selection);
        }
    }

    public final boolean isSelected() {
        return selected;
    }

    public final boolean isResizable() {
        return resizable;
    }

    public final ObjectRect getRect() {
        return rect;
    }
}