package io.github.whoisamyy.objects;

import com.badlogic.gdx.math.Vector2;
import io.github.whoisamyy.components.Component;
import io.github.whoisamyy.components.Transform2D;
import io.github.whoisamyy.editor.Editor;
import io.github.whoisamyy.editor.components.EditorObjectComponent;
import io.github.whoisamyy.logging.Logger;
import io.github.whoisamyy.test.Game;
import io.github.whoisamyy.utils.EditorObject;
import io.github.whoisamyy.utils.NotInstantiatable;
import io.github.whoisamyy.utils.input.AbstractInputHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Iterator;

@EditorObject
public class GameObject extends AbstractInputHandler {
    private static Editor game;
    private static Editor editor;
    private static long lastId = 1;
    private long id;
    protected HashSet<Component> components = new HashSet<>();
    private HashSet<Class<? extends Component>> componentsClasses = new HashSet<>();
    protected HashSet<GameObject> children = new HashSet<>();
    private boolean initialized = false;
    public Transform2D transform;

    private static Logger logger = new Logger(GameObject.class.getTypeName());

    protected GameObject(){}

    /**
     * Called on instantiation of an object. Can be used for example to initialize components.
     */
    protected void awake() {}
    protected void start() {}
    protected void update() {}
    protected void die() {} //what to do on dispose

    public final void destroy() {
        if (game == null || game.isEditorMode()) {
            die();
            editor.getEditorObjects().remove(this);
        }
        if (editor == null || !editor.isEditorMode()) {
            die();
            game.getGameObjects().remove(this);
        }
    }

    /**
     * Surely it is possible to use constructors, but this method is more safe because of check for {@link NotInstantiatable} annotation.
     * @param gameObjectClass
     * @return instance of {@code <T extends GameObject>}
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    public static <T extends GameObject> T instantiate(Class<T> gameObjectClass) {
        Class<?> caller;
        if ((caller = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass())==Editor.class) { // если будет не эффективно то я удалю
            if (!gameObjectClass.isAnnotationPresent(EditorObject.class))
                throw new RuntimeException("Cannot instantiate " + gameObjectClass + " in the editor because the class is not marked as editor object");
        }
        else {
            if (gameObjectClass.isAnnotationPresent(NotInstantiatable.class))
                throw new RuntimeException("Cannot instantiate " + gameObjectClass + " because the class is marked as not instantiatable");
        }

        try {
            T ret = gameObjectClass.getDeclaredConstructor().newInstance();
            ret.setId(lastId);
            lastId++;
            ret.init();
            if (caller==Editor.class) {
                ret.addComponent(new EditorObjectComponent());
                Editor.instance.getEditorObjects().add(ret);
            }
            else
                Game.instance.getGameObjects().add(ret);
            return ret;
        } catch (NoSuchMethodException e) {
            return instantiate((Class<T>) gameObjectClass.getSuperclass());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Surely it is possible to use constructors, but this method is more safe because of check for {@link NotInstantiatable} annotation.
     * @param gameObjectClass
     * @param constructorParams parameters of available constructor of {@code Class<T> gameObjectClass}. Order sensitive
     * @return instance of {@code <T extends GameObject>}
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    public static <T extends GameObject> T instantiate(Class<T> gameObjectClass, Object... constructorParams) {
        Class<?> caller;
        if ((caller = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass())==Editor.class) { // если будет не эффективно то я удалю
            if (!gameObjectClass.isAnnotationPresent(EditorObject.class))
                throw new RuntimeException("Cannot instantiate " + gameObjectClass + " in the editor because the class is not marked as editor object");
        }
        else {
            if (gameObjectClass.isAnnotationPresent(NotInstantiatable.class))
                throw new RuntimeException("Cannot instantiate " + gameObjectClass + " because the class is marked as not instantiatable");
        }
        Class<?>[] paramsTypes = new Class<?>[constructorParams.length];
        for (int i = 0; i < constructorParams.length; i++) {
            Class<?> sc = constructorParams[i].getClass().getSuperclass();
            if (Modifier.isAbstract(sc.getModifiers())) {
                paramsTypes[i] = sc;
                continue;
            }
            paramsTypes[i] = constructorParams[i].getClass();
        }

        try {
            T ret = gameObjectClass.getDeclaredConstructor(paramsTypes).newInstance(constructorParams);
            ret.setId(lastId);
            lastId++;
            ret.init();
            if (caller==Editor.class) {
                ret.addComponent(new EditorObjectComponent());
                Editor.instance.getEditorObjects().add(ret);
            }
            else
                Game.instance.getGameObjects().add(ret);
            return ret;
        } catch (NoSuchMethodException e) {
            return instantiate((Class<T>) gameObjectClass.getSuperclass(), constructorParams);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Surely it is possible to use constructors, but this method is more safe because of check for {@link NotInstantiatable} annotation.
     * @param gameObjectClass
     * @param parent parent {@code GameObject}
     * @param constructorParams parameters of available constructor of {@code Class<T> gameObjectClass}. Order sensitive
     * @return instance of {@code <T extends GameObject>}
     * @param <T>
     */
    public static <T extends GameObject> T instantiate(Class<T> gameObjectClass, GameObject parent, Object... constructorParams) {
        if (gameObjectClass.isAnnotationPresent(NotInstantiatable.class)) throw new RuntimeException("Cannot instantiate "+gameObjectClass+" because the class is marked as not instantiatable");
        T go = instantiate(gameObjectClass, constructorParams);
        parent.addChild(go);
        return go;
    }

