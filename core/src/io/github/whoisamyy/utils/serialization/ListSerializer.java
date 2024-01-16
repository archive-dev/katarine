package io.github.whoisamyy.utils.serialization;

import java.util.List;

public class ListSerializer<T extends List<?>> extends Serializer<T>{
    public ListSerializer(Class<T> tClass) {
        super(tClass);
    }

    @Override
    public String writeObject(T object) throws IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        System.out.println(object);

        for (int i = 0; i < object.size(); i++) {
            Object o = object.get(i);

            if (Json.knownTypes.containsKey(o.getClass())) {
                sb.append(i).append(":").append(Json.knownTypes.get(o.getClass()).writeObject(o)).append(",");
            }
            else {
                sb.append(i).append(":").append(o).append(",");
            }
        }

        try {
            sb.deleteCharAt(sb.length()-1);
        } catch (IndexOutOfBoundsException ignored) {}

        sb.append("}");
        return sb.toString();
    }
}
