package io.github.whoisamyy.utils.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.github.whoisamyy.components.Transform2D;
import io.github.whoisamyy.editor.Editor;
import io.github.whoisamyy.editor.EditorCamera;
import io.github.whoisamyy.objects.GameObject;
import io.github.whoisamyy.utils.EditorObject;
import io.github.whoisamyy.utils.NotInstantiatable;
import io.github.whoisamyy.utils.Utils;

@EditorObject
@NotInstantiatable
public class Grid extends GameObject {
    private int size = 50;
    private ShapeRenderer sr;
    private Transform2D transform;

    @Override
    protected void start() {
        try {
            transform = getComponent(Transform2D.class);
        } catch (NullPointerException e) {
            transform = addComponent(new Transform2D());
        }
        sr = new ShapeRenderer();
    }

    @Override
    protected void update() {
        sr.begin(ShapeRenderer.ShapeType.Line);;

        EditorCamera ec = Editor.instance.getCam().getComponent(EditorCamera.class);
        float camZoom = Editor.instance.getCamera().zoom;

        Vector2 v = ec.getTransform2D().pos;

        Color col1 = Color.GRAY;

        // next lines of have bug in some way
        // horizontal lines are "moving" vertically. compile and run if you want to see it

        // draw vertical lines
        for (float i = -Editor.instance.getWidth()*100; i < Editor.instance.getWidth()*100; i++) {
            sr.line((i-v.x)*Utils.PPM/camZoom, 0, (i-v.x)*Utils.PPM/camZoom, Editor.instance.getHeight()*Utils.PPM, col1, col1);
        }

        // draw horizontal lines
        for (float j = -Editor.instance.getHeight()*100; j < Editor.instance.getHeight()*100; j++) {
            sr.line(0, (j-v.y)*Utils.PPM/camZoom, Editor.instance.getWidth()*Utils.PPM, (j-v.y)*Utils.PPM/camZoom, col1, col1);
        }

        sr.end();
    }

    @Override
    public void dispose() {
        sr.dispose();
    }
}
