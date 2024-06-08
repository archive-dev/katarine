package org.katarine.utils;

import com.badlogic.gdx.math.Vector2;
import com.google.common.reflect.ClassPath;
import org.katarine.logging.LogLevel;
import org.katarine.logging.Logger;
import org.katarine.utils.structs.ClassHashSet;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public final class Utils {
    private Utils(){}

    /**
     * Pixels Per Unit. 100 by default
     */
    public static float PPU = 100f;

    public static final ClassHashSet loadedClasses = new ClassHashSet();

    public static void init() throws IOException {
        long startTime = System.currentTimeMillis();
        ClassPath.from(ClassLoader.getSystemClassLoader())
                .getAllClasses()
                .forEach(c -> {
                    try {
                        loadedClasses.add(c.load());
                    } catch (NoClassDefFoundError ignored) {}
                });
        Logger logger;
        (logger = new Logger(LogLevel.DEBUG)).debug("Loaded " + loadedClasses.size() + " classes. " + (System.currentTimeMillis()-startTime) + " ms");
    }

    public static float[] getVertices(float[][] points) {
        float[] vertices = new float[points.length*2];
        int c = 0;
        for (float[] point : points) {
            for (float v : point) {
                vertices[c] = v;
                c++;
            }
        }
        return vertices;
    }

    public static float[] getVertices(Vector2[] points) {
        float[] vertices = new float[points.length*2];
        int c = 0;
        for (int i = 0; i < points.length; i+=2) {
            vertices[i] = points[c].x;
            vertices[i+1] = points[c].y;
            c++;
        }
        return vertices;
    }

    public static float[][] getPoints(float[] vertices) {
        if (vertices.length%2!=0) throw new IllegalStateException("vertices must have an even number of elements");
        if (vertices.length<6) throw new IllegalStateException("vertices must have at least 6 elements");
        float[][] points = new float[vertices.length/2][2];
        int c = 0;
        for (int i = 0; i < vertices.length; i+=2) {
            points[c][0] = vertices[i];
            points[c][1] = vertices[i+1];
            c++;
        }
        return points;
    }

    public static Vector2[] getVector2Points(float[] vertices) {
        if (vertices.length%2!=0) throw new IllegalStateException("vertices must have an even number of elements");
        if (vertices.length<6) throw new IllegalStateException("vertices must have at least 6 elements");
        Vector2[] points = new Vector2[vertices.length/2];
        int c = 0;
        for (int i = 0; i < vertices.length; i+=2) {
            points[c] = new Vector2(vertices[i], vertices[i+1]);
            c++;
        }
        return points;
    }

    public static void setStaticFieldValue(Class<?> clazz, String fieldName, Object fieldValue) throws NoSuchFieldException, IllegalAccessException {
        Field field = clazz.getDeclaredField(fieldName);
        boolean isAccessible = field.canAccess(null);
        field.setAccessible(true);
        field.set(null, fieldValue);
        field.setAccessible(isAccessible);
    }

    public static void setFieldValue(Object objectInstance, String fieldName, Object fieldValue) throws NoSuchFieldException, IllegalAccessException {
        Field field = objectInstance.getClass().getDeclaredField(fieldName);
        boolean isAccessible = field.canAccess(objectInstance);
        field.setAccessible(true);
        field.set(objectInstance, fieldValue);
        field.setAccessible(isAccessible);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getStaticFieldValue(Class<?> clazz, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = clazz.getDeclaredField(fieldName);
        boolean isAccessible = field.canAccess(null);
        field.setAccessible(true);
        Object o = field.get(null);
        field.setAccessible(isAccessible);
        return (T) o;
    }

    public static Object getFieldValue(Object objectInstance, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = objectInstance.getClass().getDeclaredField(fieldName);
        boolean isAccessible;
        if (Modifier.isStatic(field.getModifiers()))
            isAccessible = field.canAccess(null);
        else
            isAccessible = field.canAccess(objectInstance);
        field.setAccessible(true);
        Object o = field.get(objectInstance);
        field.setAccessible(isAccessible);
        return o;
    }

    public static Object getFieldValue(Field field, Object objectInstance) throws IllegalAccessException {
        boolean isAccessible;
        if (Modifier.isStatic(field.getModifiers()))
            isAccessible = field.canAccess(null);
        else
            isAccessible = field.canAccess(objectInstance);
        field.setAccessible(true);
        Object ret = field.get(objectInstance);
        field.setAccessible(isAccessible);
        return ret;
    }

    public static List<Field> getAnnotatedFields(Object o, Class<? extends Annotation> annotation) {
        ArrayList<Field> result = new ArrayList<>();

        for (Field f : o.getClass().getDeclaredFields()) {
            if (f.isAnnotationPresent(annotation)) {
                result.add(f);
            }
        }

        return result;
    }

    public static <T extends Comparable<T>> T clamp(T value, T max, T min) {
        return value.compareTo(max)>=1 ? max : value.compareTo(min)<=-1 ? min : value;
    }

    public static boolean isClassAnnotated(Class<?> clazz, Class<? extends Annotation> annotation) {
        return Arrays.stream(clazz.getAnnotations()).collect(Collectors.toCollection(HashSet::new)).contains(annotation);
    }
}
