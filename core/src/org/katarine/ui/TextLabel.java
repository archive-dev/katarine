package org.katarine.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import org.katarine.components.Resizable;
import org.katarine.utils.Utils;
import org.katarine.utils.math.shapes.Rect;
import org.katarine.utils.render.RectOwner;

public class TextLabel extends UiObject implements RectOwner, Resizable {
    Rect rect;
    public Color color = Color.WHITE;

    public float fontSize = 2;
    public final String font = "fonts/Roboto-Medium.ttf";
    public String text = "label";

    Text labelText;

    public final Vector2 textPadding = new Vector2(0.05f, 0.05f);
    public Anchor anchor = Anchor.CENTER;

    @Override
    public void awake() {
        labelText = new Text(font, fontSize, Color.BLACK, 1 / Utils.PPU, Color.BLACK, true);
    }

    @Override
    public void start() {
        gameObject.addComponent(labelText);
        labelText.setSizeXY(fontSize);
        if (labelText.getTextWidth() > transform.scale.x) {
            transform.scale.x = labelText.getTextWidth();
        }
        if (labelText.getTextHeight() > transform.scale.y) {
            transform.scale.y = labelText.getTextHeight();
        }
        rect = new Rect(transform.pos.x, transform.pos.y, 1, 1);
        super.start();
    }

    @Override
    public void update() {
        if (fontSize == 0) fontSize = 0.0000001f;
        labelText.setSizeXY(fontSize);
        super.update();
        rect.w = transform.scale.x;
        rect.h = transform.scale.y;
        rect.x = transform.pos.x;
        rect.y = transform.pos.y;
        labelText.text = text;

        Vector2 ls = transform.scale.cpy();

        switch (anchor) {
            case TOP_LEFT ->      labelText.getPos().set(new Vector2().sub((ls.x / 2) - labelText.getTextWidth()/2 - textPadding.x, -ls.y/2 + labelText.getTextHeight()/2 + textPadding.y));
            case CENTER_LEFT ->   labelText.getPos().set(new Vector2().sub((ls.x / 2) - labelText.getTextWidth()/2 - textPadding.x, 0));
            case BOTTOM_LEFT ->   labelText.getPos().set(new Vector2().sub((ls.x / 2) - labelText.getTextWidth()/2 - textPadding.x, ls.y/2 - labelText.getTextHeight()/2 - textPadding.y));

            case CENTER ->        labelText.getPos().set(0, 0);
            case TOP_CENTER ->    labelText.getPos().set(new Vector2().add(0, ls.y/2 - labelText.getTextHeight()/2 + textPadding.y));
            case BOTTOM_CENTER -> labelText.getPos().set(new Vector2().sub(0, ls.y/2 - labelText.getTextHeight()/2 + textPadding.y));

            case TOP_RIGHT ->     labelText.getPos().set(new Vector2().add((ls.x / 2) - labelText.getTextWidth()/2 - textPadding.x, ls.y/2 - labelText.getTextHeight()/2 - textPadding.y));
            case CENTER_RIGHT ->  labelText.getPos().set(new Vector2().add((ls.x / 2) - labelText.getTextWidth()/2 - textPadding.x, 0));
            case BOTTOM_RIGHT ->  labelText.getPos().set(new Vector2().add((ls.x / 2) - labelText.getTextWidth()/2 - textPadding.x, -ls.y/2 + labelText.getTextHeight()/2 + textPadding.y));
        }
    }

    @Override
    public Rect getRect() {
        return rect;
    }
}