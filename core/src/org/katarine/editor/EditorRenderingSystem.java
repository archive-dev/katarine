package org.katarine.editor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import org.katarine.rendering.RenderingSystem;
import org.katarine.systems.EditorCameraSystem;

class EditorRenderingSystem extends RenderingSystem {
    private OrthographicCamera camera;

    @Override
    protected void start() {
        this.screenHeight = Editor.getEditorInstance().getHeight();
        this.screenWidth = Editor.getEditorInstance().getWidth();

        // required systems for this system to work correctly
        getSystemManager().getSystem(Grid.class);
        camera = getSystemManager().getSystem(EditorCameraSystem.class).getCurrentCamera();

//        getSystemManager().getSystem(ImGuiSystem.class);
    }

    @Override
    protected void preUpdate() {
        super.preUpdate();
        getShapeDrawer().filledRectangle(this.camera.position.x- this.camera.zoom*this.getMainViewport().getWorldWidth()/2,
                this.camera.position.y- this.camera.zoom*this.getMainViewport().getWorldHeight()/2,
                this.getScreenWidth() * this.camera.zoom, this.getScreenHeight() * this.camera.zoom,
                new Color(0x0a0a0aff),
                new Color(0x0a0a0aff),
                new Color(0x1F1F1Fff),
                new Color(0x1F1F1Fff));
    }

    @Override
    protected void update() {
        super.update();
    }

    @Override
    protected void postUpdate() {
        super.postUpdate();
    }

    @Override
    protected void die() {
        super.die();
    }

    public final OrthographicCamera getCamera() {
        return camera;
    }
}
