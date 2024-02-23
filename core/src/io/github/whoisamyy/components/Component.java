package io.github.whoisamyy.components;

import com.badlogic.gdx.math.Vector2;
import io.github.whoisamyy.logging.Logger;
import io.github.whoisamyy.objects.GameObject;
import io.github.whoisamyy.utils.input.AbstractInputHandler;

//abstract because there cannot be any empty component

public abstract class Component extends AbstractInputHandler {
    private boolean initialized = false;
    private boolean created = false;
    public GameObject gameObject;
    public Transform2D transform;
    public int updateOrder = 0; // where 0 is first
    protected final Logger logger = new Logger(this.getClass().getTypeName());

    public final void init() {
        if (initialized) return;
        // preAwake();
        this.transform = gameObject.transform;
        awake();
        initialized = true;
    }

    public final void preAwake() { //временно final
        if (transform==null) {
            if (!Transform2D.class.isAssignableFrom(getClass())) {
                transform = new Transform2D(new Vector2());
                gameObject.addComponent(transform);
                gameObject.transform = transform;
            }
        }
    }

    public final void create() {
        if (!created) {
            start();
            created = true;
        }
    }
    /**
     * Called on instantiation of an object. Can be used for example to initialize components.
     */
    public void awake() {}
    protected void start() {}
    public void update() {}
    public void die() {} //what to do on dispose

    public final void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public final boolean isInitialized() {
        return initialized;
    }
}
