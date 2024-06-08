package org.katarine.scenes;

import org.katarine.systems.System;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class SceneManager extends System {
    private static final AtomicLong sceneLastID = new AtomicLong(0);

    private static final ConcurrentHashMap<Long, Scene> activeScenes = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Long, Scene> scenes = new ConcurrentHashMap<>();

    @Override
    protected void awake() {
        activeScenes.forEachValue(2L, Scene::awake);
    }

    @Override
    protected void start() {
        activeScenes.forEachValue(2L, Scene::start);
    }

    @Override
    protected synchronized void update() {
        activeScenes.forEachValue(2L, Scene::update);
    }

    @Override
    protected void die() {
        activeScenes.forEachValue(2L, Scene::die);
    }

    public final void init() {
        this.awake();
    }

    public final void create() {
        this.start();
    }

    public final void render() {
        this.preUpdate();
        this.update();
        this.postUpdate();
    }

    public final void dispose() {
        this.die();
    }

    public Scene addScene(Scene scene) {
        scene.setId(sceneLastID.getAndIncrement());
        scenes.put(scene.getId(), scene);
        return scene;
    }

    public Scene loadScene(Scene scene) {
        if (scenes.containsValue(scene)) {
            activeScenes.put(scene.getId(), scene);
            return scene;
        }
        return null;
    }

    public Scene createAndLoadScene() {
        return loadScene(new Scene());
    }

    public Scene getScene(long id) {
        return scenes.get(id);
    }

    public Set<Scene> getActiveScenes() {
        var ret = new HashSet<Scene>(2);
        ret.addAll(activeScenes.values());
        return ret;
    }

    public void unloadScene(Scene scene) {
        activeScenes.values().remove(scene);
    }

    public void removeScene(Scene scene) {
        scenes.values().remove(scene);
    }

    public void unloadScene(long id) {
        unloadScene(getScene(id));
    }
}
