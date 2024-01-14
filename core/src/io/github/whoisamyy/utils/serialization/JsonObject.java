package io.github.whoisamyy.utils.serialization;

public class JsonObject {
    public static JsonObject root = new JsonObject().setWriter(new JsonWriter(new StringBuilder())).setParent(null);

    public JsonObject parent;
    public JsonObject[] children;

    private JsonWriter writer;

    private String name;
    private Object value;
    private Class<?> type;

    private JsonObject setWriter(JsonWriter writer) {
        this.writer = writer;
        return this;
    }

    public JsonObject setParent(JsonObject parent) {
        this.parent = parent;
        return this;
    }

    public JsonObject setChildren(JsonObject[] children) {
        this.children = children;
        return this;
    }

    public JsonObject setName(String name) {
        this.name = name;
        return this;
    }

    public JsonObject setValue(Object value) {
        this.value = value;
        return this;
    }

    public JsonObject setType(Class<?> type) {
        this.type = type;
        return this;
    }

    public void write() {
        writer.writeField(parent, name, value, type);
    }
}
