package org.katarine.objects;

import org.katarine.annotations.EditorObject;

@EditorObject
public class Scene extends GameObject {
    protected final Scene scene = this;
    public Scene(String name) {
        this.name = name;
    }
}
