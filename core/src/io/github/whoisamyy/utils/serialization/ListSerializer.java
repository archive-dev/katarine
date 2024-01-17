package io.github.whoisamyy.utils.serialization;

import java.util.List;

public class ListSerializer<T extends List<?>> extends Serializer<T>{
    public ListSerializer(Class<T> tClass) {
        super(tClass);
    }

    @Override
    public String writeObject(T object) throws IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        sb.append("[");


        for (int i = 0; i < object.size(); i++) {
            Object o = object.get(i);

            if (Json.knownTypes.containsKey(o.getClass())) {
                sb.append(new JsonWriter(new StringBuilder()).writeObject(JsonObject.toJsonObject(o))).append(",");
            }
            else {
                sb.append('"').append(o).append('"').append(",");
            }
        }

        try {
            sb.deleteCharAt(sb.length()-1);
        } catch (IndexOutOfBoundsException ignored) {}

        sb.append("]");
        return sb.toString();
    }
}
