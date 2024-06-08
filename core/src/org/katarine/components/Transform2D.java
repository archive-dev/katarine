package org.katarine.components;

import com.badlogic.gdx.math.Vector2;
import org.katarine.editor.imgui.Range;

import java.util.LinkedList;

public class Transform2D extends Component {
    public String name;
    public Transform2D parent;
    public final LinkedList<Transform2D> children = new LinkedList<>();

    public final Vector2 pos = new Vector2(0, 0);
    public final Vector2 relativePos = new Vector2(0, 0);
    @Range.FloatRange(min = 0, max = 360)
    public float rotation = 0;
    public final Vector2 scale = new Vector2(1, 1);

    public Transform2D() {}

    public Transform2D(Vector2 pos) {
        this.pos.set(pos);
    }

    public void setRelativePosition(Vector2 pos) {
        this.getGameObject().relativePosition.set(pos);
    }

    public float x() {
        return pos.x;
    }

    public float y() {
        return pos.y;
    }
    @Override
    protected void start() {
        this.name = getGameObject().getName();
    }

    @Override
    public void update() { // TODO
        if (this.getGameObject()!=null)
            this.getGameObject().setName(this.name);

        if (this.parent == null) return;
        this.pos.set(this.parent.pos.cpy().add(this.relativePos));
        this.relativePos.set(this.pos);
    }
}
