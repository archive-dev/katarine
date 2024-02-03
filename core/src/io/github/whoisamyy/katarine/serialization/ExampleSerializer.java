package io.github.whoisamyy.katarine.serialization;

import io.github.whoisamyy.katarine.components.CircleController;
import io.github.whoisamyy.utils.serialization.serializers.Serializer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ExampleSerializer extends Serializer<CircleController> {
    public ExampleSerializer() {
        super();
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
