package io.github.whoisamyy.editor.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import io.github.whoisamyy.components.Component;
import io.github.whoisamyy.katarine.annotations.EditorObject;
import io.github.whoisamyy.katarine.annotations.NotInstantiatable;
import io.github.whoisamyy.objects.GameObject;
import io.github.whoisamyy.utils.input.AbstractInputHandler;
import io.github.whoisamyy.utils.input.MouseClickEvent;

@ForbidSelection
@EditorObject
@NotInstantiatable
public final class CursorHandler extends Component {
    public static CursorHandler instance;

    private final Cursor.SystemCursor CURSOR_MOVE_NS = Cursor.SystemCursor.VerticalResize;
    private final Cursor.SystemCursor CURSOR_MOVE_WE = Cursor.SystemCursor.HorizontalResize;
    private final Cursor.SystemCursor CURSOR_MOVE_WENS = Cursor.SystemCursor.AllResize;
    private final Cursor.SystemCursor DEFAULT = Cursor.SystemCursor.Arrow;
    private final Cursor.SystemCursor HAND_MOVE = Cursor.SystemCursor.Hand;

    public static CursorHandler instance() {
        if (instance == null) {
            instance = new CursorHandler();
        }
        return instance;
    }

    private CursorHandler() {
//        this.CURSOR_MOVE_NS =   Cursor.SystemCursor.VerticalResize;     //Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("cursor_move_ns.png")), 0, 0);
//        this.CURSOR_MOVE_WE =   Cursor.SystemCursor.HorizontalResize;   //Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("cursor_move_we.png")), 0, 0);
//        this.CURSOR_MOVE_WENS = Cursor.SystemCursor.AllResize;          //Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("cursor_move_wens.png")), 0, 0);
    }

    @Override
    public void update() {
        MouseClickEvent mce = AbstractInputHandler.getMoveEvent();
        if (mce == null) {
            mce = AbstractInputHandler.getDragEvent();
        }

        boolean movingObject = false;
        for (GameObject s : EditorObjectComponent.selection) {
            movingObject = movingObject || (s.getComponent(EditorObjectComponent.class).isSelected() &&
                    !s.getComponent(EditorObjectComponent.class).rect.isPointOnEdge(mce.getMousePosition()) &&
                    Gdx.input.isButtonPressed(com.badlogic.gdx.Input.Buttons.LEFT));
        }

        boolean onEdge = false;
        for (GameObject s : EditorObjectComponent.selection) {
            if (s.getComponent(EditorObjectComponent.class).rect.isPointOnEdge(mce.getMousePosition()) && !movingObject && s.getComponent(EditorObjectComponent.class).isResizable()) {
                onEdge = true;
                EditorObjectComponent eoc = s.getComponent(EditorObjectComponent.class);
                int edge = eoc.rect.getEdgeOfPoint(mce.getMousePosition());
                if (edge == 0 || edge == 2) {
                    Gdx.graphics.setSystemCursor(CURSOR_MOVE_WE);
                } else if (edge == 1 || edge == 3) {
                    Gdx.graphics.setSystemCursor(CURSOR_MOVE_NS);
                } else if (edge == 4 || edge == 6) {
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.NESWResize);
                } else if (edge == 5 || edge == 7) {
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.NWSEResize);
                }
                break;
            }
        }

        if (movingObject) Gdx.graphics.setSystemCursor(CURSOR_MOVE_WENS);

        if (!onEdge && !movingObject) {
            Gdx.graphics.setSystemCursor(DEFAULT);
        }
    }
}
