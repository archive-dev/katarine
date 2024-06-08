package org.katarine.objects;

import com.badlogic.gdx.math.Vector2;
import org.katarine.annotations.Static;
import org.katarine.components.Component;
import org.katarine.components.ComponentManager;
import org.katarine.components.Transform2D;
import org.katarine.core.KObject;
import org.katarine.annotations.EditorObject;
import org.katarine.logging.LogLevel;
import org.katarine.logging.Logger;
import org.katarine.scenes.Scene;
import org.katarine.systems.SystemManager;
import org.katarine.utils.serialization.annotations.DontSerialize;

import java.util.*;

@EditorObject
@Static
public class GameObject implements KObject {
    private long id = -1; // uhhh now I am not sure if id should be long...

    private volatile ComponentManager componentManager;
    private volatile SystemManager systemManager;

    protected final HashSet<GameObject> children = new HashSet<>();
    @DontSerialize
    protected Scene scene;

    protected GameObject parent;
    protected String name;
    @DontSerialize
    private boolean initialized = false;
    public final Transform2D transform = new Transform2D();
    public int updateOrder = 0; // where 0 is first

    public final Vector2 relativePosition = new Vector2();

    @DontSerialize
    private static final Logger logger = new Logger(GameObject.class.getTypeName());

    public GameObject() {}

    /**
     * Called on instantiation of an object. Can be used, for example, to initialize components.
     */
    protected void awake() {}
    protected void start() {}
    protected void update() {}
    protected void die() {} //what to do on disposing

    final void destroy() {
        componentManager.destroyComponentsOf(this);
        die();
    }

    /**
     * Game Object initializer.
     * @apiNote made public for constructors being usable.
     */
    public void init() {
        logger.setLogLevel(LogLevel.DEBUG).debug(initialized);
        if (initialized) {
            return;
        }

        this.name = this.getClass().getSimpleName()+"("+id+")";

//        if (this.parent == null && !Scene.class.isAssignableFrom(this.getClass()))
//            this.scene.addChild(this);

        this.addComponent(this.transform);
        awake();
        this.componentManager.initComponentsOf(this);
        initialized = true;
    }

    /**
     * calls {@link GameObject#start()} on this object and its components
     */
    public void create() {
        if (!initialized) throw new IllegalStateException("Cannot create uninitialized GameObject");

        start();
        componentManager.createComponentsOf(this);
        logger.debug("Created gameObject "+this);
        if (this.parent!=null) {
            relativePosition.set(parent.transform.pos.cpy().sub(this.transform.pos));
        } else {
            this.relativePosition.set(transform.pos);
        }
    }

    /**
     * calls {@link GameObject#update()} on this object and its components
     */
    public void render() {
        update();
        this.transform.update();
        this.componentManager.updateComponentsOf(this);
    }

    /**
     * calls {@link GameObject#die()} on this object and {@link Component#dispose()} on all of its components,
     * and then actually "dies".
     * That means this object will not be updated or rendered by Game class, and will not be accessible anymore.
     */
    public void dispose() {
        die();
        destroy();
    }

    public final void addChild(GameObject child) {
        children.add(child);
        child.parent = this;
    }

    /**
     * Adds a component to the game object and initializes component ({@link Component#init()}).
     * @param component component to be added
     * @return added component
     * @param <T> type of component
     */
    public final <T extends Component> T addComponent(T component) {
        component.setGameObject(this);
        componentManager.addComponent(this, component);
        logger.debug("added component "+component.getClass().getName() + " to "+ component.getGameObject().getClass().getName());
        return component;
    }

    public final void removeComponent(Component component) {
        componentManager.removeComponent(this, component);
    }

    /**
     * Removes component from this object
     * @param componentClass component class
     */
    public final void removeComponent(Class<? extends Component> componentClass) {
        componentManager.removeComponent(this, componentClass);
    }

    public final <T extends Component> T getComponent(Class<T> componentClass) throws NullPointerException {
        return componentManager.getComponent(this, componentClass);
    }

    public final <T extends Component> T getComponentSubclass(Class<?> clazz) throws NullPointerException {
        return componentManager.getComponentSubclass(this, clazz);
    }

    public final Set<Component> getComponents() {
        return componentManager.getComponentsOf(this);
    }

    final void setId(long id) {
        this.id = id;
    }

    public final long getId() {
        return id;
    }

    public final String getName() {
        if (name==null || name.isEmpty() || name.isBlank()) return this.toString();
        return name;
    }

    public final HashSet<GameObject> getChildren() {
        return children;
    }

    public final GameObject getParent() {
        return parent;
    }

    public final Vector2 getRelativePosition() {
        return relativePosition;
    }

    public void setName(String name) {
        this.name = name;
    }

    public final int getUpdateOrder() {
        return updateOrder;
    }

    public final void setUpdateOrder(int updateOrder) {
        this.updateOrder = updateOrder;
    }

    public final ComponentManager getComponentManager() {
        return componentManager;
    }

    public final SystemManager getSystemManager() {
        return systemManager;
    }

    public final Scene getScene() {
        return scene;
    }

    final void setScene(Scene scene) {
        this.scene = scene;
    }

    @Override
    public HashMap<String, Object> getFields() {
        HashMap<String, Object> fields = KObject.super.getFields();
        fields.replace("parent", parent.toString());

        return fields;
    }

    final void setSystemManager(SystemManager systemManager) {
        this.systemManager = systemManager;
    }

    final void setComponentManager(ComponentManager componentManager) {
        if (componentManager!=null)
            this.componentManager = componentManager;
    }
}
