package org.katarine.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.katarine.components.Resizable;
import org.katarine.components.Sprite;
import org.katarine.editor.Editor;
import org.katarine.Game;
import org.katarine.utils.Utils;
import org.katarine.utils.input.Action;
import org.katarine.utils.math.shapes.Rect;
import org.katarine.utils.render.RectOwner;

import java.util.LinkedList;

public class Button extends UiObject implements RectOwner, Resizable {
    private boolean isPressed = false;
    private final LinkedList<Action> actions = new LinkedList<>();

    Rect buttonRect;

    public Color primaryColor = Color.WHITE;
    public Color secondaryColor = new Color(0.8f, 0.8f, 0.8f, 1);
    Text buttonText;
    public float fontSize = 2;
    public String font = "fonts/NTR-Regular.ttf";
    public String text = "Button";
    public Sprite button;
    
    public final Vector2 textPadding = new Vector2(0.05f, 0.05f);
    public Anchor anchor = Anchor.CENTER;

    @Override
    public void awake() {
        buttonText = new Text(font, fontSize, Color.BLACK, 1 / Utils.PPU, Color.BLACK, true);
    }

    @Override
    public void start() {
        buttonText.text = text;
        buttonText.setSizeXY(fontSize);
        buttonRect = new Rect(transform.pos.x, transform.pos.y, 1, 1);
        button = gameObject.addComponent(new Sprite(new Texture(Gdx.files.internal("whitepx.png")), 1, 1));
        gameObject.addComponent(buttonText);

        button.updateOrder = buttonText.updateOrder+1;

//        switch (anchor) {
//            case TOP_LEFT ->      buttonText.getPos().sub((buttonSize.x / 2) - buttonText.getTextWidth()/2 - textPadding.x, -buttonSize.y/2 + buttonText.getTextHeight()/2 + textPadding.y);
//            case CENTER_LEFT ->   buttonText.getPos().sub((buttonSize.x / 2) - buttonText.getTextWidth()/2 - textPadding.x, 0);
//            case BOTTOM_LEFT ->   buttonText.getPos().sub((buttonSize.x / 2) - buttonText.getTextWidth()/2 - textPadding.x, buttonSize.y/2 - buttonText.getTextHeight()/2 - textPadding.y);

//            case CENTER ->        buttonText.getPos().set(transform.pos.cpy());
//            case TOP_CENTER ->    buttonText.getPos().add(0, buttonSize.y/2 - buttonText.getTextHeight()/2 + textPadding.y);
//            case BOTTOM_CENTER -> buttonText.getPos().sub(0, buttonSize.y/2 - buttonText.getTextHeight()/2 + textPadding.y);

//            case TOP_RIGHT ->     buttonText.getPos().add((buttonSize.x / 2) - buttonText.getTextWidth()/2 - textPadding.x, buttonSize.y/2 - buttonText.getTextHeight()/2 - textPadding.y);
//            case CENTER_RIGHT ->  buttonText.getPos().add((buttonSize.x / 2) - buttonText.getTextWidth()/2 - textPadding.x, 0);
//            case BOTTOM_RIGHT ->  buttonText.getPos().add((buttonSize.x / 2) - buttonText.getTextWidth()/2 - textPadding.x, -buttonSize.y/2 + buttonText.getTextHeight()/2 + textPadding.y);
//        }
        super.start();
    }

    @Override
    public void update() {
        buttonText.setSizeXY(fontSize);
        super.update();
        buttonRect.x = transform.pos.x;
        buttonRect.y = transform.pos.y;

        Vector2 ls = transform.scale.cpy();

        switch (anchor) {
            case TOP_LEFT ->      buttonText.getPos().set(new Vector2().sub((ls.x / 2) - buttonText.getTextWidth()/2 - textPadding.x, -ls.y/2 + buttonText.getTextHeight()/2 + textPadding.y));
            case CENTER_LEFT ->   buttonText.getPos().set(new Vector2().sub((ls.x / 2) - buttonText.getTextWidth()/2 - textPadding.x, 0));
            case BOTTOM_LEFT ->   buttonText.getPos().set(new Vector2().sub((ls.x / 2) - buttonText.getTextWidth()/2 - textPadding.x, ls.y/2 - buttonText.getTextHeight()/2 - textPadding.y));

            case CENTER ->        buttonText.getPos().set(0, 0);
            case TOP_CENTER ->    buttonText.getPos().set(new Vector2().add(0, ls.y/2 - buttonText.getTextHeight()/2 + textPadding.y));
            case BOTTOM_CENTER -> buttonText.getPos().set(new Vector2().sub(0, ls.y/2 - buttonText.getTextHeight()/2 + textPadding.y));

            case TOP_RIGHT ->     buttonText.getPos().set(new Vector2().add((ls.x / 2) - buttonText.getTextWidth()/2 - textPadding.x, ls.y/2 - buttonText.getTextHeight()/2 - textPadding.y));
            case CENTER_RIGHT ->  buttonText.getPos().set(new Vector2().add((ls.x / 2) - buttonText.getTextWidth()/2 - textPadding.x, 0));
            case BOTTOM_RIGHT ->  buttonText.getPos().set(new Vector2().add((ls.x / 2) - buttonText.getTextWidth()/2 - textPadding.x, -ls.y/2 + buttonText.getTextHeight()/2 + textPadding.y));
        }
        
        if (Editor.getEditorInstance()!=null || Game.getEditorInstance().isEditorMode()) return;
        if (getMouseMoveEvent()==null) return;

        // monstrocity
        if (buttonRect.isPointInRect(getMouseMoveEvent().getMousePosition().x,
                (Editor.getEditorInstance()!=null
                        ? Editor.getEditorInstance().getHeight() : Game.getEditorInstance().getHeight())
                        -getMouseMoveEvent().getMousePosition().y)

                && isButtonPressed(Input.Buttons.LEFT)) {
            button.getSprite().setColor(secondaryColor);
            isPressed = true;
        } else if (isPressed) {
            button.getSprite().setColor(primaryColor);
            this.actions.forEach(Action::execute);
            isPressed = false;
        }
    }

    public void addAction(Action action) {
        this.actions.add(action);
    }

    public void clearActions() {
        this.actions.clear();
    }

    public void removeAction(int index) {
        this.actions.remove(index);
    }

    @Override
    public Rect getRect() {
        return buttonRect;
    }
}
