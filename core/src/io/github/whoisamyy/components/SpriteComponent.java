package io.github.whoisamyy.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.whoisamyy.test.Game;

public class SpriteComponent extends Component {
    Texture texture;
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
        this.texture = texture;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.rotation = rotation;
        this.flipX = flipX;
        this.flipY = flipY;
    }

    public SpriteComponent(Texture texture, float spriteWidth, float spriteHeight) {
        this.batch = Game.instance.getBatch();
        this.texture = texture;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
    }

    public SpriteComponent(SpriteBatch batch, Texture texture, float spriteWidth, float spriteHeight) {
        this.texture = texture;
        this.batch = batch;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
    }

    public SpriteComponent(SpriteBatch batch, Texture texture, float spriteWidth, float spriteHeight, float scaleX, float scaleY, float rotation, boolean flipX, boolean flipY) {
        this.texture = texture;
        this.batch = batch;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.rotation = rotation;
        this.flipX = flipX;
        this.flipY = flipY;
    }

    @Override
    public void start() {
        if ((transform = gameObject.getComponent(Transform2D.class))==null) throw new RuntimeException(new NullPointerException());
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
        
        batch.draw(texture, transform.getPosX()-(spriteWidth/2), transform.getPosY()-(spriteHeight/2),
                transform.getPosX(), transform.getPosY(),
                spriteWidth, spriteHeight, scaleX, scaleY, rotation,
                0, 0, texture.getWidth(), texture.getHeight(),
                flipX, flipY); //monstr
    }

    @Override
    public void die() {
        texture.dispose();
    }
}
