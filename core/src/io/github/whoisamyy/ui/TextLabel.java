package io.github.whoisamyy.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import io.github.whoisamyy.components.Resizable;
import io.github.whoisamyy.utils.Utils;
import io.github.whoisamyy.utils.math.shapes.Rect;
import io.github.whoisamyy.utils.render.RectOwner;

public class TextLabel extends UiObject implements RectOwner, Resizable {
    Rect rect;
    public Color color = Color.WHITE;

    public float fontSize = 2;
    public final String font = "fonts/Roboto-Medium.ttf";
    public String text = "label";

    Text labelText;

    public final Vector2 labelSize = new Vector2(3, 1);
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
        labelText.text = text;
        if (labelText.getTextWidth() > labelSize.x) {
            labelSize.x = labelText.getTextWidth();
        }
        if (labelText.getTextHeight() > labelSize.y) {
            labelSize.y = labelText.getTextHeight();
        }
        rect = new Rect(transform.pos.x, transform.pos.y, labelSize.x, labelSize.y);

//        switch (anchor) {
//            case TOP_LEFT ->      labelText.getPos().sub((labelSize.x / 2) - labelText.getTextWidth()/2 - textPadding.x, -labelSize.y/2 + labelText.getTextHeight()/2 + textPadding.y);
//            case CENTER_LEFT ->   labelText.getPos().sub((labelSize.x / 2) - labelText.getTextWidth()/2 - textPadding.x, 0);
//            case BOTTOM_LEFT ->   labelText.getPos().sub((labelSize.x / 2) - labelText.getTextWidth()/2 - textPadding.x, labelSize.y/2 - labelText.getTextHeight()/2 - textPadding.y);
//
//            case CENTER ->        labelText.getPos().set(transform.pos.cpy());
//            case TOP_CENTER ->    labelText.getPos().add(0, labelSize.y/2 - labelText.getTextHeight()/2 + textPadding.y);
//            case BOTTOM_CENTER -> labelText.getPos().sub(0, labelSize.y/2 - labelText.getTextHeight()/2 + textPadding.y);
//
//            case TOP_RIGHT ->     labelText.getPos().add((labelSize.x / 2) - labelText.getTextWidth()/2 - textPadding.x, labelSize.y/2 - labelText.getTextHeight()/2 - textPadding.y);
//            case CENTER_RIGHT ->  labelText.getPos().add((labelSize.x / 2) - labelText.getTextWidth()/2 - textPadding.x, 0);
//            case BOTTOM_RIGHT ->  labelText.getPos().add((labelSize.x / 2) - labelText.getTextWidth()/2 - textPadding.x, -labelSize.y/2 + labelText.getTextHeight()/2 + textPadding.y);
//        }
        super.start();
    }

    @Override
    public void update() {
        labelText.setSizeXY(fontSize);
        super.update();
        rect.x = transform.pos.x;
        rect.y = transform.pos.y;

        Vector2 ls = labelSize.cpy().scl(transform.scale);

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
