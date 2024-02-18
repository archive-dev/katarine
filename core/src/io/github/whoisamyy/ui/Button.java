package io.github.whoisamyy.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import io.github.whoisamyy.components.Sprite;
import io.github.whoisamyy.components.Text;
import io.github.whoisamyy.editor.Editor;
import io.github.whoisamyy.katarine.Game;
import io.github.whoisamyy.utils.Utils;
import io.github.whoisamyy.utils.input.Action;
import io.github.whoisamyy.utils.math.shapes.Rect;

import java.util.LinkedList;

public class Button extends UiObject {
    private boolean isPressed = false;
    private final LinkedList<Action> actions = new LinkedList<>();

    Rect buttonRect;

    public Color primaryColor = Color.WHITE;
    public Color secondaryColor = new Color(0.8f, 0.8f, 0.8f, 1);
    public Text buttonText;
    public Sprite button;

    public final Vector2 buttonSize = new Vector2(3, 1);
    public final Vector2 textPadding = new Vector2(0.05f, 0.05f);
    public Anchor anchor = Anchor.CENTER;

    @Override
    public void awake() {
        buttonText = new Text("fonts/Roboto-Medium.ttf", buttonSize.y, Color.BLACK, 1 / Utils.PPU, Color.BLACK, true);
        buttonText.text = "Button";
        buttonRect = new Rect(transform.pos.x, transform.pos.y, buttonSize.x, buttonSize.y);

        button = gameObject.addComponent(new Sprite(new Texture(Gdx.files.internal("whitepx.png")), buttonSize.x, buttonSize.y));
        gameObject.addComponent(buttonText);

        button.updateOrder = buttonText.updateOrder+1;
    }

    @Override
    public void start() {
        switch (anchor) {
            case TOP_LEFT -> buttonText.getPos().sub((buttonSize.x / 2) - textPadding.x, -(buttonSize.y / 2 - textPadding.y));
            case CENTER_LEFT -> buttonText.getPos().sub((buttonSize.x / 2) - textPadding.x, -buttonText.getTextHeight()/2);
            case BOTTOM_LEFT -> buttonText.getPos().sub((buttonSize.x / 2) - textPadding.x, -textPadding.y);

            case CENTER -> buttonText.getPos().set(transform.pos.cpy().sub(buttonText.getTextWidth() / 2, -buttonText.getTextHeight() / 2));
            case TOP_CENTER -> buttonText.getPos().add(-buttonText.getTextWidth()/2, buttonSize.y / 2 - textPadding.y);
            case BOTTOM_CENTER -> buttonText.getPos().sub(buttonText.getTextWidth()/2, -textPadding.y);

            case TOP_RIGHT -> buttonText.getPos().add((buttonSize.x / 2) - buttonText.getTextWidth() - textPadding.x, buttonSize.y / 2 - textPadding.y);
            case CENTER_RIGHT -> buttonText.getPos().add((buttonSize.x / 2) - buttonText.getTextWidth() - textPadding.x, buttonText.getTextHeight()/2);
            case BOTTOM_RIGHT -> buttonText.getPos().add((buttonSize.x / 2) - buttonText.getTextWidth() - textPadding.x, textPadding.y);
        }
    }

    @Override
    public void update() {
        super.update();
        buttonRect.x = transform.pos.x;
        buttonRect.y = transform.pos.y;
        if (Editor.getInstance()!=null || Game.getInstance().isEditorMode()) return;
        if (getMouseMoveEvent()==null) return;

        // monstrocity
        if (buttonRect.isPointInRect(getMouseMoveEvent().getMousePosition().x,
                (Editor.getInstance()!=null?Editor.getInstance().getHeight():Game.getInstance().getHeight())-getMouseMoveEvent().getMousePosition().y)

                && isButtonPressed(Input.Buttons.LEFT)) {
            button.getSprites().forEach(s -> s.setColor(secondaryColor));
            isPressed = true;
        } else if (isPressed) {
            button.getSprites().forEach(s -> s.setColor(primaryColor));
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
}
