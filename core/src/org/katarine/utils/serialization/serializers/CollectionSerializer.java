package org.katarine.utils.serialization.serializers;

import org.katarine.utils.serialization.Json;
import org.katarine.utils.serialization.JsonObject;
import org.katarine.utils.serialization.JsonWriter;

import java.util.Collection;
import java.util.Iterator;

public class CollectionSerializer<T extends Collection> extends Serializer<T> {
    public CollectionSerializer() {
        super();
    }

    //@Override
    public String writeObject(T object) throws IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        sb.append("[");


        Object e;

        for (Iterator itr = object.iterator(); itr.hasNext(); ) {
            e = itr.next();
            if (Json.knownTypes.containsKey(e.getClass())) {
                sb.append(new JsonWriter(new StringBuilder()).writeObject(JsonObject.toJsonObject(e))).append(",");
            }
            else {
                sb.append('"').append(e).append('"').append(",");
            }
        }

        //for (int i = 0; i < object.size(); i++) {
        //    Object o = object.(i);

        //    if (Json.knownTypes.containsKey(o.getClass())) {
        //        sb.append(new JsonWriter(new StringBuilder()).writeObject(JsonObject.toJsonObject(o))).append(",");
        //    }
        //    else {
        //        sb.append('"').append(o).append('"').append(",");
        //    }
        //}

        try {
            sb.deleteCharAt(sb.length()-1);
        } catch (IndexOutOfBoundsException ignored) {}

        sb.append("]");

        logger.debug(sb.toString());

        return sb.toString();
    }
}
