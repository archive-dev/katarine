package io.github.whoisamyy.objects;

import io.github.whoisamyy.katarine.annotations.EditorObject;

@EditorObject
public class Scene extends GameObject {
    protected final Scene scene = this;
    public Scene(String name) {
        this.name = name;
    }
}
