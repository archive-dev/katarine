package io.github.whoisamyy.components;

import io.github.whoisamyy.objects.GameObject;
import io.github.whoisamyy.test.Game;

public abstract class Component {
    public GameObject gameObject;

    public abstract void start();
    public abstract void update();
    public abstract void die(); //what to do on dispose

    public final void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }
}
