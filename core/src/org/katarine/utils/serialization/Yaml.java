package org.katarine.utils.serialization;

import org.katarine.utils.Utils;

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
            Class<?> fType = b==null ? null : b.getClass();
            try {
                var f = representation.getRepresentatedClass().getDeclaredField(a);
                if (fType==null || !fType.isAssignableFrom(f.getType()))
                    fType = f.getType();
            } catch (NoSuchFieldException | NullPointerException ignored) {}

            if (b==null) bClass = "null";
            else bClass = fType.getTypeName();
            ret += "\t\t".repeat(depth);
            if (depth>0)
                ret += "- ";

            if (!(b instanceof Serializable)) {
                ret += a + "[" + bClass + "]" + ": " + b;
            } else {
                ret += a + "[" + bClass + "]" + ": \n" + toString(ObjectRepresentation.fromMap(fType, ((Serializable) b).getFields()), depth+1);
            }

            ret += "\n";
        }

//        try {
//            ret = ret.substring(0, ret.length() - 2);
//        } catch (StringIndexOutOfBoundsException ignored) {}

        return ret;
    }

    @Override
    public ObjectRepresentation<T> fromString(String string) {
        String[] lines = string.split("\n");

        for (int i = 0; i < lines.length; i++) {
            var lsplit = lines[i].split(":");
            String identifier = lsplit[0];
            String value = lsplit[1];

            String fieldName = identifier.split("\\[")[0]
                    .replace("-", "")
                    .replace(" ", "")
                    .replace("\t", "");

            Class<?> fieldClass = extractClassFromYamlLine(identifier);

        }

        return null;
    }

    private Class<?> extractClassFromYamlLine(String line) {
        String className = line.substring(line.indexOf("[")+1, line.lastIndexOf("]"));
        var clazz = Utils.loadedClasses.stream().filter(c->c.getSimpleName().startsWith(className)).findFirst();
        return clazz.orElse(null);
    }
}
