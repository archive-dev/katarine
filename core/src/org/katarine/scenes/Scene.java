package org.katarine.scenes;

import org.katarine.components.ComponentManager;
import org.katarine.core.KObject;
import org.katarine.objects.EntityManager;
import org.katarine.objects.GameObject;
import org.katarine.systems.System;
import org.katarine.systems.SystemManager;
import org.katarine.utils.structs.tree.Tree;

public final class Scene implements KObject {
    private volatile long id = -1L;
    private volatile String name = "Scene";

    private final EntityManager entityManager = new EntityManager();
    private final ComponentManager componentManager = new ComponentManager();
    private final SystemManager systemManager = new SystemManager();

    public void awake() {
        entityManager.setSystemManager(systemManager);
        entityManager.setComponentManager(componentManager);

        componentManager.setSystemManager(systemManager);
        componentManager.setEntityManager(entityManager);

        systemManager.registerSystem(entityManager);
        systemManager.registerSystem(componentManager);

        systemManager.awake();
    }

    public void start() {
        systemManager.start();
    }

    public void update() {
        systemManager.preUpdate();
        systemManager.update();
        systemManager.postUpdate();
    }

    public void die() {
        systemManager.die();
    }

    public <T extends System> T registerSystem(T system) {
        return systemManager.registerSystem(system);
    }

    public GameObject createGameObject() {
        return entityManager.instantiate(GameObject.class);
    }

    public <T extends GameObject> T createGameObject(Class<T> gameObjectClass) {
        return entityManager.instantiate(gameObjectClass);
    }

    public GameObject createGameObject(GameObject parent) {
        return entityManager.instantiate(GameObject.class, parent);
    }

    public <T extends GameObject> T createGameObject(Class<T> gameObjectClass, GameObject parent) {
        return entityManager.instantiate(gameObjectClass, parent);
    }

    public void destroyGameObject(GameObject gameObject) {
        entityManager.destroyGameObject(gameObject);
    }

    public void destroyGameObject(long id) {
        destroyGameObject(entityManager.getGameObject(id));
    }

    public void destroyGameObject(String name) {
        destroyGameObject(entityManager.getGameObject(name));
    }

    public Tree<GameObject> getGameObjectTree() {
        return entityManager.getGameObjectTree();
    }

    void setId(long value) {
        if (id!=-1L) return;
        this.id = value;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public ComponentManager getComponentManager() {
        return componentManager;
    }

    public SystemManager getSystemManager() {
        return systemManager;
    }
}
