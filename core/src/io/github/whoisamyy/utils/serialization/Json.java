package io.github.whoisamyy.utils.serialization;

import io.github.whoisamyy.utils.serialization.serializers.Serializer;

import java.util.Hashtable;
import java.util.Map;

public class Json {
    public static Hashtable<Class<?>, Serializer> knownTypes = new Hashtable<>();

    private Json() {

    }

    public static void addTypeSerializer(Class<?> tClass, Serializer<?> serializer) {
        knownTypes.put(tClass, serializer);
    }

    public static boolean knowsExtender(Class<?> extender) {
        extender = primitiveClassToWrapperClass(extender);

        if (knownTypes.containsKey(extender)) return true;

        for (Class<?> c : knownTypes.keySet()) {
            if (c.isAssignableFrom(extender)) {
                return true;
            }
        }

        return false;
    }

    public static Serializer getByExtender(Class<?> extender) {
        extender = primitiveClassToWrapperClass(extender);

        Serializer<?> s = knownTypes.get(extender);
        if (s!=null) return s;

        System.out.println(extender);

        for (Map.Entry<Class<?>, Serializer> entry : knownTypes.entrySet()) {
            if (entry.getKey().isAssignableFrom(extender)) {
                return entry.getValue();
            }
        }

        return null;
    }

    private static Class<?> primitiveClassToWrapperClass(Class<?> primitive) {
        if (primitive.isPrimitive()) {
            primitive = java.lang.reflect.Array.get(java.lang.reflect.Array.newInstance(primitive, 1), 0).getClass();
            System.out.println(primitive.getName());
        }

        return primitive;
    }
}
