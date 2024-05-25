package org.katarine.objects;

import org.katarine.annotations.EditorObject;
import org.katarine.utils.serialization.annotations.DontSerialize;

import java.util.HashMap;

@EditorObject
public class Scene extends GameObject {
    @DontSerialize
    protected final Scene scene = this;
    public Scene(String name) {
        this.name = name;
    }

    @Override
    public HashMap<String, Object> getFields() {
        final HashMap<String, Object> fields = new HashMap<>();
        fields.put("scene", this.name);
//        fields.put("children", this.children);

        return fields;
    }
}
