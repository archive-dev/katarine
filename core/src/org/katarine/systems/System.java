package org.katarine.systems;

import org.katarine.core.KObject;

public abstract class System implements KObject {
    private volatile SystemManager systemManager;

    final void init() {
        awake();
        isAwaken = true;
    }

    final void create() {
        start();
        isCreated = true;
    }

    final void preRender() {
        preUpdate();
    }

    final void render() {
        update();
    }

    final void postRender() {
        postUpdate();
    }

    final void dispose() {
        die();
    }

    protected void awake() {}
    protected void start() {}
    protected void preUpdate() {}
    protected void update() {}
    protected void postUpdate() {}
    protected void die() {}

    public boolean isAwaken = false;
    public boolean isCreated = false;

    public final SystemManager getSystemManager() {
        return this.systemManager;
    }

    final void setSystemManager(SystemManager systemManager) {
        if (this.systemManager ==null)
            this.systemManager = systemManager;
    }
}
