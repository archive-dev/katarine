package org.katarine.components;

import com.badlogic.gdx.math.Vector2;
import org.katarine.logging.Logger;
import org.katarine.objects.GameObject;
import org.katarine.utils.input.AbstractInputHandler;
import org.katarine.utils.serialization.annotations.HideInInspector;

import java.util.UUID;

//abstract because there cannot be any empty component

public abstract class Component extends AbstractInputHandler {
    @HideInInspector
    public final UUID id = UUID.randomUUID();
    private boolean initialized = false;
    private boolean created = false;
    @HideInInspector
    public GameObject gameObject;
    @HideInInspector
    public Transform2D transform;
    public int updateOrder = 0; // where 0 is first
    @HideInInspector
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
