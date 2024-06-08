package org.katarine.components;

import com.badlogic.gdx.math.Vector2;
import org.katarine.core.KObject;
import org.katarine.logging.Logger;
import org.katarine.objects.EntityManager;
import org.katarine.objects.GameObject;
import org.katarine.systems.SystemManager;
import org.katarine.editor.imgui.HideInInspector;
import org.katarine.utils.serialization.annotations.DontSerialize;


public abstract class Component implements KObject {
    @DontSerialize
    private volatile boolean initialized = false;

    @DontSerialize
    private volatile boolean created = false;

    @HideInInspector
    @DontSerialize
    private volatile GameObject gameObject;

    @HideInInspector
    @DontSerialize
    private volatile Transform2D transform;

    public int updateOrder = 0; // where 0 is first

    @HideInInspector
    @DontSerialize
    protected final Logger logger = new Logger(this.getClass().getTypeName());

    @HideInInspector
    @DontSerialize
    private volatile SystemManager systemManager;

    @HideInInspector
    @DontSerialize
    private volatile EntityManager entityManager;

    public final void init() {
        if (initialized) return;
        // preAwake();
        this.transform = gameObject.transform;
        awake();
        initialized = true;
    }

    private void preAwake() {
        if (transform !=null && Transform2D.class.isAssignableFrom(getClass())) return;

        transform = new Transform2D(new Vector2());
//        gameObject.transform = transform;
    }

    public synchronized final void create() {
        if (!created) {
            start();
            created = true;
        }
    }

    public synchronized final void render() {
        update();
    }

    public synchronized final void dispose() {
        die();
    }

    /**
     * This method is called during the initialization of the component.
     * It can be overridden in subclasses
     * to perform any specific logic or setup required for the component when it is being initialized.
     */
    protected void awake() {}

    /**
     * Called when the component is created for the first time.
     * This method can be used to perform any initialization logic or setup required for the component.
     */
    protected void start() {}

    /**
     * This method is called on each frame to update the state of the component.
     * Subclasses can override this method to implement custom update logic for the component.
     */
    protected void update() {}

    /**
     * This method is called when the component needs to be disposed or destroyed.
     * Subclasses can override this method to implement any custom cleanup logic required for the component.
     */
    protected void die() {}

    public final synchronized void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public final GameObject getGameObject() {
        return gameObject;
    }

    public final boolean isInitialized() {
        return initialized;
    }

    public final Transform2D getTransform() {
        return transform;
    }

    void setSystemManager(SystemManager systemManager) {
        this.systemManager = systemManager;
    }

    final void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public final SystemManager getSystemManager() {
        return systemManager;
    }

    public final EntityManager getEntityManager() {
        return entityManager;
    }
}
