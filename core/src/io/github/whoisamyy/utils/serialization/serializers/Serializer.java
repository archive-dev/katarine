package io.github.whoisamyy.utils.serialization.serializers;

import io.github.whoisamyy.logging.Logger;
import io.github.whoisamyy.utils.serialization.JsonObject;

public abstract class Serializer<T> extends Logger {
    public Serializer(){
        super();
    }

    //protected JsonWriter writer = new JsonWriter(new StringBuilder());

    public abstract String writeObject(T object) throws IllegalAccessException;

    /**
     * Method, used to
     * @param object
     * @return
     * @throws IllegalAccessException
     */
    public JsonObject toJsonObject(T object) throws IllegalAccessException {
        return JsonObject.toJsonObject(object);
    }
}
