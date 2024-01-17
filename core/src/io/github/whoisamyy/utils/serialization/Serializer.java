package io.github.whoisamyy.utils.serialization;

import io.github.whoisamyy.logging.Logger;

public abstract class Serializer<T> extends Logger {
    public Serializer(Class<T> tClass) {
        super();
    }

    public Serializer(){
        super();
    }

    //protected JsonWriter writer = new JsonWriter(new StringBuilder());

    public abstract String writeObject(T object) throws IllegalAccessException;
}
