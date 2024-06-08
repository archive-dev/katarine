package org.katarine.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.katarine.editor.components.ForbidSelection;
import org.katarine.annotations.EditorObject;
import org.katarine.annotations.NotInstantiatable;
import org.katarine.systems.EditorCameraSystem;
import org.katarine.systems.System;
import org.katarine.utils.Utils;
import org.katarine.editor.imgui.HideInInspector;
import org.katarine.utils.serialization.Assets;
import org.katarine.utils.serialization.annotations.DontSerialize;

@EditorObject
@NotInstantiatable
@ForbidSelection
@HideInInspector
@DontSerialize
public class Grid extends System {
    private TextureRegion reg;

    private float camBorderRight, camBorderLeft, camBorderUp, camBorderBottom, camZoom, spacing, spacing2;
    private Color col1, col2;

    private SpriteBatch batch;

    @Override
    protected void awake() {
        reg = new TextureRegion(new Texture(Assets.get("whitepx.png")));

        spacing = 1;
        spacing2 = 4;

        col1 = new Color(.3f, .3f, .3f, 1);
        col2 = new Color(.3f, .3f, .3f, 2);
    }

    @Override
    protected void start() {
    }

    @Override
    protected void update() {
        batch = getSystemManager().getSystem(EditorRenderingSystem.class).getSpriteBatch();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        camZoom = getSystemManager().getSystem(EditorCameraSystem.class).getCurrentCamera().zoom;

        float scrW, scrH;
        scrW = getSystemManager().getSystem(EditorRenderingSystem.class).getScreenWidth();
        scrH = getSystemManager().getSystem(EditorRenderingSystem.class).getScreenHeight();

        Camera camera = getSystemManager().getSystem(EditorCameraSystem.class).getCurrentCamera();

        camBorderLeft = (camera.position.x - scrW/2*camZoom);
        camBorderRight = (camera.position.x + scrW/2*camZoom);

        camBorderBottom = (camera.position.y - scrH/2*camZoom);
        camBorderUp = (camera.position.y + scrH/2*camZoom);

        col1.a = 1/camZoom;
        col2.a = 2/camZoom;

        //vertical
        batch.setColor(col1);
        drawVerticalLines(spacing);

        batch.setColor(col2);
        drawVerticalLines(spacing2);

        //horizontal
        batch.setColor(col1);
        drawHorizontalLines(spacing);

        batch.setColor(col2);
        drawHorizontalLines(spacing2);

        batch.setColor(Color.WHITE);

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void drawVerticalLines(float spacing) {
        for (float i = 0; i < camBorderRight; i+=spacing) {
            batch.draw(reg, i, 0, 0, 0, camZoom/ Utils.PPU *2, 1, 1, camZoom+camBorderUp, 0);
            batch.draw(reg, i+camZoom/Utils.PPU *2, 0, 0, 0, camZoom/Utils.PPU *2, 1, 1, camZoom-camBorderBottom, 180);
        }
        for (float i = 0; i > camBorderLeft; i-=spacing) {
            batch.draw(reg, i, 0, 0, 0, camZoom/Utils.PPU *2, 1, 1, camZoom+camBorderUp, 0);
            batch.draw(reg, i+camZoom/Utils.PPU *2, 0, 0, 0, camZoom/Utils.PPU *2, 1, 1, camZoom-camBorderBottom, 180);
        }
    }

    private void drawHorizontalLines(float spacing) {
        for (float i = 0; i < camBorderUp; i+=spacing) {
            batch.draw(reg, 0, i, 0, 0, 1, camZoom/Utils.PPU *2, camZoom+camBorderRight, 1, 0);
            batch.draw(reg, 0, i+camZoom/Utils.PPU *2, 0, 0, 1, camZoom/Utils.PPU *2, camZoom-camBorderLeft, 1, 180);
        }
        for (float i = 0; i > camBorderBottom; i-=spacing) {
            batch.draw(reg, 0, i, 0, 0, 1, camZoom/Utils.PPU *2, camZoom+camBorderRight, 1, 0);
            batch.draw(reg, 0, i+camZoom/Utils.PPU *2, 0, 0, 1, camZoom/Utils.PPU *2, camZoom-camBorderLeft, 1, 180);
        }
    }
}
