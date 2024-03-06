package io.github.whoisamyy.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import io.github.whoisamyy.editor.Editor;
import io.github.whoisamyy.katarine.Game;
import io.github.whoisamyy.utils.math.shapes.Rect;
import io.github.whoisamyy.utils.render.RectOwner;

public class Sprite extends DrawableComponent implements RectOwner {
    Texture texture;
    /**
     * These values are set in units, NOT pixels. If you want to use pixels make sure that you do {@code px/Utils.PPU}
     * @see io.github.whoisamyy.utils.Utils
     */
    float spriteWidth, spriteHeight;
    float scaleX=1, scaleY=1, rotation=0;
    boolean flipX = false, flipY = false;

    com.badlogic.gdx.graphics.g2d.Sprite sprite;

    public final Vector2 relativePosition = new Vector2();

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
        this.batch = Game.getInstance()==null ? Editor.getInstance().getBatch() : Game.getInstance().getBatch();
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.rotation = rotation;
        this.flipX = flipX;
        this.flipY = flipY;
        this.texture = texture;
    }

    public Sprite(Texture texture, float spriteWidth, float spriteHeight) {
        this.batch = Game.getInstance()==null ? Editor.getInstance().getBatch() : Game.getInstance().getBatch();
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.texture = texture;
    }

    public Sprite(SpriteBatch batch, Texture texture, float spriteWidth, float spriteHeight) {
        this.batch = batch;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.texture = texture;
    }

    public Sprite(SpriteBatch batch, Texture texture, float spriteWidth, float spriteHeight, float scaleX, float scaleY, float rotation, boolean flipX, boolean flipY) {
        this.batch = batch;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.rotation = rotation;
        this.flipX = flipX;
        this.flipY = flipY;
        this.texture = texture;
    }

    public Sprite copy() {
        return new Sprite(batch, new Texture(texture.getTextureData()), spriteWidth, spriteHeight, scaleX, scaleY, rotation, flipX, flipY);
    }

    @Override
    public void awake() {
        sprite = new com.badlogic.gdx.graphics.g2d.Sprite(texture);
    }

    @Override
    protected void draw() {
        batch.setColor(sprite.getColor());
        batch.draw(sprite.getTexture(), transform.getPosX()-(spriteWidth/2)+relativePosition.x, transform.getPosY()-(spriteHeight/2)+relativePosition.y,
                transform.getPosX(), transform.getPosY(),
                spriteWidth, spriteHeight, scaleX, scaleY, rotation,
                0, 0, texture.getWidth(), texture.getHeight(),
                flipX, flipY);
        batch.setColor(Color.WHITE);
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

    public Transform2D getTransform() {
        return transform;
    }

    public float getSpriteWidth() {
        return spriteWidth*scaleX;
    }

    public float getSpriteHeight() {
        return spriteHeight*scaleY;
    }

    public float getScaleX() {
        return scaleX;
    }

    public float getScaleY() {
        return scaleY;
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
        this.spriteWidth = spriteWidth;
    }

    public void setSpriteHeight(float spriteHeight) {
        this.spriteHeight = spriteHeight;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
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
        return new Rect(spriteWidth, spriteHeight);
    }
}
