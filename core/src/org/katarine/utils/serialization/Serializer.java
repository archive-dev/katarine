package org.katarine.utils.serialization;

import org.katarine.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public abstract class Serializer<T extends Serializable> {
    public static <T extends Serializable> ObjectRepresentation<T> serialize(Object obj) {
        return serialize(obj, obj.getClass());
    }

    public static <T extends Serializable> ObjectRepresentation<T> serialize(Object obj, Class<?> objClass) {
//        if (!Serializable.class.isAssignableFrom(objClass)) return null;
        if (objClass.equals(Object.class)) return null;
        Class<?> superObjClass = objClass.getSuperclass();

        ObjectRepresentation<? super T> superObjRep = new ObjectRepresentation<>(serialize(superObjClass.cast(obj), superObjClass));
        for (var f : superObjClass.getDeclaredFields()) {
            try {
                superObjRep.addField(f.getName(), Utils.getFieldValue(f, obj));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        ObjectRepresentation<T> objRep = new ObjectRepresentation<>(superObjRep);
        for (var f : objClass.getDeclaredFields()) {
            try {
                objRep.addField(f.getName(), Utils.getFieldValue(f, obj));
//                objRep.addFields(((Serializable) obj).getFields());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return objRep;
    }

    public File toFile(ObjectRepresentation<T> representation, File file) throws IOException {
        if (!file.exists()) file.createNewFile();

        try(FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(toString(representation).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return file;
    }

    public abstract String toString(ObjectRepresentation<T> representation);
    public abstract ObjectRepresentation<T> fromString(String string);
}
