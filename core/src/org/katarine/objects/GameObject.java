package org.katarine.objects;

import com.badlogic.gdx.math.Vector2;
import org.katarine.components.Component;
import org.katarine.components.Transform2D;
import org.katarine.components.phyiscs.RigidBody2D;
import org.katarine.editor.Editor;
import org.katarine.editor.components.EditorObjectComponent;
import org.katarine.Game;
import org.katarine.annotations.EditorObject;
import org.katarine.annotations.NotInstantiatable;
import org.katarine.logging.Logger;
import org.katarine.utils.input.AbstractInputHandler;
import org.katarine.utils.structs.UniqueClassPriorityQueue;
import org.katarine.utils.structs.tree.Tree;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

@EditorObject
public class GameObject extends AbstractInputHandler {
    // game objects queued to be added in editor/game objects list
//    public static ArrayDeque<GameObject> Editor.gameObjectsCreationQueue = new ArrayDeque<>(32);
//    public static ArrayDeque<GameObject> destroyQueue = new ArrayDeque<>(32);
    private static Editor game;
    private static Editor editor;
    private static long lastId = 1;
    private long id;
    protected final UniqueClassPriorityQueue<Component> components = new UniqueClassPriorityQueue<>(new ComponentComparator());

    public Scene getScene() {
        return scene;
    }

    private static class ComponentComparator implements Comparator<Component> {
        @Override
        public int compare(Component o1, Component o2) {
            return Integer.compare(o1.updateOrder, o2.updateOrder);
        }
    }

    private final HashSet<Class<? extends Component>> componentsClasses = new HashSet<>();
    protected HashSet<GameObject> children = new HashSet<>();
    protected final Scene scene;
    protected final Tree<GameObject> tree = new Tree<>(this);
    protected GameObject parent;
    protected String name = toString();
    private boolean initialized = false;
    public Transform2D transform;
    public int updateOrder = 0; // where 0 is first

    public final Vector2 relativePosition = new Vector2();

    private static final Logger logger = new Logger(GameObject.class.getTypeName());

    protected GameObject() {
        if (Editor.getEditorInstance()!=null && Editor.getEditorInstance().isEditorMode())
            this.scene = Editor.getEditorInstance().getCurrentScene();
        else this.scene = Game.gameInstance.getCurrentScene();
    }

    /**
     * Called on instantiation of an object. Can be used for example to initialize components.
     */
    protected void awake() {}
    protected void start() {}
    protected void update() {}
    protected void die() {} //what to do on dispose

    public final void destroy() {
        for (Component c : components) {
            c.die();
        }
        if (Game.getEditorInstance() == null || Game.getEditorInstance().isEditorMode()) {
            die();
            // Editor.getEditorInstance().getEditorObjects().remove(this);
        } else if (Editor.getEditorInstance() == null || !Editor.getEditorInstance().isEditorMode()) {
            die();
            // Game.getEditorInstance().getGameObjects().remove(this);
        }
        Editor.gameObjectsDestroyQueue.addLast(this);
    }

