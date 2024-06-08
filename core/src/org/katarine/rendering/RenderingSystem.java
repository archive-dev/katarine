package org.katarine.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.katarine.systems.EditorCameraSystem;
import org.katarine.systems.System;
import org.katarine.utils.Utils;
import org.katarine.utils.input.MouseClickEvent;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class RenderingSystem extends System {
    protected final SpriteBatch spriteBatch = new SpriteBatch();
    protected final SpriteBatch uiSpriteBatch = new SpriteBatch();

    protected final ShapeDrawer shapeDrawer = new ShapeDrawer(spriteBatch);

    protected EditorCameraSystem editorCameraSystem;

    protected ScreenViewport mainViewport, guiViewport;

    protected volatile float screenWidth, screenHeight;

    @Override
    protected void awake() {
        editorCameraSystem = getSystemManager().getSystem(EditorCameraSystem.class);
        mainViewport = new ScreenViewport(editorCameraSystem.getCurrentCamera());

        MouseClickEvent.setScreenViewport(mainViewport);

        spriteBatch.setProjectionMatrix(editorCameraSystem.getCurrentCamera().combined);
        spriteBatch.setTransformMatrix(editorCameraSystem.getCurrentCamera().projection);

        mainViewport.setUnitsPerPixel(1/Utils.PPU);

        spriteBatch.enableBlending();
        shapeDrawer.setTextureRegion(new TextureRegion(new Texture(Gdx.files.internal("assets/whitepx.png")), 0, 0, 1, 1));
//        uiSpriteBatch.enableBlending();
    }

    @Override
    protected void preUpdate() {
        shapeDrawer.setDefaultLineWidth(1/Utils.PPU * ((OrthographicCamera) editorCameraSystem.getCurrentCamera()).zoom);
        ScreenUtils.clear(0, 0, 0, 0);
//        new Logger(LogLevel.DEBUG).debug(mainViewport.getScreenWidth() + " : " +
//                mainViewport.getWorldWidth());

        mainViewport.setScreenWidth(Gdx.graphics.getWidth());
        mainViewport.setScreenHeight(Gdx.graphics.getHeight());

        mainViewport.setWorldSize(Gdx.graphics.getWidth()/Utils.PPU, Gdx.graphics.getHeight()/Utils.PPU);

//        spriteBatch.setTransformMatrix(editorCameraSystem.getCurrentCamera().projection);

        shapeDrawer.setPixelSize(1/Utils.PPU);
        spriteBatch.setProjectionMatrix(editorCameraSystem.getCurrentCamera().combined);
        spriteBatch.begin();
//        uiSpriteBatch.begin();

//        editorCameraSystem.getCurrentCamera().update();
    }

    @Override
    protected void update() {

        mainViewport.update(mainViewport.getScreenWidth(), mainViewport.getScreenHeight(), false);
    }

    @Override
    protected void postUpdate() {
        spriteBatch.end();
//        uiSpriteBatch.end();
    }

    @Override
    protected void die() {
        spriteBatch.dispose();
//        uiSpriteBatch.dispose();
    }

    public void resize(int width, int height) {
        mainViewport.update(width, height, true);
        editorCameraSystem.getCurrentCamera().update(true);

        this.screenWidth = width/Utils.PPU;
        this.screenHeight = height/Utils.PPU;
    }

/*
    public final SpriteBatch getUiSpriteBatch() {
        return uiSpriteBatch;
    }
*/

    public final SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public final float getScreenWidth() {
        return screenWidth;
    }

    public final void setScreenWidth(float screenWidth) {
        this.screenWidth = screenWidth;
    }

    public final float getScreenHeight() {
        return screenHeight;
    }

    public final void setScreenHeight(float screenHeight) {
        this.screenHeight = screenHeight;
    }

    public final ShapeDrawer getShapeDrawer() {
        return shapeDrawer;
    }

    public final ScreenViewport getMainViewport() {
        return mainViewport;
    }
}
