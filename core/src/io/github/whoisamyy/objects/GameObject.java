package io.github.whoisamyy.objects;

import io.github.whoisamyy.components.Component;
import io.github.whoisamyy.test.Game;
import io.github.whoisamyy.utils.NotInstantiatable;
import io.github.whoisamyy.utils.input.AbstractInputHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Iterator;

public class GameObject extends AbstractInputHandler {
    private static long lastId = 1;
    private long id;
    protected HashSet<Component> components = new HashSet<>();
    private HashSet<Class<? extends Component>> componentsClasses = new HashSet<>();
    protected HashSet<GameObject> children = new HashSet<>();
    private boolean initialized = false;

    protected GameObject(){}

    /**
     * Called on instantiation of an object. Can be used for example to initialize components.
     */
    protected void awake() {}
    protected void start() {}
    protected void update() {}
    protected void die() {} //what to do on dispose


    /**
     * Surely it is possible to use constructors, but this method is more safe because of check for {@link NotInstantiatable} annotation.
     * @param gameObjectClass
     * @return instance of {@code <T extends GameObject>}
     * @param <T>
     */
    public static <T extends GameObject> T instantiate(Class<T> gameObjectClass) {
        if (gameObjectClass.isAnnotationPresent(NotInstantiatable.class)) throw new RuntimeException("Cannot instantiate "+gameObjectClass+" because the class is marked as not instantiatable");
        try {
            T ret = gameObjectClass.getDeclaredConstructor().newInstance();
            ret.setId(lastId);
            lastId++;
            ret.init();
            Game.instance.gameObjects.add(ret);
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
    public static <T extends GameObject> T instantiate(Class<T> gameObjectClass, Object... constructorParams) {
        if (gameObjectClass.isAnnotationPresent(NotInstantiatable.class)) throw new RuntimeException("Cannot instantiate "+gameObjectClass+" because the class is marked as not instantiatable");
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
            Game.instance.gameObjects.add(ret);
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
        parent.addChild(go = instantiate(gameObjectClass));
        return go;
    }

    /**
     * Game Object initializer.
     * @apiNote made public for using constructors being available.
     */
    public void init() {
        if (initialized) {
            //System.out.println("object already initialized");
            return;
        }
        awake();
        for (Component c : components) {
            c.awake();
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
            //System.out.println("Initialized component "+c.getClass().getName());
        }
        //System.out.println("Initialized GameObject"+this.getClass().getName());
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
     * calls {@link GameObject#die()} on this object and its components
     */
    public void dispose() {
        die();
        for (Component c : components) {
            c.die();
        }
    }

    public final void addChild(GameObject child) {
        children.add(child);
    }

    public Component addComponent(Component component) {
        components.add(component);
        component.setGameObject(this);
        component.gameObject=this;
        componentsClasses.add(component.getClass());
        component.awake();
        return component;
        //("Added component "+component.getClass().getName() + " to "+ component.gameObject.getClass().getName());
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
        return null;
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
