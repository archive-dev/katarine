package io.github.whoisamyy.utils.serialization;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class JsonObject {
    public static JsonObject root = new JsonObject().setWriter(new JsonWriter(new StringBuilder())).setParent(null);

    public JsonObject parent;
    public List<JsonObject> children = new LinkedList<>();

    private JsonWriter writer;

    private String name;
    private Object value;
    private Class<?> type;

    public JsonObject(){}
    public JsonObject(Class<?> type) {
        this.type = type;
    }

    public static JsonObject toJsonObject(Object object) throws IllegalAccessException {
        JsonObject jsonObject = new JsonObject();

        jsonObject.setValue(object);
        for (Field f : object.getClass().getDeclaredFields()) {
            boolean isAccessible;
            if (Modifier.isStatic(f.getModifiers())) {
                isAccessible = f.canAccess(null);
            } else
                isAccessible = f.canAccess(object);

            f.setAccessible(true);
            if (Json.knownTypes.containsKey(f.getType()))
                jsonObject.addChild(toJsonObject(f.get(object)));
            else
                jsonObject.addChild(new JsonObject().setValue(f.get(object)));
            f.setAccessible(isAccessible);
        }

        return jsonObject;
    }

    private JsonObject setWriter(JsonWriter writer) {
        this.writer = writer;
        return this;
    }

    public JsonObject setParent(JsonObject parent) {
        this.parent = parent;
        return this;
    }

    public JsonObject addChild(JsonObject child) {
        this.children.add(child);
        return this;
    }

    public JsonObject setChildren(List<JsonObject> children) {
        this.children = children;
        return this;
    }

    public JsonObject setName(String name) {
        this.name = name;
        return this;
    }

    public JsonObject setValue(Object value) {
        this.value = value;
        this.type = this.value!=null?this.value.getClass():null;
        return this;
    }

    public static JsonObject getRoot() {
        return root;
    }

    public JsonObject getParent() {
        return parent;
    }

    public List<JsonObject> getChildren() {
        return children;
    }

    public JsonWriter getWriter() {
        return writer;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public Class<?> getType() {
        return type;
    }
}
