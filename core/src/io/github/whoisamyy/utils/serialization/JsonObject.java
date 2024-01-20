package io.github.whoisamyy.utils.serialization;

import io.github.whoisamyy.logging.LogLevel;
import io.github.whoisamyy.logging.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class JsonObject {
    public static JsonObject root = new JsonObject(true).setWriter(new JsonWriter(new StringBuilder())).setParent(null);
    public JsonObject parent;
    public List<JsonObject> children = new LinkedList<>();

    private JsonWriter writer;

    private String name;
    private Object value;
    private Class<?> type; // придумать оправдание почему я не использую генерики

    private static Logger logger = new Logger(JsonObject.class.getName());

    private JsonObject(boolean root) {
        value = new RootJsonObject();
    }

    public JsonObject() {
        root.addChild(this);
    }
    public JsonObject(Class<?> type) {
        this.type = type;
    }


    /**
     * <p>
     * This method recursively converts the fields of the given object into a hierarchical structure represented
     * by a JsonObject. The JsonObject will have a name based on the fully qualified class name of the object.
     * The fields of the object will be added as children to the JsonObject, and the type information of the object
     * and its fields will be stored in the JsonObject.
     * </p>
     * <p>
     * Note: This method might not work correctly with certain types or objects with circular references.
     * </p>
     * @param object The Java object to be converted into a JSON object.
     * @return A JsonObject instance representing the converted JSON object.
     * @throws IllegalAccessException If an attempt is made to access a private or protected field that is not accessible by the current class or object.
     */
    public static JsonObject toJsonObject(Object object) throws IllegalAccessException {
        JsonObject jsonObject = new JsonObject();


        jsonObject.setValue(object);
        jsonObject.setType(object.getClass());
        jsonObject.setName(object.getClass().getName());

        if (Json.knowsExtender(object.getClass())) {
            logger.debug("type is known: \"" + object.getClass().getName() + ":" + object.getClass() + '"');
            jsonObject.addChild(Json.getByExtender(object.getClass()).toJsonObject(object).setName(object.getClass().getName()));
            return jsonObject;
        }

        for (Field f : object.getClass().getDeclaredFields()) {
            boolean isAccessible;
            if (Modifier.isStatic(f.getModifiers())) {
                isAccessible = f.canAccess(null);
            } else
                isAccessible = f.canAccess(object);

            try {
                f.setAccessible(true);
                if (Json.knowsExtender(f.getType())) {
                    logger.debug("type is known: \""+f.getName()+":"+f.getType()+'"');
                    jsonObject.addChild(Json.getByExtender(f.getType()).toJsonObject(f.get(object)).setName(f.getName()));
                } else if (f.getType().isPrimitive()) {
                    logger.debug("type is primitive: \""+f.getName()+":"+f.getType()+'"');
                    jsonObject.addChild(new JsonObject().setValue(f.get(object)).setName(f.getName()));
                }
                else {
                    logger.debug("type is unknown: \""+f.getName()+":"+f.getType()+'"');
                    jsonObject.addChild(new JsonObject().setValue(f.get(object)).setName(f.getName()));
                }
            } catch (InaccessibleObjectException e) {
                f.setAccessible(isAccessible);
                continue;
            }
            f.setAccessible(isAccessible);
        }

        return jsonObject;
    }

    protected final JsonObject setWriter(JsonWriter writer) {
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

    protected final JsonObject setChildren(List<JsonObject> children) {
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

    public void setType(Class<?> type) {
        this.type = type;
    }

    private final static class RootJsonObject {
        RootJsonObject(){}
    }
}
