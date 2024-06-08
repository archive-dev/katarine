package org.katarine.objects;

import org.katarine.annotations.EditorObject;
import org.katarine.annotations.NotInstantiatable;
import org.katarine.components.ComponentManager;
import org.katarine.editor.Editor;
import org.katarine.editor.EditorObjectComponent;
import org.katarine.editor.systems.EditorSystem;
import org.katarine.systems.System;
import org.katarine.systems.SystemManager;
import org.katarine.utils.structs.tree.Tree;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicLong;

public final class EntityManager extends System {
    private final AtomicLong lastGameObjectID = new AtomicLong(0);

    private volatile SystemManager systemManager;
    private volatile ComponentManager componentManager;

    private final Tree<GameObject> gameObjectTree = new Tree<>();

    private final ConcurrentHashMap<Long, GameObject> gameObjects = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> gameObjectIDS = new ConcurrentHashMap<>();
    private final ConcurrentSkipListSet<GameObject> gameObjectCreation = new ConcurrentSkipListSet<>(Comparator.comparingInt(Object::hashCode));
    private final ConcurrentSkipListSet<GameObject> gameObjectDestruction = new ConcurrentSkipListSet<>(Comparator.comparingInt(Object::hashCode));

    /**
     * Surely it is possible to use constructors,
     * but this method is safer because of check for {@link NotInstantiatable} annotation.
     * @param gameObjectClass class of a game object to be instantiated
     * @param constructorParams parameters of available constructor of {@code Class<T> gameObjectClass}. Order sensitive
     * @return gameInstance of {@code <T extends GameObject>}
     * @param <T> type of game object
     */
    @SuppressWarnings("unchecked")
    public <T extends GameObject> T instantiate(Class<T> gameObjectClass, Object... constructorParams) {
        Class<?> caller = checkCaller(gameObjectClass);

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
            ret.setSystemManager(getSystemManager());
            ret.setComponentManager(this.componentManager);

            getSystemManager().getSystem(EntityManager.class).addGameObject(ret);

            if (getSystemManager().getSystem(EditorSystem.class)!=null) {
                EditorObjectComponent eoc = new EditorObjectComponent();
                eoc.setGameObject(ret);
                ret.addComponent(eoc);
            }

            ret.init();
            return ret;
        } catch (NoSuchMethodException e) {
            return instantiate((Class<T>) gameObjectClass.getSuperclass(), constructorParams);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Surely it is possible to use constructors,
     * but this method is safer because of check for {@link NotInstantiatable} annotation.
     * @param gameObjectClass class of a game object to be instantiated
     * @return gameInstance of {@code <T extends GameObject>}
     * @param <T> type of game object
     */
    @SuppressWarnings("unchecked")
    public <T extends GameObject> T instantiate(Class<T> gameObjectClass) {
        Class<?> caller = checkCaller(gameObjectClass);

        try {
            T ret = gameObjectClass.getDeclaredConstructor().newInstance();
            ret.setSystemManager(getSystemManager());
            ret.setComponentManager(this.componentManager);

            addGameObject(ret);

            if (getSystemManager().getSystem(EditorSystem.class)!=null) {
                EditorObjectComponent eoc = new EditorObjectComponent();
                eoc.setGameObject(ret);
                ret.addComponent(eoc);
            }

            ret.init();
            return ret;
        } catch (NoSuchMethodException e) {
            return instantiate((Class<T>) gameObjectClass.getSuperclass());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Surely it is possible to use constructors,
     * but this method is safer because of check for {@link NotInstantiatable} annotation.
     * @param gameObjectClass class of a game object to be instantiated
     * @param parent parent {@code GameObject}
     * @param constructorParams parameters of available constructor of {@code Class<T> gameObjectClass}. Order sensitive
     * @return gameInstance of {@code <T extends GameObject>}
     * @param <T> type of game object
     */
    public <T extends GameObject> T instantiate(Class<T> gameObjectClass, GameObject parent, Object... constructorParams) {
        if (gameObjectClass.isAnnotationPresent(NotInstantiatable.class)) throw new RuntimeException("Cannot instantiate "+gameObjectClass+" because the class is marked as not instantiatable");
        T go = instantiate(gameObjectClass, constructorParams);
        parent.addChild(go);
        return go;
    }

    /**
     * Surely it is possible to use constructors,
     * but this method is safer because of check for {@link NotInstantiatable} annotation.
     * @param gameObjectClass class of a game object to be instantiated
     * @param parent parent {@code GameObject}
     * @return gameInstance of {@code <T extends GameObject>}
     * @param <T> type of game object
     */
    public <T extends GameObject> T instantiate(Class<T> gameObjectClass, GameObject parent) {
        if (gameObjectClass.isAnnotationPresent(NotInstantiatable.class)) throw new RuntimeException("Cannot instantiate "+gameObjectClass+" because the class is marked as not instantiatable");
        T go;
        parent.addChild(go = instantiate(gameObjectClass));
        return go;
    }

    public <T extends GameObject> T instantiate(T gameObject) {
        Class<?> caller = checkCaller(gameObject.getClass());

        if (caller.equals(Editor.class) || Editor.editorInstance !=null) {
            EditorObjectComponent eoc = new EditorObjectComponent();
            eoc.setGameObject(gameObject);
            gameObject.addComponent(eoc);
        }

        gameObject.init();
        return gameObject;
    }

    public <T extends GameObject> T instantiate(T gameObject, GameObject parent) {
        parent.addChild(instantiate(gameObject));
        return gameObject;
    }

    private static Class<?> checkCaller(Class<? extends GameObject> goClass) {
        Class<?> caller;
        if ((caller = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass())== Editor.class) {
            if (!goClass.isAnnotationPresent(EditorObject.class))
                throw new RuntimeException("Cannot instantiate " + goClass.getSimpleName() + " in the editor because the class is not marked as editor object");
        }
        else {
            if (goClass.isAnnotationPresent(NotInstantiatable.class))
                throw new RuntimeException("Cannot instantiate " + goClass.getSimpleName() + " because the class is marked as not instantiatable");
        }
        return caller;
    }

    @Override
    protected void update() {
        updateGameObjectIds();
        updateGameObjects();
    }

    @Override
    protected void start() {
        removeDestroyedGameObjects();
        addCreatedGameObjects();
        gameObjects.forEachValue(32L, GameObject::create);
    }

    @Override
    protected void awake() {
        removeDestroyedGameObjects();
        addCreatedGameObjects();
        gameObjects.forEachValue(32L, GameObject::init);
    }

    @Override
    protected void preUpdate() {
        super.preUpdate();
    }

    @Override
    protected void postUpdate() {
        super.postUpdate();
    }

    @Override
    protected void die() {
        removeDestroyedGameObjects();
        addCreatedGameObjects();
        gameObjects.forEachValue(32L, GameObject::dispose);
    }

    private void updateGameObjectIds() {
        gameObjects.forEachValue(32L, go -> gameObjectIDS.put(go.getName(), go.getId()));
    }

    public synchronized void updateGameObjects() {
        removeDestroyedGameObjects();
        addCreatedGameObjects();
        renderAllGameObjects();
    }

    private void removeDestroyedGameObjects() {
        synchronized (gameObjectDestruction) {
            gameObjects.values().removeAll(gameObjectDestruction);
            gameObjectDestruction.clear();
        }
    }

    private void addCreatedGameObjects() {
        synchronized (gameObjectCreation) {
            gameObjectCreation.forEach(go -> gameObjects.put(go.getId(), go));
            gameObjectCreation.clear();
        }
    }

    private void renderAllGameObjects() {
        gameObjects.forEachValue(32L, GameObject::render);
    }

    public GameObject getGameObject(String name) {
        Long id = gameObjectIDS.get(name);
        if (id != null) {
            return gameObjects.get(id);
        }

        synchronized (gameObjectCreation) {
            for (var go : gameObjectCreation) {
                if (go.getName().equals(name)) {
                    return go;
                }
            }
        }

        return null;
    }

    public GameObject getGameObject(long id) {
        return gameObjects.get(id);
    }

    public <T extends GameObject> T addGameObject(T gameObject) {
        gameObject.setId(lastGameObjectID.getAndIncrement());
        gameObject.setSystemManager(this.systemManager);
        gameObject.setComponentManager(this.componentManager);

        if (gameObjects.containsValue(gameObject)) {
            throw new IllegalStateException("There is already a GameObject with id " + gameObject.getId());
        }

        gameObjectCreation.add(gameObject);

        if (gameObject.getParent()!=null)
            gameObjectTree.findNode(gameObject.getParent()).addChild(gameObject);
        else gameObjectTree.addChild(gameObject);

        return gameObject;
    }

    public void destroyGameObject(GameObject gameObject) {
        gameObjectDestruction.add(gameObject);
        gameObjectTree.removeElement(gameObjectTree.findNode(gameObject));
    }

    public Tree<GameObject> getGameObjectTree() {
        return gameObjectTree;
    }

    public void setSystemManager(SystemManager systemManager) {
        if (this.systemManager==null)
            this.systemManager = systemManager;
    }

    public void setComponentManager(ComponentManager componentManager) {
        if (this.componentManager==null)
            this.componentManager = componentManager;
    }
}