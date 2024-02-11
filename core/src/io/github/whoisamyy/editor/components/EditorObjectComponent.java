package io.github.whoisamyy.editor.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import io.github.whoisamyy.components.Component;
import io.github.whoisamyy.components.TriggerBox;
import io.github.whoisamyy.editor.Editor;
import io.github.whoisamyy.logging.LogLevel;
import io.github.whoisamyy.objects.GameObject;
import io.github.whoisamyy.utils.EditorObject;
import io.github.whoisamyy.utils.HideInInspector;
import io.github.whoisamyy.utils.input.MouseClickEvent;

import java.util.LinkedHashSet;

@HideInInspector
public class EditorObjectComponent extends Component {
    public static LinkedHashSet<GameObject> selection = new LinkedHashSet<>(16);
    private static EditorCamera ec;

    TriggerBox triggerBox;

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
            selected = false;
            selection.clear();
        }
    }
}
