package io.github.whoisamyy.components;

import com.badlogic.gdx.graphics.g2d.Batch;
import io.github.whoisamyy.editor.Editor;
import io.github.whoisamyy.katarine.Game;

public abstract class DrawableComponent extends Component {
    public long drawOrder = 0; // 0 is first
    public Batch batch;
    public boolean show = true;
    protected boolean ui;

    public DrawableComponent(boolean ui) {
        if (ui) {
            try {
                batch = Game.getEditorInstance().getUiBatch();
            } catch (NullPointerException e) {
                batch = Editor.getEditorInstance().getUiBatch();
            }
        } else {
            try {
                batch = Game.getEditorInstance().getBatch();
            } catch (NullPointerException e) {
                batch = Editor.getEditorInstance().getBatch();
            }
        }
        this.ui = ui;
    }

    protected abstract void draw();

    @Override
    public void update() {
        if (show) draw();
    }
}
