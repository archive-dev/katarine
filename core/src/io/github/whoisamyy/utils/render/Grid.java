package io.github.whoisamyy.utils.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL32;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.github.whoisamyy.components.Transform2D;
import io.github.whoisamyy.editor.Editor;
import io.github.whoisamyy.editor.EditorCamera;
import io.github.whoisamyy.logging.LogLevel;
import io.github.whoisamyy.logging.Logger;
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
    private Logger logger = new Logger().setLogLevel(LogLevel.DEBUG);

    @Override
    protected void start() {
        try {
            transform = getComponent(Transform2D.class);
        } catch (NullPointerException e) {
            transform = addComponent(new Transform2D());
        }
        sr = new ShapeRenderer();
        sr.setAutoShapeType(true);
    }

    @Override
    protected void update() {
        Gdx.gl.glEnable(GL32.GL_BLEND);
        Gdx.gl.glBlendFunc(GL32.GL_SRC_ALPHA, GL32.GL_ONE_MINUS_SRC_ALPHA);

        sr.begin();

        sr.set(ShapeRenderer.ShapeType.Filled);
        sr.rect(0, 0, Editor.instance.getWidth()*Utils.PPM, Editor.instance.getHeight()*Utils.PPM, new Color(0x0a0a0aff), new Color(0x0a0a0aff), new Color(0x1F1F1Fff), new Color(0x1F1F1Fff));
        sr.set(ShapeRenderer.ShapeType.Line);

        EditorCamera ec = Editor.instance.getCam().getComponent(EditorCamera.class);
        float camZoom = Editor.instance.getCamera().zoom;
        float camBorderLeft = Editor.instance.getCamera().position.x-(Editor.instance.getWidth() * Utils.PPM /2);
        float camBorderRight = Editor.instance.getCamera().position.x+(Editor.instance.getWidth() * Utils.PPM/2);

        float camBorderUpper = Editor.instance.getCamera().position.y-(Editor.instance.getHeight() * Utils.PPM/2);
        float camBorderBottom = Editor.instance.getCamera().position.y+(Editor.instance.getHeight() * Utils.PPM/2);

        float spacing = Utils.PPM / camZoom;

        Vector2 v = ec.getTransform2D().pos;


        Color col1 = new Color(.3f, .3f, .3f, 1/camZoom);
        Color col2 = new Color(.3f, .3f, .3f, 1);

        // next lines have bug in some way
        // horizontal lines are "moving" vertically. compile and run if you want to see it

        // draw vertical lines
        for (float i = camBorderLeft-(camBorderLeft%Utils.PPM); i < camBorderRight-(camBorderRight%Utils.PPM); i++) {
            sr.line((i-v.x)*spacing, 0, (i-v.x)*spacing, Editor.instance.getHeight()*Utils.PPM, col1, col1);
        }

        // draw horizontal lines
        for (float j = camBorderUpper-(camBorderUpper%Utils.PPM); j < camBorderBottom-(camBorderBottom%Utils.PPM); j++) {
            sr.line(0, (j-v.y)*spacing, Editor.instance.getWidth()*Utils.PPM, (j-v.y)*spacing, col1, col1);
        }

        sr.end();

        Gdx.gl.glDisable(GL32.GL_BLEND);
    }

    @Override
    public void dispose() {
        sr.dispose();
    }
}
