package io.github.whoisamyy.utils.serialization;

import java.util.Hashtable;

public class Json {
    public static Hashtable<Class, Serializer> knownTypes = new Hashtable<>();

    public Json() {

    }

    public static void addTypeSerializer(Class<?> tClass, Serializer<?> serializer) {
        knownTypes.put(tClass, serializer);
    }
}
