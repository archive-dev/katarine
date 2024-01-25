package io.github.whoisamyy.utils.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL32;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import io.github.whoisamyy.components.SpriteComponent;
import io.github.whoisamyy.components.Transform2D;
import io.github.whoisamyy.editor.Editor;
import io.github.whoisamyy.editor.EditorCamera;
import io.github.whoisamyy.logging.LogLevel;
import io.github.whoisamyy.logging.Logger;
import io.github.whoisamyy.objects.GameObject;
import io.github.whoisamyy.test.Game;
import io.github.whoisamyy.utils.EditorObject;
import io.github.whoisamyy.utils.NotInstantiatable;
import io.github.whoisamyy.utils.Utils;

@EditorObject
@NotInstantiatable
public class Grid extends GameObject {
    private int size = 50;
    private ShapeRenderer sr;
    private Transform2D transform;
    private SpriteComponent sprite;
    private Logger logger = new Logger();

    @Override
    protected void start() {
        try {
            transform = getComponent(Transform2D.class);
        } catch (NullPointerException e) {
            transform = addComponent(new Transform2D());
        }
        try {
            sprite = getComponent(SpriteComponent.class);
        } catch (NullPointerException e) {
            sprite = addComponent(new SpriteComponent(Game.getInstance().getBatch(), new Texture(Gdx.files.internal("whitepx.png")), 1, 1));
        }

        sr = new ShapeRenderer();
        sr.setAutoShapeType(true);
    }

    @Override
    protected void update() {
        Gdx.gl.glEnable(GL32.GL_BLEND);
        Gdx.gl.glBlendFunc(GL32.GL_SRC_ALPHA, GL32.GL_ONE_MINUS_SRC_ALPHA);

        EditorCamera ec = Editor.instance.getCam().getComponent(EditorCamera.class);
        float camZoom = Editor.instance.getCamera().zoom;

        Camera camera = Editor.instance.getCamera();

        float camBorderLeft = ec.getTransform2D().getPosX()-(camera.viewportWidth*Utils.PPM)/2;
        float camBorderRight = ec.getTransform2D().getPosX()+(camera.viewportWidth*Utils.PPM)/2;

        float camBorderBottom = ec.getTransform2D().getPosY()-(camera.viewportHeight*Utils.PPM)/2;
        float camBorderUp = ec.getTransform2D().getPosY()+(camera.viewportHeight*Utils.PPM)/2;

        float spacing = 1;
        float spacing2 = 5;

        Color col1 = new Color(.3f, .3f, .3f, .5f);
        Color col2 = new Color(.3f, .3f, .3f, 1);

        //vertical
        Editor.getInstance().getBatch().setColor(col1);
        for (float i = transform.getPosX(); i < camBorderRight*camZoom; i+=spacing) {
            Editor.getInstance().getBatch().draw(sprite.getTexture().get(0), i, camBorderBottom*camZoom, 0, 0, camZoom/Utils.PPM, (int) (camBorderUp - camBorderBottom)*Utils.PPM, 1, 1, 0, 0, 0, 1, 1, false, false);
        }

        for (float i = transform.getPosX(); i > camBorderLeft*camZoom; i-=spacing) {
            Editor.getInstance().getBatch().draw(sprite.getTexture().get(0), i, camBorderBottom*camZoom, 0, 0, camZoom/Utils.PPM, (int) (camBorderUp - camBorderBottom)*Utils.PPM, 1, 1, 0, 0, 0, 1, 1, false, false);
        }

        Editor.getInstance().getBatch().setColor(col2);
        for (float i = transform.getPosX(); i < camBorderRight*camZoom; i+=spacing2) {
            Editor.getInstance().getBatch().draw(sprite.getTexture().get(0), i, camBorderBottom*camZoom, 0, 0, camZoom/Utils.PPM, (int) (camBorderUp - camBorderBottom)*Utils.PPM, 1, 1, 0, 0, 0, 1, 1, false, false);
        }

        for (float i = transform.getPosX(); i > camBorderLeft*camZoom; i-=spacing2) {
            Editor.getInstance().getBatch().draw(sprite.getTexture().get(0), i, camBorderBottom*camZoom, 0, 0, camZoom/Utils.PPM, (int) (camBorderUp - camBorderBottom)*Utils.PPM, 1, 1, 0, 0, 0, 1, 1, false, false);
        }

        //horizontal
        Editor.getInstance().getBatch().setColor(col1);
        for (float i = transform.getPosY(); i < camBorderUp; i+=spacing) {
            Editor.getInstance().getBatch().draw(sprite.getTexture().get(0), camBorderLeft*camZoom, i, 0, 0, (int) (camBorderUp - camBorderBottom)*Utils.PPM, camZoom/Utils.PPM, 1, 1, 0, 0, 0, 1, 1, false, false);
        }

        for (float i = transform.getPosY(); i > camBorderBottom; i-=spacing) {
            Editor.getInstance().getBatch().draw(sprite.getTexture().get(0), camBorderLeft*camZoom, i, 0, 0, (int) (camBorderUp - camBorderBottom)*Utils.PPM, camZoom/Utils.PPM, 1, 1, 0, 0, 0, 1, 1, false, false);
        }

        Editor.getInstance().getBatch().setColor(col2);
        for (float i = transform.getPosY(); i < camBorderUp; i+=spacing2) {
            Editor.getInstance().getBatch().draw(sprite.getTexture().get(0), camBorderLeft*camZoom, i, 0, 0, (int) (camBorderUp - camBorderBottom)*Utils.PPM, camZoom/Utils.PPM, 1, 1, 0, 0, 0, 1, 1, false, false);
        }

        for (float i = transform.getPosY(); i > camBorderBottom; i-=spacing2) {
            Editor.getInstance().getBatch().draw(sprite.getTexture().get(0), camBorderLeft*camZoom, i, 0, 0, (int) (camBorderUp - camBorderBottom)*Utils.PPM, camZoom/Utils.PPM, 1, 1, 0, 0, 0, 1, 1, false, false);
        }

        Editor.getInstance().getBatch().setColor(Color.WHITE);

        Gdx.gl.glDisable(GL32.GL_BLEND);
    }

    @Override
    public void dispose() {
        sr.dispose();
    }
}
