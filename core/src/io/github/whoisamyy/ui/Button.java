package io.github.whoisamyy.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import io.github.whoisamyy.components.Sprite;
import io.github.whoisamyy.components.Text;
import io.github.whoisamyy.components.TriggerBox;
import io.github.whoisamyy.editor.Editor;
import io.github.whoisamyy.katarine.Game;
import io.github.whoisamyy.utils.Utils;
import io.github.whoisamyy.utils.input.Action;

import java.util.LinkedList;

public class Button extends UiObject {
    private boolean isPressed = false;
    private final LinkedList<Action> actions = new LinkedList<>();

    TriggerBox triggerBox;

    public Color primaryColor = Color.WHITE;
    public Color secondaryColor = new Color(0.8f, 0.8f, 0.8f, 1);
    public Text buttonText;
    public Sprite button;

    public final Vector2 buttonSize = new Vector2(3, 1);
    public final Vector2 textPadding = new Vector2(0.05f, 0.05f);
    public Anchor anchor = Anchor.CENTER;

    @Override
    public void awake() {
        buttonText = new Text("fonts/Roboto-Medium.ttf", 1, Color.WHITE, 1 / Utils.PPU, Color.BLACK, true);
        buttonText.setColor(Color.RED);
        buttonText.text = "Button";
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(buttonSize.x/2, buttonSize.y/2);

        triggerBox = gameObject.addComponent(new TriggerBox(shape));
        button = gameObject.addComponent(new Sprite(new Texture(Gdx.files.internal("whitepx.png")), 3, 1));
        gameObject.addComponent(buttonText);

        button.updateOrder = buttonText.updateOrder+1;
    }

    @Override
    public void start() {
        switch (anchor) {
            case TOP_LEFT -> buttonText.getPos().sub((buttonSize.x / 2) - textPadding.x, -(buttonSize.y / 2 - textPadding.y));
            case CENTER_LEFT -> buttonText.getPos().sub((buttonSize.x / 2) - textPadding.x, -buttonText.getTextHeight()/2);
            case BOTTOM_LEFT -> buttonText.getPos().sub((buttonSize.x / 2) - textPadding.x, -textPadding.y);

            case CENTER -> buttonText.getPos().set(triggerBox.getBody().getWorldCenter().cpy().sub(buttonText.getTextWidth() / 2, -buttonText.getTextHeight() / 2));
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

        if (Editor.getInstance()!=null || Game.getInstance().isEditorMode()) return;
        if (triggerBox.isTouched() && isButtonPressed(Input.Buttons.LEFT)) {
            button.getSprites().forEach(s -> {
                s.setColor(secondaryColor);
            });
            isPressed = true;
        } else if (isPressed) {
            button.getSprites().forEach(s -> {
                s.setColor(primaryColor);
            });
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
