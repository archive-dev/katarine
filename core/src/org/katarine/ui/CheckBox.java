package org.katarine.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.katarine.components.Resizable;
import org.katarine.components.Sprite;
import org.katarine.editor.Editor;
import org.katarine.editor.components.EditorObjectComponent;
import org.katarine.Game;
import org.katarine.logging.LogLevel;
import org.katarine.objects.GameObject;
import org.katarine.utils.Utils;
import org.katarine.utils.math.shapes.Rect;
import org.katarine.utils.render.RectOwner;

public class CheckBox extends UiObject implements RectOwner, Resizable {
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

    private GameObject checkBoxObject;

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
        checkBoxRect = new Rect(transform.pos.x, transform.pos.y, 1, 1);
        checkBoxObject = GameObject.instantiate(GameObject.class);
        checkBox = checkBoxObject.addComponent(new Sprite(new Texture(Gdx.files.internal("whitepx.png")), transform.scale.y/2, transform.scale.y/2));
        gameObject.addChild(checkBoxObject);
        gameObject.addComponent(checkBoxText);

        checkBox.updateOrder = checkBoxText.updateOrder+1;

        checkBox.relativePosition.sub(transform.scale.x/2 - transform.scale.y/2, 0);

        if (transform.scale.x < checkBoxText.getTextWidth() + transform.scale.y) {
            transform.scale.x = checkBoxText.getTextWidth() + transform.scale.y;
        }

        try {
            EditorObjectComponent eoc = checkBoxObject.getComponent(EditorObjectComponent.class);
            eoc.resizable = false;
            eoc.movable = false;
        } catch (NullPointerException ignored) {}

        super.start();

        checkBoxText.setColor(textColor);


        switch (anchor) {
            case TOP_LEFT ->      checkBoxText.getPos().set(transform.pos.cpy().sub((transform.scale.x / 2) - checkBoxText.getTextWidth()/2 - textPadding.x - transform.scale.y, -transform.scale.y/2 + checkBoxText.getTextHeight()/2 + textPadding.y));
            case CENTER_LEFT ->   checkBoxText.getPos().set(transform.pos.cpy().sub((transform.scale.x / 2) - checkBoxText.getTextWidth()/2 - textPadding.x - transform.scale.y, 0));
            case BOTTOM_LEFT ->   checkBoxText.getPos().set(transform.pos.cpy().sub((transform.scale.x / 2) - checkBoxText.getTextWidth()/2 - textPadding.x - transform.scale.y, transform.scale.y/2 - checkBoxText.getTextHeight()/2 - textPadding.y));

            case CENTER ->        //noinspection SuspiciousNameCombination
                                  checkBoxText.getPos().set(new Vector2(transform.scale.y, 0));
            case TOP_CENTER ->    //noinspection SuspiciousNameCombination
                                  checkBoxText.getPos().set(transform.pos.cpy().add(transform.scale.y, transform.scale.y/2 - checkBoxText.getTextHeight()/2 + textPadding.y));
            case BOTTOM_CENTER -> checkBoxText.getPos().set(transform.pos.cpy().sub(-transform.scale.y, transform.scale.y/2 - checkBoxText.getTextHeight()/2 + textPadding.y));

            case TOP_RIGHT ->     checkBoxText.getPos().set(transform.pos.cpy().add((transform.scale.x / 2) - checkBoxText.getTextWidth()/2 - textPadding.x, transform.scale.y/2 - checkBoxText.getTextHeight()/2 - textPadding.y));
            case CENTER_RIGHT ->  checkBoxText.getPos().set(transform.pos.cpy().add((transform.scale.x / 2) - checkBoxText.getTextWidth()/2 - textPadding.x, 0));
            case BOTTOM_RIGHT ->  checkBoxText.getPos().set(transform.pos.cpy().add((transform.scale.x / 2) - checkBoxText.getTextWidth()/2 - textPadding.x, -transform.scale.y/2 + checkBoxText.getTextHeight()/2 + textPadding.y));
        }
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
        //noinspection SuspiciousNameCombination
        checkBox.transform.scale.x = transform.scale.y;
        checkBox.transform.scale.y = transform.scale.y;

        switch (anchor) {
            case TOP_LEFT ->      checkBoxText.getPos().set(transform.pos.cpy().sub((transform.scale.x / 2) - checkBoxText.getTextWidth()/2 - textPadding.x - transform.scale.y, -transform.scale.y/2 + checkBoxText.getTextHeight()/2 + textPadding.y));
            case CENTER_LEFT ->   checkBoxText.getPos().set(transform.pos.cpy().sub((transform.scale.x / 2) - checkBoxText.getTextWidth()/2 - textPadding.x - transform.scale.y, 0));
            case BOTTOM_LEFT ->   checkBoxText.getPos().set(transform.pos.cpy().sub((transform.scale.x / 2) - checkBoxText.getTextWidth()/2 - textPadding.x - transform.scale.y, transform.scale.y/2 - checkBoxText.getTextHeight()/2 - textPadding.y));

            case CENTER ->        //noinspection SuspiciousNameCombination
                                  checkBoxText.getPos().set(new Vector2(transform.scale.y, 0));
            case TOP_CENTER ->    //noinspection SuspiciousNameCombination
                                  checkBoxText.getPos().set(transform.pos.cpy().add(transform.scale.y, transform.scale.y/2 - checkBoxText.getTextHeight()/2 + textPadding.y));
            case BOTTOM_CENTER -> checkBoxText.getPos().set(transform.pos.cpy().sub(-transform.scale.y, transform.scale.y/2 - checkBoxText.getTextHeight()/2 + textPadding.y));

            case TOP_RIGHT ->     checkBoxText.getPos().set(transform.pos.cpy().add((transform.scale.x / 2) - checkBoxText.getTextWidth()/2 - textPadding.x, transform.scale.y/2 - checkBoxText.getTextHeight()/2 - textPadding.y));
            case CENTER_RIGHT ->  checkBoxText.getPos().set(transform.pos.cpy().add((transform.scale.x / 2) - checkBoxText.getTextWidth()/2 - textPadding.x, 0));
            case BOTTOM_RIGHT ->  checkBoxText.getPos().set(transform.pos.cpy().add((transform.scale.x / 2) - checkBoxText.getTextWidth()/2 - textPadding.x, -transform.scale.y/2 + checkBoxText.getTextHeight()/2 + textPadding.y));
        }
        checkBox.relativePosition.set(-transform.scale.x/2, 0).add(transform.scale.y/2, 0);

        if (isKeyJustPressed(Input.Keys.E)) {
            logger.setLogLevel(LogLevel.DEBUG);
            logger.debug(checkBox.getScaleX());
        }

        if (Editor.getEditorInstance()!=null || Game.getEditorInstance().isEditorMode()) return;
        if (getMouseMoveEvent()==null) return;

        // monstrocity
        //noinspection ConditionalExpressionWithIdenticalBranches
        if (checkBoxRect.isPointInRect(getMouseMoveEvent().getMousePosition().x,
                (Editor.getEditorInstance()!=null?Editor.getEditorInstance().getHeight():Game.getEditorInstance().getHeight())-getMouseMoveEvent().getMousePosition().y)

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
