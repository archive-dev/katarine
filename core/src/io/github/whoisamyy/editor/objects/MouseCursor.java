package io.github.whoisamyy.editor.objects;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import io.github.whoisamyy.components.Transform2D;
import io.github.whoisamyy.components.TriggerBox;
import io.github.whoisamyy.logging.LogLevel;
import io.github.whoisamyy.logging.Logger;
import io.github.whoisamyy.objects.GameObject;
import io.github.whoisamyy.utils.EditorObject;
import io.github.whoisamyy.utils.Utils;
import io.github.whoisamyy.utils.input.MouseClickEvent;

@EditorObject
public class MouseCursor extends GameObject {
    @Override
    protected void awake() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f / Utils.PPU, 0.5f / Utils.PPU);
        addComponent(new TriggerBox(shape)).getBody().setType(BodyDef.BodyType.DynamicBody);
    }

    @Override
    protected void update() {
        MouseClickEvent event = getMouseMoveEvent();
        if (event!=null) {
            transform.pos.set(event.getMousePosition());
        }
    }
}
