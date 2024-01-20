package io.github.whoisamyy.utils.serialization.serializers;

import io.github.whoisamyy.components.Component;

import java.lang.reflect.Field;

public class ComponentSerializer<T extends Component> extends Serializer<T>{
    public ComponentSerializer() {
        super();
    }

    @Override
    public String writeObject(T object) throws IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        for (Field f : object.getClass().getDeclaredFields()) {
            sb.append(f.getName()).append(":").append(f.get(object)).append(",");
        }
        try {
            sb.deleteCharAt(sb.length()-1);
        } catch (IndexOutOfBoundsException ignored) {}

        sb.append("}");

        debug(sb.toString());
        return sb.toString();
    }
}
