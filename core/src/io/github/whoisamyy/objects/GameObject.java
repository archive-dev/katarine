package io.github.whoisamyy.objects;

import io.github.whoisamyy.components.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Iterator;

public abstract class GameObject {
    private long id;
    private static long lastId = 1;
    protected HashSet<Component> components = new HashSet<>();
    protected HashSet<GameObject> children = new HashSet<>();
    protected boolean initialized = false;

    GameObject(){}

    protected abstract void start();
    protected abstract void update();
    protected abstract void die(); //what to do on dispose

    public static <T extends GameObject> T instantiate(Class<T> gameObjectClass, GameObject parent) {
        T go;
        parent.addChild(go = instantiate(gameObjectClass));
        return go;
    }

    public static <T extends GameObject> T instantiate(Class<T> gameObjectClass) {
        try {
            //go.init();
            T ret = gameObjectClass.getDeclaredConstructor().newInstance();
            ret.setId(lastId);
            lastId++;
            return ret;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends GameObject> T instantiate(Class<T> gameObjectClass, Object... constructorParams) {
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
            //go.init();
            T ret = gameObjectClass.getDeclaredConstructor(paramsTypes).newInstance(constructorParams);
            ret.setId(lastId);
            lastId++;
            return ret;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends GameObject> T instantiate(Class<T> gameObjectClass, GameObject parent, Object... constructorParams) {
        T go = instantiate(gameObjectClass, constructorParams);
        parent.addChild(go);
        return go;
    }

    /**
     * calls {@link GameObject#start()} on this object and its components
     */
    public void init() {
        if (initialized) return;
        start();
        for (Component c : components) {
            c.start();
            //System.out.println("Initialized component "+c.getClass().getName());
        }
        //System.out.println("Initialized GameObject"+this.getClass().getName());
        initialized = true;
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

    public void addComponent(Component component) {
        components.add(component);
        component.setGameObject(this);
        component.gameObject=this;
        //("Added component "+component.getClass().getName() + " to "+ component.gameObject.getClass().getName());
    }

    public final boolean removeComponent(Component component) {
        return removeComponent(component.getClass());
    }

    public final boolean removeComponent(Class<? extends Component> componentClass) {
        for (Iterator<Component> itr = components.iterator(); itr.hasNext();) {
            if (itr.next().getClass()==componentClass) {
                itr.remove();
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

    protected void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
