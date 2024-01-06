package io.github.whoisamyy.components;

import io.github.whoisamyy.objects.GameObject;
import io.github.whoisamyy.test.Game;

//abstract because there cannot be any empty component

public abstract class Component {
    public GameObject gameObject;

    public void start() {}
    public void update() {}
    public void die() {} //what to do on dispose

    public final void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }
}