    /**
     * Surely it is possible to use constructors, but this method is more safe because of check for {@link NotInstantiatable} annotation.
     * @param gameObjectClass
     * @param parent parent {@code GameObject}
     * @return instance of {@code <T extends GameObject>}
     * @param <T>
     */
    public static <T extends GameObject> T instantiate(Class<T> gameObjectClass, GameObject parent) {
        if (gameObjectClass.isAnnotationPresent(NotInstantiatable.class)) throw new RuntimeException("Cannot instantiate "+gameObjectClass+" because the class is marked as not instantiatable");
        T go;
        logger.debug("parent: "+parent);
        parent.addChild(go = instantiate(gameObjectClass));
        return go;
    }

    /**
     * Game Object initializer.
     * @apiNote made public for using constructors being available.
     */
    public void init() {
        if (initialized) {
            return;
        }
        this.transform = new Transform2D(new Vector2(0, 0));
        awake();
        for (Component c : components) {
            // c.transform = this.transform;
            if (!c.isInitialized())
                c.init();
        }
        initialized = true;
    }

    /**
     * calls {@link GameObject#start()} on this object and its components
     */
    public void create() {
        if (!initialized) throw new IllegalStateException("Cannot create uninitialized GameObject");
        start();
        for (Component c : components) {
            c.start();
        }
        logger.debug("Created gameObject "+this);
    }

    /**
     * calls {@link GameObject#update()} on this object and its components
     */
    public void render() {
        update();
        for (Component c : components) {
            c.update();
        }
    }

    /**
     * calls {@link GameObject#die()} on this object and its components, and then actually "dies". That means this object will not be updated or rendered by Game class, and will not be accessible anymore.
     */
    public void dispose() {
        die();
        for (Component c : components) {
            c.die();
        }
        destroy();
    }

    public final void addChild(GameObject child) {
        children.add(child);
    }

    @SuppressWarnings("unchecked")
    public final <T extends Component> T addComponent(T component) {
        if (components.contains(component) || componentsClasses.contains(component.getClass())) return (T) getComponent(component.getClass());
        components.add(component);
        component.setGameObject(this);
        component.gameObject=this;
        componentsClasses.add(component.getClass());
        if (!component.isInitialized())
            component.init();
        logger.debug("added component "+component.getClass().getName() + " to "+ component.gameObject.getClass().getName());
        return component;
    }

    public final boolean removeComponent(Component component) {
        return removeComponent(component.getClass());
    }

    public final boolean removeComponent(Class<? extends Component> componentClass) {
        for (Iterator<Component> itr = components.iterator(); itr.hasNext();) {
            if (itr.next().getClass()==componentClass) {
                itr.remove();
                componentsClasses.remove(componentClass);
                return true;
            }
        }
        return false;
    }

    public final <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component c : components) {
            if (c.getClass() == componentClass) {
                return (T) c;
            }
        }
        throw new NullPointerException(this.getClass().getName() + " does not have "+componentClass.getName()+ " component");
    }

    public final HashSet<Component> getComponents() {
        return components;
    }

    void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public static long getLastId() {
        return lastId;
    }

    //@Override
    //public void write(Json json) {
    //    System.out.println("cerf");
    //    Field[] fields = this.getClass().getFields();
    //    for (Field f : fields) {
    //        try {
    //            json.writeValue(f.getName(), f.get(this), f.getType());
    //        } catch (IllegalAccessException e) {
    //            throw new RuntimeException(e);
    //        }
    //    }
    //}
//
    //@Override
    //public void read(Json json, JsonValue jsonData) {
    //    //this.id = jsonData.child().asLong();
    //}
}