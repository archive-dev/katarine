package org.katarine.utils.serialization;

import java.util.HashMap;
import java.util.Map;

public class Yaml<T extends Serializable> extends Serializer<T> {
    @Override
    public String toString(ObjectRepresentation<T> representation) {
        return toString(representation, 0);
    }

    private String toString(ObjectRepresentation<T> representation, int depth) {
        String ret = "";
        HashMap<String, Object> allFields = new HashMap<>();
        ObjectRepresentation<?> rep = representation;
        while ((rep=rep.getSuperObject())!=null) {
            allFields.putAll(rep.getFields());
        }

        allFields.putAll(representation.getFields());

        for (Map.Entry<String, Object> entry : allFields.entrySet()) {
            String a = entry.getKey();
            Object b = entry.getValue();
            String bClass;
            if (b==null) bClass = "null";
            else bClass = b.getClass().getName();
            ret += "\t".repeat(depth);
            if (depth>0)
                ret += "- ";

            if (!(b instanceof Serializable)) {
                ret += a + "[" + bClass + "]" + ": " + b;
            } else {
                ret += a + "[" + bClass + "]" + ": \n" + toString(Serializer.serialize(b), depth+1);
            }
            ret += "\n";
        }

        return ret.substring(0, ret.length()-2);
    }

    @Override
    public ObjectRepresentation<T> fromString(String string) {
        return null;
    }
}
