package io.github.whoisamyy.utils.serialization.serializers;

import io.github.whoisamyy.objects.GameObject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class GameObjectSerializer<T extends GameObject> extends Serializer<T>{

    public GameObjectSerializer() {
        super();
    }

    @Override
    public String writeObject(T object) throws IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        for (Field f : object.getClass().getDeclaredFields()) {
            boolean isAccessible;
            if (Modifier.isStatic(f.getModifiers()))
                isAccessible = f.canAccess(null);
            else
                isAccessible = f.canAccess(object);

            f.setAccessible(true);

            sb.append(f.getName()).append(":").append('"').append(f.get(object)).append('"').append(",");

            f.setAccessible(isAccessible);
        }
        try {
            sb.deleteCharAt(sb.length()-1);
        } catch (IndexOutOfBoundsException ignored) {}

        sb.append("}");

        logger.debug(sb.toString());
        return sb.toString();
    }
}
