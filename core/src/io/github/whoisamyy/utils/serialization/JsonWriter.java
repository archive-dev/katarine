package io.github.whoisamyy.utils.serialization;

import java.util.HashSet;

public class JsonWriter {
    StringBuilder jsonBuilder;

    static HashSet<Class> knownTypes = new HashSet<>();

    public JsonWriter(StringBuilder jsonBuilder) {
        this.jsonBuilder = jsonBuilder;
        startWrite();
    }

    void startWrite() {
        jsonBuilder.append("{");
    }

    void endWrite() {
        jsonBuilder.append("}");
    }

    public void writeField(JsonObject parent, String name, Object value, Class<?> type) {

    }
}
