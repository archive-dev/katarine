package io.github.whoisamyy.utils.render.shapes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.whoisamyy.editor.Editor;
import io.github.whoisamyy.katarine.Game;

public class FilledRectShape extends RectShape {
    private Color color1 = Color.WHITE,
            color2 = Color.WHITE,
            color3 = Color.WHITE,
            color4 = Color.WHITE;

    public FilledRectShape() {}

    public FilledRectShape(float width, float height) {
        super(width, height);
        this.color1 = Color.WHITE;
        this.color2 = color1;
        this.color3 = color2;
        this.color4 = color3;
    }

    public FilledRectShape(float width, float height, Color color) {
        super(width, height);
        this.color1 = color;
        this.color2 = color1;
        this.color3 = color2;
        this.color4 = color3;
    }

    /**
     * Constructs a filled rectangle shape with color gradient (bottom to top) of color1 and color2
     * @param w <b></b>
     * @param h <b></b>
     * @param color1 <b></b>
     * @param color2 <b></b>
     */
    public FilledRectShape(float w, float h, Color color1, Color color2) {
        super(w, h);
        this.color1 = color1;
        this.color2 = color1;
        this.color3 = color2;
        this.color4 = color2;
    }

    public FilledRectShape(float width, float height, Color color1, Color color2, Color color3, Color color4) {
        super(width, height);
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.color4 = color4;
    }

    @Override
    public void draw() {
        shapeDrawer.filledRectangle((x-w/2*scaleX), (y-h/2*scaleY), w*scaleX, h*scaleY, color1, color2, color3, color4);
    }
}
