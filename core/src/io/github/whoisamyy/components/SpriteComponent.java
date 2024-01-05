package io.github.whoisamyy.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class SpriteComponent extends Component {
    Texture texture;
    SpriteBatch batch;

    public SpriteComponent(SpriteBatch batch, Texture texture) {
        this.batch = batch;
        this.texture = texture;
    }

    @Override
    public void start() {
        if (gameObject.getComponent(Transform2D.class)==null) System.out.println("null!!");
    }

    @Override
    public void update() {
        Vector2 pos = gameObject.getComponent(Transform2D.class).transform.getPosition();
        batch.draw(this.texture, pos.x- (float) this.texture.getWidth() /2, pos.y - (float) this.texture.getHeight() /2);
    }

    @Override
    public void die() {

    }
}
