package io.github.whoisamyy.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.whoisamyy.test.Game;

import java.util.LinkedList;
import java.util.List;

public class SpriteComponent extends Component {
    LinkedList<Texture> textures = new LinkedList<>();
    SpriteBatch batch;
    Transform2D transform;
    /**
     * These values are set in pixels. NOT meters. If you want to use meters make sure that you do {@code px/Utils.PPM}
     * @see io.github.whoisamyy.utils.Utils
     */
    float spriteWidth, spriteHeight;
    float scaleX=1, scaleY=1, rotation=0;
    boolean flipX = false, flipY = false;

    public SpriteComponent(Texture texture, float spriteWidth, float spriteHeight, float scaleX, float scaleY, float rotation, boolean flipX, boolean flipY) {
        this.batch = Game.instance.getBatch();
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.rotation = rotation;
        this.flipX = flipX;
        this.flipY = flipY;
        this.textures.add(texture);
    }

    public SpriteComponent(Texture texture, float spriteWidth, float spriteHeight) {
        this.batch = Game.instance.getBatch();
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.textures.add(texture);
    }

    public SpriteComponent(SpriteBatch batch, Texture texture, float spriteWidth, float spriteHeight) {
        this.batch = batch;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.textures.add(texture);
    }

    public SpriteComponent(SpriteBatch batch, Texture texture, float spriteWidth, float spriteHeight, float scaleX, float scaleY, float rotation, boolean flipX, boolean flipY) {
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

    public SpriteComponent copy(int textureIndex) {
        return new SpriteComponent(batch, new Texture(textures.get(textureIndex).getTextureData()), spriteWidth, spriteHeight, scaleX, scaleY, rotation, flipX, flipY);
    }

    public void addTexture(Texture texture) {
        textures.add(texture);
    }

    @Override
    public void start() {
        transform = gameObject.getComponent(Transform2D.class);
    }

    @Override
    public void update() {
        //draw(Texture texture, float x, float y,
        //float originX, float originY, float width, float height,
        //float scaleX, float scaleY, float rotation,
        //int srcX, int srcY, int srcWidth, int srcHeight,
        //boolean flipX, boolean flipY)

        //System.out.println(texture+", "+ transform.posX+", "+ transform.posY+", "+ transform.posX+", "+ transform.posY+", "+
        //        spriteWidth+", "+ spriteHeight+", "+ scaleX+", "+ scaleY+", "+ rotation+", "+
        //        0+", "+ 0+", "+ texture.getWidth()+", "+ texture.getHeight()+", "+
        //        flipX+", "+ flipY);
        for (Texture texture : textures) {
            batch.draw(texture, transform.getPosX()-(spriteWidth/2), transform.getPosY()-(spriteHeight/2),
                    transform.getPosX(), transform.getPosY(),
                    spriteWidth, spriteHeight, scaleX, scaleY, rotation,
                    0, 0, texture.getWidth(), texture.getHeight(),
                    flipX, flipY);
        }
        //batch.draw(texture, transform.getPosX()-(spriteWidth/2), transform.getPosY()-(spriteHeight/2),
        //        transform.getPosX(), transform.getPosY(),
        //        spriteWidth, spriteHeight, scaleX, scaleY, rotation,
        //        0, 0, texture.getWidth(), texture.getHeight(),
        //        flipX, flipY); //monstr
    }

    @Override
    public void die() {
        for (Texture texture : textures) {
            texture.dispose();
        }
    }

    public List<Texture> getTexture() {
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
