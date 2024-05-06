package org.katarine.utils.serialization.serializers;

import org.katarine.utils.serialization.Json;
import org.katarine.utils.serialization.JsonObject;
import org.katarine.utils.serialization.JsonWriter;

import java.util.Map;

public class MapSerializer<T extends Map<?, ?>> extends Serializer<T> {
    @Override
    public String writeObject(T object) throws IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        sb.append("["); // вообще любой нормальный парсер json'ов выдаст вам ошибку, но пока что будет так

        for (Map.Entry<?, ?> entry : object.entrySet()) {
            Object k = entry.getKey();
            Object v = entry.getValue();

            if (Json.knownTypes.containsKey(k.getClass())) {
                sb.append(new JsonWriter(new StringBuilder()).writeObject(JsonObject.toJsonObject(k))).append(":");
            }
            else {
                sb.append('"').append(k).append('"').append(":");
            }

            if (Json.knownTypes.containsKey(v.getClass())) {
                sb.append(new JsonWriter(new StringBuilder()).writeObject(JsonObject.toJsonObject(v))).append(":");
            }
            else {
                sb.append('"').append(v).append('"').append(",");
            }
        }

        try {
            sb.deleteCharAt(sb.length()-1);
        } catch (IndexOutOfBoundsException ignored) {}

        sb.append("]");

        logger.debug(sb.toString());
        return sb.toString();
    }
}
