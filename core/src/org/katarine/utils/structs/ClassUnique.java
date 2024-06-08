package org.katarine.utils.structs;

public interface ClassUnique<T> {
    default void checkClass(Class<? extends T> clazz) {
        if (hasClass(clazz)) throw new IllegalStateException("Class " + clazz.getSimpleName() + " is already present");
    }

    /**
     * Check if the given class is present in the collection of classes of type T.
     *
     * @param clazz the class to check for presence
     * @return true if the class is present, false otherwise
     */
    boolean hasClass(Class<? extends T> clazz);
}
