package org.katarine.utils.serialization;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class Yaml<T extends Serializable> extends Serializer<T> {
    private final Class<? extends T> clazz;

    public Yaml(Class<? extends T> clazz) {
        this.clazz = clazz;
    }

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
                var f = representation.getRepresentedClass().getDeclaredField(a);
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
        final HashMap<String, Object> fields = new HashMap<>();
        ObjectRepresentation<T> rep = ObjectRepresentation.fromMap(clazz, fields);

        for (String line : lines) {
            int cint = line.indexOf(":");
            String identifier = line.substring(0, cint);
            String value = line.substring(cint + 1);
            if (line.startsWith("\t\t") || value.isBlank() || value.isEmpty()) continue;

            String fieldName = identifier.split("\\[")[0]
                    .replace("-", "")
                    .replace(" ", "")
                    .replace("\t", "");

            Class<?> fieldClass = extractClassFromYamlLine(identifier);

            if (fieldClass.isPrimitive() || fieldClass.equals(String.class)) {
                fields.put(fieldName, getPrimitiveValue(fieldClass, value.substring(1)));
            }
        }

        rep.addFields(fields);

        return rep;
    }

    private Object getPrimitiveValue(Class<?> clazz, String value) {
        if (clazz.equals(String.class)) return value;
        else if (clazz.equals(boolean.class) || clazz.equals(Boolean.class)) return Boolean.parseBoolean(value);
        else if (clazz.equals(int.class) || clazz.equals(Integer.class)) return Integer.parseInt(value);
        else if (clazz.equals(float.class) || clazz.equals(Float.class)) return Float.parseFloat(value);
        else if (clazz.equals(double.class) || clazz.equals(Double.class)) return Double.parseDouble(value);
        else if (clazz.equals(short.class) || clazz.equals(Short.class)) return Short.parseShort(value);
        else if (clazz.equals(byte.class) || clazz.equals(Byte.class)) return Byte.parseByte(value);
        else if (clazz.equals(char.class) || clazz.equals(Character.class)) return value.charAt(0);
        else if (clazz.equals(long.class) || clazz.equals(Long.class)) return Long.parseLong(value);
        else throw new IllegalArgumentException("Given class "+clazz+" is not primitive type");
    }

    private Class<?> extractClassFromYamlLine(String line) {
        String className = line.substring(line.indexOf("[")+1, line.lastIndexOf("]"));
        Optional<Class<?>> clazz = Optional.empty();
        try {
            clazz = Optional.of(ClassLoader.getSystemClassLoader().loadClass(className));
        } catch (ClassNotFoundException e) {
            if (className.equals(int.class.getTypeName())) clazz = Optional.of(int.class);
            else if (className.equals(boolean.class.getTypeName())) clazz = Optional.of(boolean.class);
            else if (className.equals(long.class.getTypeName())) clazz = Optional.of(long.class);
            else if (className.equals(byte.class.getTypeName())) clazz = Optional.of(byte.class);
            else if (className.equals(short.class.getTypeName())) clazz = Optional.of(short.class);
            else if (className.equals(float.class.getTypeName())) clazz = Optional.of(float.class);
            else if (className.equals(double.class.getTypeName())) clazz = Optional.of(double.class);
            else if (className.equals(char.class.getTypeName())) clazz = Optional.of(char.class);
        }
        return clazz.orElse(null);
    }

    private int countString(String str, String toCount) {
        int count = 0;
        while (str.contains(toCount)) {
            str = str.replaceFirst(toCount, "");
            count++;
        }

        return count;
    }
}
