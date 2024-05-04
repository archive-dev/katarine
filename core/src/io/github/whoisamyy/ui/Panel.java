package io.github.whoisamyy.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import io.github.whoisamyy.components.Sprite;
import io.github.whoisamyy.utils.math.shapes.Rect;
import io.github.whoisamyy.utils.render.RectOwner;

public class Panel extends UiObject implements RectOwner {
    Rect rect;
    Sprite panel;

    public Color color = Color.WHITE.cpy();

    public Vector2 panelSize = new Vector2(2, 2);

    @Override
    public void start() {
        super.start();
        panel = gameObject.addComponent(new Sprite(new Texture(Gdx.files.internal("whitepx.png")), panelSize.x, panelSize.y));
        rect = new Rect(transform.pos.x, transform.pos.y, panelSize.x, panelSize.y);
    }

    @Override
    public void update() {
        rect.x = transform.pos.x;
        rect.y = transform.pos.y;
        panel.setSpriteWidth(panelSize.x);
        panel.setSpriteHeight(panelSize.y);
        panel.getSprite().setColor(color);
        rect.w = panelSize.x;
        rect.h = panelSize.y;
        super.update();
    }

    @Override
    public Rect getRect() {
        return rect;
    }
}
