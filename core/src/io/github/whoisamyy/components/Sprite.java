package io.github.whoisamyy.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.whoisamyy.editor.Editor;
import io.github.whoisamyy.katarine.Game;

import java.util.LinkedList;
import java.util.List;

public class Sprite extends DrawableComponent {
    LinkedList<Texture> textures = new LinkedList<>();
    SpriteBatch batch;
    /**
     * These values are set in units, NOT pixels. If you want to use pixels make sure that you do {@code px/Utils.PPU}
     * @see io.github.whoisamyy.utils.Utils
     */
    float spriteWidth, spriteHeight;
    float scaleX=1, scaleY=1, rotation=0;
    boolean flipX = false, flipY = false;

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
        this.textures.add(texture);
    }

    /**
     *
     * @param textures
     * @param spriteWidth sprite width in units
     * @param spriteHeight sprite height in units
     * @param scaleX scale multiplier in x
     * @param scaleY scale multiplier in y
     * @param rotation rotation in degrees
     * @param flipX
     * @param flipY
     */
    public Sprite(Texture[] textures, float spriteWidth, float spriteHeight, float scaleX, float scaleY, float rotation, boolean flipX, boolean flipY) {
        this.batch = Game.getInstance()==null ? Editor.getInstance().getBatch() : Game.getInstance().getBatch();
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.rotation = rotation;
        this.flipX = flipX;
        this.flipY = flipY;
        this.textures.addAll(List.of(textures));
    }

    public Sprite(Texture texture, float spriteWidth, float spriteHeight) {
        this.batch = Game.getInstance()==null ? Editor.getInstance().getBatch() : Game.getInstance().getBatch();
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.textures.add(texture);
    }

    public Sprite(SpriteBatch batch, Texture texture, float spriteWidth, float spriteHeight) {
        this.batch = batch;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.textures.add(texture);
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
        this.textures.add(texture);
    }

    public Sprite copy(int textureIndex) {
        return new Sprite(batch, new Texture(textures.get(textureIndex).getTextureData()), spriteWidth, spriteHeight, scaleX, scaleY, rotation, flipX, flipY);
    }

    public void addTexture(Texture texture) {
        textures.add(texture);
    }

    //removed start

    @Override
    protected void draw() {
        for (Texture texture : textures) {
            batch.draw(texture, transform.getPosX()-(spriteWidth/2), transform.getPosY()-(spriteHeight/2),
                    transform.getPosX(), transform.getPosY(),
                    spriteWidth, spriteHeight, scaleX, scaleY, rotation,
                    0, 0, texture.getWidth(), texture.getHeight(),
                    flipX, flipY);
        }
    }

    @Override
    public void die() {
        for (Texture texture : textures) {
            texture.dispose();
        }
    }

    public List<Texture> getTextures() {
        return textures;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public Transform2D getTransform() {
        return transform;
    }

    public float getSpriteWidth() {
        return spriteWidth;
    }

    public float getSpriteHeight() {
        return spriteHeight;
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
    public void setShow(boolean show) {
        this.show = show;
    }
    
    public void setTextures(LinkedList<Texture> textures) {
        this.textures = textures;
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
}
