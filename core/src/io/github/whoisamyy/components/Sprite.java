package io.github.whoisamyy.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import io.github.whoisamyy.utils.math.shapes.Rect;
import io.github.whoisamyy.utils.render.RectOwner;
import io.github.whoisamyy.utils.serialization.annotations.HideInInspector;

public class Sprite extends DrawableComponent implements RectOwner, Resizable {
    Texture texture;
    /**
     * These values are set in units, NOT pixels. If you want to use pixels make sure that you do {@code px/Utils.PPU}
     * @see io.github.whoisamyy.utils.Utils
     */
    float spriteHeight;
    public final Vector2 spriteSize = new Vector2(1, spriteHeight);
    @HideInInspector
    public final Vector2 scale = new Vector2(1, 1);
    float rotation=0;
    boolean flipX = false, flipY = false;

    com.badlogic.gdx.graphics.g2d.Sprite sprite;

    public final Vector2 relativePosition = new Vector2();

    public Sprite() {}

    /**
     *
     * @param texture
     * @param spriteWidth sprite width in units
     * @param spriteHeight sprite height in units
     * @param scaleX scale multiplier in x
     * @param scaleY scale multiplier in y
     * @param rotation rotation in degrees
     * @param flipX
     * @param flipY
     */
    public Sprite(Texture texture, float spriteWidth, float spriteHeight, float scaleX, float scaleY, float rotation, boolean flipX, boolean flipY) {
        this.spriteSize.x = spriteWidth;
        this.spriteSize.y = spriteHeight;
        this.scale.x = scaleX;
        this.scale.y = scaleY;
        this.rotation = rotation;
        this.flipX = flipX;
        this.flipY = flipY;
        this.texture = texture;
    }

    public Sprite(Texture texture, float spriteWidth, float spriteHeight) {
        this.spriteSize.x = spriteWidth;
        this.spriteSize.y = spriteHeight;
        this.texture = texture;
    }

    public Sprite(SpriteBatch batch, Texture texture, float spriteWidth, float spriteHeight) {
        this.batch = batch;
        this.spriteSize.x = spriteWidth;
        this.spriteSize.y = spriteHeight;
        this.texture = texture;
    }

    public Sprite(SpriteBatch batch, Texture texture, float spriteWidth, float spriteHeight, float scaleX, float scaleY, float rotation, boolean flipX, boolean flipY) {
        this.batch = batch;
        this.spriteSize.x = spriteWidth;
        this.spriteSize.y = spriteHeight;
        this.scale.x = scaleX;
        this.scale.y = scaleY;
        this.rotation = rotation;
        this.flipX = flipX;
        this.flipY = flipY;
        this.texture = texture;
    }

    public Sprite copy() {
        return new Sprite((SpriteBatch) this.batch, new Texture(texture.getTextureData()), spriteSize.x, spriteSize.y, scale.x, scale.y, rotation, flipX, flipY);
    }

    @Override
    public void awake() {
        if (texture!=null)
            sprite = new com.badlogic.gdx.graphics.g2d.Sprite(texture);
    }

    @Override
    protected void draw() {
        scale.x = transform.scale.x;
        scale.y = transform.scale.y;
        if (sprite!=null) {
            batch.setColor(sprite.getColor());
            sprite.setSize(spriteSize.x * scale.x, spriteSize.y * scale.y);
            sprite.setPosition(transform.x() - (spriteSize.x * scale.x / 2) + relativePosition.x, transform.y() - (spriteSize.y * scale.y / 2) + relativePosition.y);
            sprite.setRotation(rotation + transform.rotation);
            sprite.setOrigin(transform.x() + (spriteSize.x * scale.x / 2), transform.y() + (spriteSize.y * scale.y / 2));
            sprite.draw(batch);

            batch.setColor(Color.WHITE);
        }
    }

    @Override
    public void die() {
        texture.dispose();
    }

    public Texture getTexture() {
        return texture;
    }

    public SpriteBatch getBatch() {
        return (SpriteBatch) batch;
    }

    public Transform2D getTransform() {
        return transform;
    }

    public float getSpriteWidth() {
        return spriteSize.x*scale.x;
    }

    public float getSpriteHeight() {
        return spriteSize.y*scale.y;
    }

    public float getScaleX() {
        return scale.x;
    }

    public float getScaleY() {
        return scale.y;
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

    public void setTextures(Texture texture) {
        this.texture = texture;
    }

    public void setSpriteWidth(float spriteWidth) {
        this.spriteSize.x = spriteWidth;
    }

    public void setSpriteHeight(float spriteHeight) {
        this.spriteSize.y = spriteHeight;
    }

    public void setScaleX(float scaleX) {
        this.scale.x = scaleX;
    }

    public void setScaleY(float scaleY) {
        this.scale.y = scaleY;
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
        return new Rect(spriteSize.x, spriteSize.y);
    }
}
