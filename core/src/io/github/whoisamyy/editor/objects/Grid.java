package io.github.whoisamyy.editor.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.whoisamyy.editor.Editor;
import io.github.whoisamyy.editor.components.ForbidSelection;
import io.github.whoisamyy.objects.GameObject;
import io.github.whoisamyy.katarine.annotations.EditorObject;
import io.github.whoisamyy.katarine.annotations.NotInstantiatable;
import io.github.whoisamyy.utils.Utils;
import io.github.whoisamyy.utils.serialization.annotations.HideInInspector;

@EditorObject
@NotInstantiatable
@ForbidSelection
@HideInInspector
public class Grid extends GameObject {
    private ShapeRenderer sr;
    private TextureRegion reg;

    private float camBorderRight, camBorderLeft, camBorderUp, camBorderBottom, camZoom, spacing, spacing2;
    private Color col1, col2;

    @Override
    protected void awake() {
        sr = new ShapeRenderer();
        sr.setAutoShapeType(true);
        reg = new TextureRegion(new Texture(Gdx.files.internal("whitepx.png")));

        spacing = 1;
        spacing2 = 5;

        col1 = new Color(.3f, .3f, .3f, 1);
        col2 = new Color(.3f, .3f, .3f, 2);
    }

    @Override
    protected void update() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        camZoom = Editor.editorInstance.getMainCamera().zoom;

        Camera camera = Editor.editorInstance.getMainCamera();

        camBorderLeft = (camera.position.x - Editor.getEditorInstance().getWidth()/2*camZoom);
        camBorderRight = (camera.position.x + Editor.getEditorInstance().getWidth()/2*camZoom);

        camBorderBottom = (camera.position.y - Editor.getEditorInstance().getHeight()/2*camZoom);
        camBorderUp = (camera.position.y + Editor.getEditorInstance().getHeight()/2*camZoom);

        col1.a = 1/camZoom;
        col2.a = 2/camZoom;

        //vertical
        Editor.getEditorInstance().getBatch().setColor(col1);
        drawVerticalLines(spacing);

        Editor.getEditorInstance().getBatch().setColor(col2);
        drawVerticalLines(spacing2);

        //horizontal
        Editor.getEditorInstance().getBatch().setColor(col1);
        drawHorizontalLines(spacing);

        Editor.getEditorInstance().getBatch().setColor(col2);
        drawHorizontalLines(spacing2);

        Editor.getEditorInstance().getBatch().setColor(Color.WHITE);

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    //я подумывал о том, чтобы вместо TextureRegion использовать Sprite(gdx), но это значит что мне нужно будет держать несколько спрайтов и тратить на это память. так что не. пока что
    private void drawVerticalLines(float spacing) {
        for (float i = 0; i < camBorderRight; i+=spacing) {
            Editor.getEditorInstance().getBatch().draw(reg, i, 0, 0, 0, camZoom/Utils.PPU *2, 1, 1, camZoom+camBorderUp, 0);
            Editor.getEditorInstance().getBatch().draw(reg, i+camZoom/Utils.PPU *2, 0, 0, 0, camZoom/Utils.PPU *2, 1, 1, camZoom-camBorderBottom, 180);
        }
        for (float i = 0; i > camBorderLeft; i-=spacing) {
            Editor.getEditorInstance().getBatch().draw(reg, i, 0, 0, 0, camZoom/Utils.PPU *2, 1, 1, camZoom+camBorderUp, 0);
            Editor.getEditorInstance().getBatch().draw(reg, i+camZoom/Utils.PPU *2, 0, 0, 0, camZoom/Utils.PPU *2, 1, 1, camZoom-camBorderBottom, 180);
        }
    }

    private void drawHorizontalLines(float spacing) {
        for (float i = 0; i < camBorderUp; i+=spacing) {
            Editor.getEditorInstance().getBatch().draw(reg, 0, i, 0, 0, 1, camZoom/Utils.PPU *2, camZoom+camBorderRight, 1, 0);
            Editor.getEditorInstance().getBatch().draw(reg, 0, i+camZoom/Utils.PPU *2, 0, 0, 1, camZoom/Utils.PPU *2, camZoom-camBorderLeft, 1, 180);
        }
        for (float i = 0; i > camBorderBottom; i-=spacing) {
            Editor.getEditorInstance().getBatch().draw(reg, 0, i, 0, 0, 1, camZoom/Utils.PPU *2, camZoom+camBorderRight, 1, 0);
            Editor.getEditorInstance().getBatch().draw(reg, 0, i+camZoom/Utils.PPU *2, 0, 0, 1, camZoom/Utils.PPU *2, camZoom-camBorderLeft, 1, 180);
        }
    }

    @Override
    public void dispose() {
        sr.dispose();
    }
}
