package io.github.whoisamyy.components;

import io.github.whoisamyy.objects.GameObject;
import io.github.whoisamyy.utils.input.AbstractInputHandler;

//abstract because there cannot be any empty component

public abstract class Component extends AbstractInputHandler {
    public GameObject gameObject;

    /**
     * Called on instantiation of an object. Can be used for example to initialize components.
     */
    public void awake() {}
    public void start() {}
    public void update() {}
    public void die() {} //what to do on dispose

    public final void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }
}
