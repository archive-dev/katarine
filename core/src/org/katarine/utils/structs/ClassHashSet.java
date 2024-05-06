package org.katarine.utils.structs;

import java.util.HashSet;

public class ClassHashSet extends HashSet<Class<?>> {

    public ClassHashSet getSubClassesOf(Class<?> clazz) {
        final ClassHashSet classHashSet = new ClassHashSet();
        forEach(c -> {
            if (clazz.isAssignableFrom(c)) classHashSet.add(c);
        });
        return classHashSet;
    }
}
