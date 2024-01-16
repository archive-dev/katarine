package io.github.whoisamyy.utils.serialization;

public abstract class Serializer<T> {
    public Serializer(Class<T> tClass) {

    }

    public abstract String writeObject(T object) throws IllegalAccessException;
}
