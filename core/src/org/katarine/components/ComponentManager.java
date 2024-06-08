package org.katarine.components;

import org.katarine.logging.LogLevel;
import org.katarine.logging.Logger;
import org.katarine.objects.EntityManager;
import org.katarine.objects.GameObject;
import org.katarine.systems.System;
import org.katarine.systems.SystemManager;
import org.katarine.utils.structs.concurrent.ConcurrentClassUniqueHashSet;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class ComponentManager extends System {
    private volatile SystemManager systemManager;
    private volatile EntityManager entityManager;

    private boolean isCreated;

    private final ConcurrentHashMap<GameObject, ConcurrentClassUniqueHashSet<Component>> components = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<GameObject, ConcurrentClassUniqueHashSet<Component>> componentsCreation = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<GameObject, ConcurrentClassUniqueHashSet<Component>> componentsRemoval = new ConcurrentHashMap<>();

    private void ensureComponentCollectionExists(GameObject obj) {
        components.computeIfAbsent(obj, k -> new ConcurrentClassUniqueHashSet<>());
        componentsCreation.computeIfAbsent(obj, k -> new ConcurrentClassUniqueHashSet<>());
        componentsRemoval.computeIfAbsent(obj, k -> new ConcurrentClassUniqueHashSet<>());

    }

    public void updateComponentsOf(GameObject on) {
        ensureComponentCollectionExists(on);
        processPendingChanges(on);
        synchronized (components.get(on)) {
            components.get(on).forEach(Component::render);
        }
    }

    private synchronized void processPendingChanges(GameObject on) {
        componentsCreation.get(on).removeAll(componentsRemoval.computeIfAbsent(on, k -> new ConcurrentClassUniqueHashSet<>()));

        if (componentsCreation.containsKey(on)) {
            components.get(on).addAll(componentsCreation.get(on));
            componentsCreation.get(on).clear();
        }
        if (componentsRemoval.containsKey(on)) {
            components.get(on).removeAll(componentsRemoval.get(on));
            componentsRemoval.get(on).clear();
        }
    }

    public void initComponentsOf(GameObject on) {
        processPendingChanges(on);
        ensureComponentCollectionExists(on);
        synchronized (components.get(on)) {
            components.get(on).forEach(Component::init);
        }
    }

    public void createComponentsOf(GameObject on) {
        ensureComponentCollectionExists(on);
        synchronized (components.get(on)) {
            components.get(on).forEach(Component::create);
        }
        isCreated = true;
    }

    public void destroyComponentsOf(GameObject on) {
        ensureComponentCollectionExists(on);
        synchronized (components.get(on)) {
            components.get(on).forEach(Component::die);
        }
    }

    public void addComponents(GameObject obj, Collection<? extends Component> components) {
        components.forEach(c -> addComponent(obj, c));
    }

    public void addComponent(GameObject obj, Component component) {
        ensureComponentCollectionExists(obj);
        componentsCreation.get(obj).add(component);
        component.setSystemManager(this.systemManager);
        component.setEntityManager(this.entityManager);
        component.setGameObject(obj);
        component.init();
        new Logger(LogLevel.DEBUG).debug(component.getClass() + " " + isCreated);
        if (isCreated) component.create();
    }

    public <T extends Component> T removeComponent(GameObject obj, T component) {
        ensureComponentCollectionExists(obj);
        componentsRemoval.get(obj).add(component);
        component.dispose();
        return component;
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> T removeComponent(GameObject obj, Class<T> componentClass) {
        return removeComponent(obj, (T) components.get(obj).getByClass(componentClass));
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponentSubclass(GameObject obj, Class<?> superClass) {
        ensureComponentCollectionExists(obj);
        T ret = (T) components.get(obj).getBySuperclass(superClass);
        if (ret != null) return ret;

        synchronized (components.get(obj)) {
            for (var c : components.get(obj)) {
                if (superClass.isAssignableFrom(c.getClass())) return (T) c;
            }
            for (var c : componentsCreation.get(obj)) {
                if (superClass.isAssignableFrom(c.getClass())) return (T) c;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(GameObject obj, Class<T> componentClass) {
        ensureComponentCollectionExists(obj);
        T ret = (T) components.get(obj).getByClass(componentClass);
        if (ret != null) return ret;

        synchronized (components.get(obj)) {
            for (var c : components.get(obj)) {
                if (componentClass.isAssignableFrom(c.getClass())) return (T) c;
            }
            for (var c : componentsCreation.get(obj)) {
                if (componentClass.isAssignableFrom(c.getClass())) return (T) c;
            }
        }
        return null;
    }

    public Set<Component> getComponentsOf(GameObject gameObject) {
        ensureComponentCollectionExists(gameObject);
        return components.get(gameObject);
    }

    public void setSystemManager(SystemManager systemManager) {
        if (this.systemManager==null)
            this.systemManager = systemManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        if (this.entityManager==null)
            this.entityManager = entityManager;
    }
}