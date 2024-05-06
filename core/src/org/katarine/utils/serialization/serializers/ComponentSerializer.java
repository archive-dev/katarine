package org.katarine.utils.serialization.serializers;

import org.katarine.components.Component;

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

        logger.debug(sb.toString());
        return sb.toString();
    }
}
