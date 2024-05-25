package org.katarine.utils.serialization;

import org.katarine.utils.Utils;

import java.util.HashMap;

public final class ObjectRepresentation<T extends Serializable> {
    private final HashMap<String, Object> fields = new HashMap<>();
    private final ObjectRepresentation<? super T> superObject;
    private final Class<?> representedClass;

    public ObjectRepresentation(Class<?> representedClass, ObjectRepresentation<? super T> superObject) {
        this.representedClass = representedClass;
        this.superObject = superObject;
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, Object> getFields() {
        return (HashMap<String, Object>) fields.clone();
    }

    public ObjectRepresentation<? super T> getSuperObject() {
        return this.superObject;
    }

    public void addField(String name, Object value) {
        this.fields.put(name, value);
    }

    public void addFields(HashMap<String, Object> fields) {
        this.fields.putAll(fields);
    }

    public Class<?> getRepresentedClass() {
        return representedClass;
    }

    public static <T extends Serializable> ObjectRepresentation<T> from(Object obj) {
        return from(obj, obj.getClass());
    }

    public static <T extends Serializable> ObjectRepresentation<T> from(Object obj, Class<?> objClass) {
//        if (!Serializable.class.isAssignableFrom(objClass)) return null;
        if (objClass.equals(Object.class)) return null;
        Class<?> superObjClass = objClass.getSuperclass();

        ObjectRepresentation<? super T> superObjRep = new ObjectRepresentation<>(superObjClass, from(obj, superObjClass));
        for (var f : superObjClass.getDeclaredFields()) {
            try {
                String fieldName = f.getName();
                superObjRep.addField(fieldName, Utils.getFieldValue(f, obj));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        ObjectRepresentation<T> objRep = new ObjectRepresentation<>(objClass, superObjRep);
        for (var f : objClass.getDeclaredFields()) {
            try {
//                System.out.println(f.getName() + "; " + obj.getClass());
                objRep.addField(f.getName(), Utils.getFieldValue(f, obj));
//                objRep.addFields(((Serializable) obj).getFields());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return objRep;
    }

    public static <T extends Serializable> ObjectRepresentation<T> fromMap(Class<?> clazz, HashMap<String, Object> fieldsMap) {
        ObjectRepresentation<T> objRep = new ObjectRepresentation<>(clazz, null);

        objRep.addFields(fieldsMap);
        return objRep;
    }
}
