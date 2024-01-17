package io.github.whoisamyy.test.serialization;

import io.github.whoisamyy.test.components.CircleController;
import io.github.whoisamyy.utils.serialization.Json;
import io.github.whoisamyy.utils.serialization.JsonObject;
import io.github.whoisamyy.utils.serialization.Serializer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ExampleSerializer extends Serializer<CircleController> {
    public ExampleSerializer(Class<CircleController> circleControllerClass) {
        super(circleControllerClass);
    }

    @Override
    public String writeObject(CircleController object) throws IllegalAccessException {
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
        return sb.toString();
    }
}
