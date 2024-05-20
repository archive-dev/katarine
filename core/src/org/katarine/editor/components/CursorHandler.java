package org.katarine.editor.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import imgui.flag.ImGuiMouseCursor;
import org.katarine.components.Component;
import org.katarine.annotations.EditorObject;
import org.katarine.annotations.NotInstantiatable;
import org.katarine.objects.GameObject;
import org.katarine.ui.imgui.ImGui;
import org.katarine.utils.input.AbstractInputHandler;
import org.katarine.utils.input.MouseClickEvent;
import org.lwjgl.glfw.GLFW;

@ForbidSelection
@EditorObject
@NotInstantiatable
public final class CursorHandler extends Component {
    public static final CursorHandler instance = new CursorHandler();

    public enum Cursors {
        CURSOR_MOVE_NS(Cursor.SystemCursor.VerticalResize, GLFW.GLFW_VRESIZE_CURSOR, ImGuiMouseCursor.ResizeNS),
        CURSOR_MOVE_WE(Cursor.SystemCursor.HorizontalResize, GLFW.GLFW_HRESIZE_CURSOR, ImGuiMouseCursor.ResizeEW),
        CURSOR_MOVE_WENS(Cursor.SystemCursor.AllResize, GLFW.GLFW_RESIZE_ALL_CURSOR, ImGuiMouseCursor.ResizeAll),
        DEFAULT(Cursor.SystemCursor.Arrow, GLFW.GLFW_ARROW_CURSOR, ImGuiMouseCursor.Arrow),
        HAND_MOVE(Cursor.SystemCursor.Hand, GLFW.GLFW_HAND_CURSOR, ImGuiMouseCursor.Hand),
        RESIZE_NESW(Cursor.SystemCursor.NESWResize, GLFW.GLFW_RESIZE_NESW_CURSOR, ImGuiMouseCursor.ResizeNESW),
        RESIZE_NWSE(Cursor.SystemCursor.NWSEResize, GLFW.GLFW_RESIZE_NWSE_CURSOR, ImGuiMouseCursor.ResizeNWSE);

        public final Cursor.SystemCursor gdxCursor;
        public final int glfwCursor;
        public final int imguiCursor;

        Cursors(Cursor.SystemCursor gdxCursor, int glfwCursor, int imguiCursor) {
            this.gdxCursor = gdxCursor;
            this.glfwCursor = glfwCursor;
            this.imguiCursor = imguiCursor;
        }
    }

    public Cursors currentCursor = Cursors.DEFAULT;

    public static CursorHandler instance() {
        return instance;
    }

    private CursorHandler() {}

    @Override
    public void update() {
        MouseClickEvent mce = AbstractInputHandler.getMoveEvent();

        boolean movingObject = false;
        boolean onEdge = false;
        for (GameObject s : EditorObjectComponent.selection) {

            if (s.getComponent(EditorObjectComponent.class).rect.isPointOnEdge(mce.getMousePosition()) &&
                    s.getComponent(EditorObjectComponent.class).isResizable()) {
                // if mouse is on edge of any game object, not moving any object and the object is resizable
                // then change the cursor type

                onEdge = true;
                EditorObjectComponent eoc = s.getComponent(EditorObjectComponent.class);
                int edge = eoc.rect.getEdgeOfPoint(mce.getMousePosition());
                if (edge == 0 || edge == 2) {
                    currentCursor = Cursors.CURSOR_MOVE_WE;
                } else if (edge == 1 || edge == 3) {
                    currentCursor = Cursors.CURSOR_MOVE_NS;
                } else if (edge == 4 || edge == 6) {
                    currentCursor = Cursors.RESIZE_NESW;
                } else if (edge == 5 || edge == 7) {
                    currentCursor = Cursors.RESIZE_NWSE;
                }
                break;
            }

            if (!s.getComponent(EditorObjectComponent.class).rect.isPointOnEdge(mce.getMousePosition()) &&
                    s.getComponent(EditorObjectComponent.class).moving) {
                movingObject = true;
                currentCursor = Cursors.CURSOR_MOVE_WENS;
                break;
            }
        }

        if (!onEdge && !movingObject) {
            currentCursor = Cursors.DEFAULT;
        }

        if (!ImGui.controlsInput)
            Gdx.graphics.setSystemCursor(currentCursor.gdxCursor);
    }
}
