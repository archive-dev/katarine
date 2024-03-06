package io.github.whoisamyy.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import io.github.whoisamyy.components.Sprite;
import io.github.whoisamyy.editor.Editor;
import io.github.whoisamyy.katarine.Game;
import io.github.whoisamyy.utils.Utils;
import io.github.whoisamyy.utils.math.shapes.Rect;
import io.github.whoisamyy.utils.render.RectOwner;

public class CheckBox extends UiObject implements RectOwner {
    Rect checkBoxRect;

    boolean isActive = false;
    public Color primaryColor = new Color(0.8f, 0.8f, 0.8f, 1);
    public Color textColor = Color.WHITE;
    public Color secondaryColor = Color.WHITE;
    Text checkBoxText;
    public float fontSize = 2;
    public String font = "fonts/Roboto-Medium.ttf";
    public String text = "Button";
    public Sprite checkBox;

    public final Vector2 checkBoxSize = new Vector2(5, 2);
    public final Vector2 textPadding = new Vector2(0.05f, 0.05f);
    public Anchor anchor = Anchor.CENTER;

    @Override
    public void awake() {
        checkBoxText = new Text(font, fontSize, Color.BLACK, 1 / Utils.PPU, Color.BLACK, true);
    }

    @Override
    public void start() {
        checkBoxText.text = text;
        checkBoxText.setSizeXY(fontSize);
        checkBoxRect = new Rect(transform.pos.x, transform.pos.y, checkBoxSize.x, checkBoxSize.y);
        checkBox = gameObject.addComponent(new Sprite(new Texture(Gdx.files.internal("whitepx.png")), checkBoxSize.y/2, checkBoxSize.y/2));
        gameObject.addComponent(checkBoxText);

        checkBox.updateOrder = checkBoxText.updateOrder+1;

        if (checkBoxSize.x < checkBoxText.getTextWidth() + checkBoxSize.y) {
            checkBoxSize.x = checkBoxText.getTextWidth() + checkBoxSize.y;
            checkBoxRect.w = checkBoxSize.x;
        }

        switch (anchor) {
            case TOP_LEFT ->      checkBoxText.getPos().sub((checkBoxSize.x / 2) - checkBoxText.getTextWidth()/2 - textPadding.x - checkBoxSize.y, -checkBoxSize.y/2 + checkBoxText.getTextHeight()/2 + textPadding.y);
            case CENTER_LEFT ->   checkBoxText.getPos().sub((checkBoxSize.x / 2) - checkBoxText.getTextWidth()/2 - textPadding.x - checkBoxSize.y, 0);
            case BOTTOM_LEFT ->   checkBoxText.getPos().sub((checkBoxSize.x / 2) - checkBoxText.getTextWidth()/2 - textPadding.x - checkBoxSize.y, checkBoxSize.y/2 - checkBoxText.getTextHeight()/2 - textPadding.y);

            case CENTER -> checkBoxText.getPos().set(transform.pos.cpy().add(checkBoxSize.y/2, 0));
            case TOP_CENTER ->    //noinspection SuspiciousNameCombination
                    checkBoxText.getPos().add(checkBoxSize.y, checkBoxSize.y/2 - checkBoxText.getTextHeight()/2 + textPadding.y);
            case BOTTOM_CENTER -> checkBoxText.getPos().sub(-checkBoxSize.y, checkBoxSize.y/2 - checkBoxText.getTextHeight()/2 + textPadding.y);

            case TOP_RIGHT ->     checkBoxText.getPos().add((checkBoxSize.x / 2) - checkBoxText.getTextWidth()/2 - textPadding.x, checkBoxSize.y/2 - checkBoxText.getTextHeight()/2 - textPadding.y);
            case CENTER_RIGHT ->  checkBoxText.getPos().add((checkBoxSize.x / 2) - checkBoxText.getTextWidth()/2 - textPadding.x, 0);
            case BOTTOM_RIGHT ->  checkBoxText.getPos().add((checkBoxSize.x / 2) - checkBoxText.getTextWidth()/2 - textPadding.x, -checkBoxSize.y/2 + checkBoxText.getTextHeight()/2 + textPadding.y);
        }
        super.start();

        checkBox.relativePosition.sub(checkBoxSize.x/2 - checkBoxSize.y/2, 0);
        checkBoxText.setColor(textColor);
    }

    public final void setTextColor(Color textColor) {
        this.textColor = textColor;
        checkBoxText.setColor(textColor);
    }

    @Override
    public void update() {
        checkBoxText.setSizeXY(fontSize);
        super.update();
        checkBoxRect.x = transform.pos.x;
        checkBoxRect.y = transform.pos.y;
//        if (Editor.getInstance()!=null || Game.getInstance().isEditorMode()) return;
        if (getMouseMoveEvent()==null) return;

        // monstrocity
        if (checkBoxRect.isPointInRect(getMouseMoveEvent().getMousePosition().x,
                (Editor.getInstance()!=null?Editor.getInstance().getHeight():Game.getInstance().getHeight())-getMouseMoveEvent().getMousePosition().y)

                && isButtonJustPressed(Input.Buttons.LEFT)) {
            isActive = !isActive;
        }

        if (isActive) {
            checkBox.getSprite().setColor(secondaryColor);
        } else {
            checkBox.getSprite().setColor(primaryColor);
        }
    }

    public final boolean isActive() {
        return isActive;
    }

    public final void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public Rect getRect() {
        return checkBoxRect;
    }
}