    /**
     * Surely it is possible to use constructors, but this method is more safe because of check for {@link NotInstantiatable} annotation.
     * @param gameObjectClass class of game object to be instantiated
     * @return gameInstance of {@code <T extends GameObject>}
     * @param <T> type of game object
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

            if ((caller.equals(Editor.class) || Editor.editorInstance != null) && Editor.editorInstance.isEditorMode()) {
                EditorObjectComponent eoc = new EditorObjectComponent();
                eoc.gameObject = ret;
                ret.components.add(eoc);
            }

            ret.setId(lastId++);
            ret.init();

            Editor.gameObjectsCreationQueue.addLast(ret);

            return ret;
        } catch (NoSuchMethodException e) {
            return instantiate((Class<T>) gameObjectClass.getSuperclass());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Surely it is possible to use constructors, but this method is more safe because of check for {@link NotInstantiatable} annotation.
     * @param gameObjectClass class of game object to be instantiated
     * @param constructorParams parameters of available constructor of {@code Class<T> gameObjectClass}. Order sensitive
     * @return gameInstance of {@code <T extends GameObject>}
     * @param <T> type of game object
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

            if ((caller.equals(Editor.class) || Editor.editorInstance !=null) && Editor.editorInstance.isEditorMode()) {
                EditorObjectComponent eoc = new EditorObjectComponent();
                eoc.gameObject = ret;
                ret.components.add(eoc);
            }

            ret.setId(lastId);
            lastId++;
            ret.init();

            Editor.gameObjectsCreationQueue.addLast(ret);

            return ret;
        } catch (NoSuchMethodException e) {
            return instantiate((Class<T>) gameObjectClass.getSuperclass(), constructorParams);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Surely it is possible to use constructors, but this method is more safe because of check for {@link NotInstantiatable} annotation.
     * @param gameObjectClass class of game object to be instantiated
     * @param parent parent {@code GameObject}
     * @param constructorParams parameters of available constructor of {@code Class<T> gameObjectClass}. Order sensitive
     * @return gameInstance of {@code <T extends GameObject>}
     * @param <T> type of game object
     */
    public static <T extends GameObject> T instantiate(Class<T> gameObjectClass, GameObject parent, Object... constructorParams) {
        if (gameObjectClass.isAnnotationPresent(NotInstantiatable.class)) throw new RuntimeException("Cannot instantiate "+gameObjectClass+" because the class is marked as not instantiatable");
        T go = instantiate(gameObjectClass, constructorParams);
        parent.addChild(go);
        return go;
    }

    /**
     * Surely it is possible to use constructors, but this method is more safe because of check for {@link NotInstantiatable} annotation.
     * @param gameObjectClass class of game object to be instantiated
     * @param parent parent {@code GameObject}
     * @return gameInstance of {@code <T extends GameObject>}
     * @param <T> type of game object
     */
    public static <T extends GameObject> T instantiate(Class<T> gameObjectClass, GameObject parent) {
        if (gameObjectClass.isAnnotationPresent(NotInstantiatable.class)) throw new RuntimeException("Cannot instantiate "+gameObjectClass+" because the class is marked as not instantiatable");
        T go;
        logger.debug("parent: "+parent);
        parent.addChild(go = instantiate(gameObjectClass));
        return go;
    }

    public static <T extends GameObject> T instantiate(T gameObject) {
        Class<?> caller;
        if ((caller = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass())==Editor.class) { // если будет не эффективно то я удалю
            if (!gameObject.getClass().isAnnotationPresent(EditorObject.class))
                throw new RuntimeException("Cannot instantiate " + gameObject.getClass() + " in the editor because the class is not marked as editor object");
        }
        else {
            if (gameObject.getClass().isAnnotationPresent(NotInstantiatable.class))
                throw new RuntimeException("Cannot instantiate " + gameObject.getClass() + " because the class is marked as not instantiatable");
        }

        if (caller.equals(Editor.class) || Editor.editorInstance !=null) {
            EditorObjectComponent eoc = new EditorObjectComponent();
            eoc.gameObject = gameObject;
            gameObject.components.add(eoc);
        }

        gameObject.setId(lastId);
        lastId++;
        gameObject.init();

        Editor.gameObjectsCreationQueue.addLast(gameObject);

        return gameObject;
    }

    public static <T extends GameObject> T instantiate(T gameObject, GameObject parent) {
        parent.addChild(instantiate(gameObject));
        return gameObject;
    }

