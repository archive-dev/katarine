package io.github.whoisamyy.editor.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import io.github.whoisamyy.components.Component;
import io.github.whoisamyy.components.Sprite;
import io.github.whoisamyy.components.TriggerBox;
import io.github.whoisamyy.editor.Editor;
import io.github.whoisamyy.logging.LogLevel;
import io.github.whoisamyy.objects.GameObject;
import io.github.whoisamyy.utils.EditorObject;
import io.github.whoisamyy.utils.HideInInspector;
import io.github.whoisamyy.utils.input.MouseClickEvent;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

@HideInInspector
public class EditorObjectComponent extends Component {
    public static LinkedHashSet<GameObject> selection = new LinkedHashSet<>(128);
    private static EditorCamera ec;

    TriggerBox triggerBox;
    TriggerBox[] triggerBoxes; // 0 - free-move sqare ( triggerBox ), 1 - Y axis, 2 - X axis

    private ArrowObject[] arrowObjects = new ArrowObject[2];

    private boolean selected = false;


    @Override
    public void awake() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.35f, 0.35f);

        gameObject.addComponent(new TriggerBox(shape));
        logger.setLogLevel(LogLevel.DEBUG);
    }

    @Override
    public void start() {
        triggerBox = gameObject.getComponent(TriggerBox.class);
        ec = Editor.getInstance().getCam().getComponent(EditorCamera.class);
    }

    @Override
    public void update() {
        MouseClickEvent drag = getMouseDragEvent();

        if (!selected && arrowObjects[0]!=null) {
            arrowObjects[0].destroy();
            arrowObjects[1].destroy();

            arrowObjects[0] = null;
            arrowObjects[1] = null;
        }

        if (selected && arrowObjects[0] == null) {
            arrowObjects[0] = GameObject.instantiate(new ArrowObject(ArrowObject.X_AXIS), this.gameObject);
            arrowObjects[1] = GameObject.instantiate(new ArrowObject(ArrowObject.Y_AXIS), this.gameObject);
        }

        if (!triggerBox.isTouched() && selected && isButtonJustPressed(Input.Buttons.LEFT)) {
            selected = false;
            selection.remove(gameObject);
        }

        if (triggerBox.isTouched() && !selected && isButtonJustPressed(Input.Buttons.LEFT)) {
            selected = true;
            selection.add(gameObject);
        }

        if (triggerBox.isTouched() && selected && drag!=null && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            triggerBox.gameObject.relativePosition.add(new Vector2(drag.getDragDelta()).scl(ec.getCamera().zoom));
            //for (GameObject c : gameObject.getChildren()) {
            //    c.transform.pos.add(new Vector2(drag.getDragDelta()).scl(ec.getCamera().zoom));
            //}
        }

        if (isKeysPressed(Input.Keys.SHIFT_LEFT, Input.Keys.D)) {
            selection.clear();
            if (arrowObjects[0] != null) {
                arrowObjects[0].destroy();
                arrowObjects[1].destroy();
                arrowObjects[0] = null;
                arrowObjects[1] = null;
            }
            selected = false;
        }
    }

    @HideInInspector
    @EditorObject
    private class ArrowObject extends GameObject {
        static final int X_AXIS = 0;
        static final int Y_AXIS = 1;

        private final int axis;
        private Sprite arrowSprite;
        private TriggerBox triggerBox;

        public ArrowObject(int axis) {
            this.axis=axis;
        }

        @Override
        protected void awake() {
            logger.setLogLevel(LogLevel.DEBUG);
            logger.debug("Awake ArrowObject");

            this.arrowSprite = addComponent(new Sprite(new Texture(Gdx.files.internal("arrow.png")), 2, 2, 1, 1, 0, false, false));
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(this.axis==0?0.5f:0.1f, this.axis==0?0.1f:0.5f);
        }

        @Override
        protected void update() {
            try {
                ((PolygonShape) getComponent(EditorObjectComponent.class).triggerBox.getFixture().getShape()).setAsBox(this.axis == 0 ? 0.5f : 0.1f, this.axis == 0 ? 0.1f : 0.5f);
            } catch (NullPointerException ignored) {}

            if (parent.getChildren().size()<=2) {
                this.transform.pos.set(parent.transform.pos);
            } else if (parent.getChildren().size()>2) {
                List<Vector2> childrenPos = new LinkedList<>();
                for (GameObject c : parent.getChildren()) {
                    if (c.getClass().isAssignableFrom(ArrowObject.class)) continue;

                    childrenPos.add(c.transform.pos);
                }

                float avgX = 0;
                float avgY = 0;

                for (Vector2 v : childrenPos) {
                    avgX += v.x;
                    avgY += v.y;
                }

                avgX /= childrenPos.size();
                avgY /= childrenPos.size();

                transform.pos.set(avgX, avgY);
            }
        }
    }
}
