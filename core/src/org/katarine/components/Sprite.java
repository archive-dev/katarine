package org.katarine.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import org.katarine.logging.LogLevel;
import org.katarine.utils.Utils;
import org.katarine.utils.math.shapes.Rect;
import org.katarine.utils.render.RectOwner;

public class Sprite extends Component implements RectOwner, Resizable {
    private Texture texture;
    private SpriteBatch batch;
    private boolean show;
    /**
     * These values are set in units, NOT pixels. If you want to use pixels make sure that you do {@code px/Utils.PPU}
     * @see Utils
     */
    private float spriteHeight = 1, spriteWidth = 1;

    public final Vector2 spriteSize = new Vector2(spriteWidth, spriteHeight);

    private float rotation=0;

    private boolean flipX = false, flipY = false;

    private com.badlogic.gdx.graphics.g2d.Sprite sprite;

    public final Vector2 relativePosition = new Vector2();

    public Sprite() {}

    @Override
    public void awake() {
        if (texture!=null)
            sprite = new com.badlogic.gdx.graphics.g2d.Sprite(texture);
    }

    @Override
    protected void update() {
        if (sprite!=null) {
            batch.setColor(sprite.getColor());
            sprite.setSize(spriteSize.x * getTransform().scale.x, spriteSize.y * getTransform().scale.y);
            sprite.setPosition(getTransform().x() - (spriteSize.x * getTransform().scale.x / 2) + relativePosition.x, getTransform().y() - (spriteSize.y * getTransform().scale.y / 2) + relativePosition.y);
            sprite.setOrigin(spriteSize.x * getTransform().scale.x / 2, spriteSize.y * getTransform().scale.y / 2);
            sprite.setRotation(rotation + getTransform().rotation);
            sprite.draw(batch);

            batch.setColor(Color.WHITE);
        }
    }

    public final void setBatch(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void die() {
        texture.dispose();
    }

    public Texture getTexture() {
        return texture;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public float getSpriteWidth() {
        return spriteSize.x* getTransform().scale.x;
    }

    public float getSpriteHeight() {
        return spriteSize.y* getTransform().scale.y;
    }

    public float getScaleX() {
        return getTransform().scale.x;
    }

    public float getScaleY() {
        return getTransform().scale.y;
    }

    public float getRotation() {
        return rotation;
    }

    public boolean isFlipX() {
        return flipX;
    }

    public boolean isFlipY() {
        return flipY;
    }

    public boolean isShow() {
        return show;
    }

    public com.badlogic.gdx.graphics.g2d.Sprite getSprite() {
        return sprite;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setSpriteWidth(float spriteWidth) {
        this.spriteSize.x = spriteWidth;
    }

    public void setSpriteHeight(float spriteHeight) {
        this.spriteSize.y = spriteHeight;
    }

    public void setScaleX(float scaleX) {
        this.getTransform().scale.x = scaleX;
    }

    public void setScaleY(float scaleY) {
        this.getTransform().scale.y = scaleY;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void setFlipX(boolean flipX) {
        this.flipX = flipX;
    }

    public void setFlipY(boolean flipY) {
        this.flipY = flipY;
    }

    @Override
    public Rect getRect() {
        logger.setLogLevel(LogLevel.DEBUG).debug("got rect");
        return new Rect(spriteSize.x, spriteSize.y);
    }
}