    /**
     * Game Object initializer.
     * @apiNote made public for using constructors being available.
     */
    public void init() {
        if (initialized) {
            return;
        }

        if (this.parent == null && !Scene.class.isAssignableFrom(this.getClass()))
            this.scene.addChild(this);

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
            c.create();
        }
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
//        if (this.parent != null) {
//            this.transform.parent = this.parent.transform;
//            this.transform.pos.set(parent.transform.pos.cpy());
//        } else {
////            this.transform.pos.set(relativePosition);
//            this.relativePosition.set(this.transform.pos);
//        }
        update();
        this.transform.update();
        for (Component c : components) {
            c.update();
        }
    }

    /**
     * calls {@link GameObject#die()} on this object and {@link Component#die()} on all of its components, and then actually "dies". That means this object will not be updated or rendered by Game class, and will not be accessible anymore.
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
    @SuppressWarnings("unchecked")
    public final <T extends Component> T addComponent(T component) {
        if (component.gameObject!=null) throw new RuntimeException("Cannot add component "+component.getClass().getName()+" to "+this.getClass().getName()+", because it is already attached to "+component.gameObject.getClass().getName());
        if (components.contains(component)) return (T) getComponent(component.getClass());
        if (componentsClasses.contains(component.getClass()))
            for (Component c : Editor.componentsCreationQueue)
                if (c.getClass() == component.getClass()) return (T) c;

        Editor.componentsCreationQueue.addLast(component);
        component.setGameObject(this);
        component.gameObject=this;
        componentsClasses.add(component.getClass());
        component.init();
        logger.debug("added component "+component.getClass().getName() + " to "+ component.gameObject.getClass().getName());
        return component;
    }

    public final void removeComponent(Component component) {
        removeComponent(component.getClass());
    }

    /**
     * Removes component from this object
     * @param componentClass component class
     */
    public final void removeComponent(Class<? extends Component> componentClass) {
        for (Component c : components) {
            if (c.getClass()==componentClass) {
                components.remove(c);
                componentsClasses.remove(c.getClass());
                c.gameObject = null;
                break;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public final <T extends Component> T getComponentExtender(Class<T> componentClass) throws NullPointerException {
        for (Component c : components) {
            if (componentClass.isAssignableFrom(c.getClass())) {
                return (T) c;
            }
        }
        throw new NullPointerException(this.getClass().getName() + " does not have "+componentClass.getName()+ " component");
    }

    @SuppressWarnings("unchecked")
    public final <T> T getExtendedComponent(Class<T> clazz) throws NullPointerException {
        for (Component c : components) {
            if (clazz.isAssignableFrom(c.getClass())) {
                return (T) c;
            }
        }
        throw new NullPointerException(this.getClass().getName() + " does not have "+clazz.getName()+ " component");
    }

    @SuppressWarnings("unchecked")
    public final <T extends Component> List<T> getComponentExtenders(Class<T> componentClass) throws NullPointerException {
        List<T> result = new ArrayList<>();
        for (Component c : components) {
            if (componentClass.isAssignableFrom(c.getClass())) {
                result.add(((T) c));
            }
        }
        if (result.isEmpty()) throw new NullPointerException(this.getClass().getName() + " does not have "+componentClass.getName()+ " component");
        return result;
    }

    @SuppressWarnings("unchecked")
    public final <T extends Component> T getComponent(Class<T> componentClass) throws NullPointerException {
        for (Component c : components) {
            if (c.getClass() == componentClass) {
                return (T) c;
            }
        }
        for (Component c : Editor.componentsCreationQueue) {
            if (c.getClass() == componentClass && c.gameObject == this) {
                return (T) c;
            }
        }

        throw new NullPointerException(this.getClass().getName() + " does not have "+componentClass.getName()+ " component");
    }

    @SuppressWarnings("unchecked")
    public final <T extends Component> List<T> getComponents(Class<T> componentClass) throws NullPointerException {
        List<T> result = new ArrayList<>();
        for (Component c : components) {
            if (c.getClass() == componentClass) {
                result.add((T) c);
            }
        }
        if (result.isEmpty()) throw new NullPointerException(this.getClass().getName() + " does not have "+componentClass.getName()+ " component");
        return result;
    }

    public final UniqueClassPriorityQueue<Component> getComponents() {
        return components;
    }

    final void setId(long id) {
        this.id = id;
    }

    public final long getId() {
        return id;
    }

    public static long getLastId() {
        return lastId;
    }

    public final String getName() {
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

    public final Tree<GameObject> toTree() {
        Tree<GameObject> result = new Tree<>(this);
        for (var c : children) {
            if (c.parent != this) continue;
            result.addChild(c.toTree());
        }
        return result;
    }
}