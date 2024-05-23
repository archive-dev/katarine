package org.katarine.utils.serialization;

import java.util.HashMap;

public final class ObjectRepresentation<T extends Serializable> {
    private final HashMap<String, Object> fields = new HashMap<>();
    private final ObjectRepresentation<? super T> superObject;

    public ObjectRepresentation(ObjectRepresentation<? super T> superObject) {
        this.superObject = superObject;
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, Object> getFields() {
        return (HashMap<String, Object>) fields.clone();
    }

    public ObjectRepresentation<? super T> getSuperObject() {
        return this.superObject;
    }

    public void addField(String name, Object value) {
        this.fields.put(name, value);
    }

    public void addFields(HashMap<String, Object> fields) {
        this.fields.putAll(fields);
    }
}
