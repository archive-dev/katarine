package io.github.whoisamyy.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class DrawableComponent extends Component {
    public long drawOrder = 0; // 0 is first

    protected SpriteBatch batch;
    public boolean show = true;

    protected abstract void draw();

    @Override
    public void update() {
        if (show) draw();
    }
}
