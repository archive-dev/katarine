package org.katarine.components;

import com.badlogic.gdx.graphics.g2d.Batch;
import org.katarine.Game;

public abstract class DrawableComponent extends Component {
    public long drawOrder = 0; // 0 is first
    public Batch batch = Game.getEditorInstance()!=null ? Game.getEditorInstance().getBatch()
            : Game.gameInstance.getBatch();
    public boolean show = true;

    protected abstract void draw();

    @Override
    public void update() {
        if (show) draw();
    }
}
