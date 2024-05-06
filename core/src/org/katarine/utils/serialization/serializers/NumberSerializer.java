package org.katarine.utils.serialization.serializers;

import org.katarine.utils.serialization.JsonObject;

public class NumberSerializer<T extends Number> extends Serializer<T> {
    @Override
    public String writeObject(T object) throws IllegalAccessException {
        logger.debug(String.valueOf(object));
        return String.valueOf(object);
    }

    @Override
    public JsonObject toJsonObject(T object) throws IllegalAccessException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.setValue(object);
        jsonObject.setType(object.getClass());
        jsonObject.setName(jsonObject.getType().getName());

        logger.debug(jsonObject.getName()+" "+jsonObject.getType()+" "+jsonObject.getValue());

        return jsonObject;
    }
}
