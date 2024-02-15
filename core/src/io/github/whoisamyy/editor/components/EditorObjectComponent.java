package io.github.whoisamyy.editor.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import io.github.whoisamyy.components.Component;
import io.github.whoisamyy.components.Text;
import io.github.whoisamyy.components.TriggerBox;
import io.github.whoisamyy.editor.Editor;
import io.github.whoisamyy.logging.LogLevel;
import io.github.whoisamyy.objects.GameObject;
import io.github.whoisamyy.ui.Button;
import io.github.whoisamyy.utils.EditorObject;
import io.github.whoisamyy.utils.HideInInspector;
import io.github.whoisamyy.utils.input.MouseClickEvent;

import java.util.LinkedHashSet;

@HideInInspector
public class EditorObjectComponent extends Component {
    @HideInInspector
    @EditorObject
    public static class EditorTriggerBox extends TriggerBox {
        public Vector2 relativePos = new Vector2();

        public EditorTriggerBox(PolygonShape shape) {
            super(shape);
        }

        @Override
        public void update() {
            this.body.setTransform(transform.pos.cpy().add(relativePos), 0);

            Array<Contact> contacts = world.getContactList();

            isTouched = false;

            for (Contact contact : contacts) {
                if (contact.getFixtureA()==this.fixture && contact.getFixtureB()==Editor.getInstance().getCursorBox().getFixture()) {
                    isTouched = true;
                    break;
                }
                if (contact.getFixtureB()==this.fixture && contact.getFixtureA()==Editor.getInstance().getCursorBox().getFixture()) {
                    isTouched = true;
                    break;
                }
                if (contact.getFixtureA().getBody().getType() == BodyDef.BodyType.DynamicBody && contact.getFixtureB().getBody().getType() == BodyDef.BodyType.DynamicBody ||
                        contact.getFixtureB().getBody().getType() == BodyDef.BodyType.DynamicBody && contact.getFixtureA().getBody().getType() == BodyDef.BodyType.DynamicBody) {
                    isTouched = Editor.getInstance()!=null;
                    break;
                }
            }
        }
    }
    public static LinkedHashSet<GameObject> selection = new LinkedHashSet<>(16);
    private static EditorCamera ec;

    EditorTriggerBox triggerBox;

    private boolean selected = false;

    @Override
    public void awake() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.35f, 0.35f);

        gameObject.addComponent(new EditorTriggerBox(shape));

        logger.setLogLevel(LogLevel.DEBUG);
    }

    @Override
    public void start() {
        triggerBox = gameObject.getComponent(EditorTriggerBox.class);
        try {
            gameObject.getComponentExtender(Button.class);
        } catch (NullPointerException e) {
            try {
                Text t = this.gameObject.getComponentExtender(Text.class);
                triggerBox.relativePos.set(t.getTextWidth() / 2, -t.getTextHeight() / 2);
                triggerBox.setShapeAsBox(t.getTextWidth() / 2, t.getTextHeight() / 2);
            } catch (NullPointerException ignored) {}
        }

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
        }

        if (isKeysPressed(Input.Keys.SHIFT_LEFT, Input.Keys.D)) {
            selected = false;
            selection.clear();
        }
    }
}
