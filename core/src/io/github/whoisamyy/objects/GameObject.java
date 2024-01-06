package io.github.whoisamyy.objects;

import com.badlogic.gdx.math.Vector2;
import io.github.whoisamyy.components.Component;
import io.github.whoisamyy.utils.NotInstantiatable;
import io.github.whoisamyy.utils.input.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;

public class GameObject {
    private static int pressedKey = -1;
    private static int unpressedKey = -1;
    private static char typedChar = Character.MIN_VALUE;
    private static int pressedButton = -1;
    private static int unpressedButton = -1;
    private static MouseClickEvent mouseClickEvent;
    private static long lastId = 1;
    private long id;
    protected HashSet<Component> components = new HashSet<>();
    protected HashSet<GameObject> children = new HashSet<>();
    protected boolean initialized = false;

    protected GameObject(){}

    protected void start() {}
    protected void update() {}
    protected void die() {} //what to do on dispose

    public static <T extends GameObject> T instantiate(Class<T> gameObjectClass, GameObject parent) {
        if (gameObjectClass.isAnnotationPresent(NotInstantiatable.class)) throw new RuntimeException("Cannot instantiate "+gameObjectClass+" because the class is marked as not instantiatable");
        T go;
        parent.addChild(go = instantiate(gameObjectClass));
        return go;
    }

    public static <T extends GameObject> T instantiate(Class<T> gameObjectClass) {
        if (gameObjectClass.isAnnotationPresent(NotInstantiatable.class)) throw new RuntimeException("Cannot instantiate "+gameObjectClass+" because the class is marked as not instantiatable");
        try {
            //go.init();
            T ret = gameObjectClass.getDeclaredConstructor().newInstance();
            ret.setId(lastId);
            lastId++;
            return ret;
        } catch (NoSuchMethodException e) {
            return instantiate((Class<T>) gameObjectClass.getSuperclass());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

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
            //go.init();
            T ret = gameObjectClass.getDeclaredConstructor(paramsTypes).newInstance(constructorParams);
            ret.setId(lastId);
            lastId++;
            return ret;
        } catch (NoSuchMethodException e) {
            return instantiate((Class<T>) gameObjectClass.getSuperclass(), constructorParams);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends GameObject> T instantiate(Class<T> gameObjectClass, GameObject parent, Object... constructorParams) {
        if (gameObjectClass.isAnnotationPresent(NotInstantiatable.class)) throw new RuntimeException("Cannot instantiate "+gameObjectClass+" because the class is marked as not instantiatable");
        T go = instantiate(gameObjectClass, constructorParams);
        parent.addChild(go);
        return go;
    }

    protected final boolean getKeyDown(int expectedKeyCode) {
        return expectedKeyCode==pressedKey && pressedKey!=-1;
    }


    protected final boolean getCharTyped(char expectedChar) {
        boolean ret = expectedChar==typedChar && typedChar!=Character.MIN_VALUE;
        typedChar = Character.MIN_VALUE;
        return ret;
    }

    protected final boolean getKeyUp(int expectedKeyCode) {
        return expectedKeyCode==unpressedKey && unpressedKey!=-1;
    }
    protected final void onGetKeyDown(int keyCode, OnKeyPressedActionExecutor executor) {
        if (getKeyDown(keyCode)) executor.onKeyDown(getPressedKey());
    }

    protected final void onGetKeyUp(int keyCode, OnKeyUpActionExecutor executor) {
        if (getKeyUp(keyCode)) executor.onKeyUp(getPressedKey());
    }

    protected final void onCharTyped(char charTyped, OnCharTypedActionExecutor executor) {
        boolean ret = charTyped==typedChar && typedChar!=Character.MIN_VALUE;
        if (ret) executor.onCharTyped(typedChar);
        typedChar = Character.MIN_VALUE;
    }

    protected static int getPressedKey() {
        return pressedKey;
    }

    protected static int getUnpressedKey() {
        return unpressedKey;
    }

    protected static char getTypedChar() {
        return typedChar;
    }

    protected static boolean getMouseDown(int mouseButton) {
        boolean ret = mouseButton==pressedButton && pressedButton!=-1;
        pressedButton = -1;
        return ret;
    }

    protected static boolean getMouseUp(int mouseButton) {
        boolean ret = mouseButton==unpressedButton && unpressedButton!=-1;
        unpressedButton = -1;
        return ret;
    }

    protected static int getPressedButton() {
        int ret = pressedButton;
        pressedButton = -1;
        return ret;
    }

    protected static int getUnpressedButton() {
        int ret = unpressedButton;
        unpressedButton = -1;
        return ret;
    }

    protected static Vector2 getMouseClickPosition() {
        Vector2 ret = null;
        if (mouseClickEvent != null && Objects.equals(mouseClickEvent.isButtonPressed(), true)) {
            ret = new Vector2(mouseClickEvent.getMouseX(), mouseClickEvent.getMouseY());
            mouseClickEvent = null;
        }
        return ret;
    }

    protected static Vector2 getMouseUnClickPosition() {
        Vector2 ret = null;
        if (mouseClickEvent != null && Objects.equals(mouseClickEvent.isButtonPressed(), false)) {
            ret = new Vector2(mouseClickEvent.getMouseX(), mouseClickEvent.getMouseY());
            mouseClickEvent = null;
        }
        return ret;
    }

    protected static void onMouseClick(MouseEventHandler handler) {
        if (mouseClickEvent!=null && Objects.equals(mouseClickEvent.isButtonPressed(), true)) handler.handle(mouseClickEvent);
        mouseClickEvent = null;
    }

    protected static void onMouseUnClick(MouseEventHandler handler) {
        if (mouseClickEvent!=null && Objects.equals(mouseClickEvent.isButtonPressed(), false)) handler.handle(mouseClickEvent);
        mouseClickEvent = null;
    }

    protected static void onMouseDrag(MouseEventHandler handler) {
        if (mouseClickEvent!=null && Objects.equals(mouseClickEvent.isDrag(), true))
            handler.handle(mouseClickEvent);
        mouseClickEvent = null;
    }

    protected static void onMouseScroll(MouseEventHandler handler) {
        if (mouseClickEvent!=null && Objects.equals(mouseClickEvent.isScroll(), true)) {
            handler.handle(mouseClickEvent);
        }
        mouseClickEvent = null;
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

    void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
