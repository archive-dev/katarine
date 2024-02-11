package io.github.whoisamyy.components;

public abstract class DrawableComponent extends Component {
    public boolean show = true;

    protected abstract void draw();

    @Override
    public void update() {
        if (show) draw();
    }
}
