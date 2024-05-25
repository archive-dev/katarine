package org.katarine.editor;

import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import imgui.ImGui;
import imgui.type.ImString;
import org.katarine.components.Component;
import org.katarine.ui.imgui.AppendableGui;
import org.katarine.ui.imgui.GuiBuilder;
import org.katarine.ui.imgui.GuiSupplier;
import org.katarine.ui.imgui.HideInInspector;
import org.katarine.ui.imgui.Range;
import org.katarine.utils.serialization.annotations.SerializeField;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public final class GuiGenerator {
    private GuiGenerator() {}

    private static List<Field> getFieldsRecursively(Class<?> clazz, List<Field> fields) {
        if (Component.class.equals(clazz)) return fields;
        if (fields==null) {
            return getFieldsRecursively(clazz.getSuperclass(), new ArrayList<>(List.of(clazz.getDeclaredFields())));
        } else {
            fields.addAll(List.of(clazz.getDeclaredFields()));
            return getFieldsRecursively(clazz.getSuperclass(), fields);
        }
    }

    public static AppendableGui generate(Component component) {
        final GuiBuilder gui = new GuiBuilder();
        List<Field> fields = getFieldsRecursively(component.getClass(), null);

        for (var f : fields) {
            if (f.isAnnotationPresent(HideInInspector.class)) continue;
            if (Modifier.isFinal(f.getModifiers()) && !Collection.class.isAssignableFrom(f.getType())
                && !Vector.class.isAssignableFrom(f.getType())) continue;
            boolean canAccess;
            try {
                canAccess = f.canAccess(component);
            } catch (IllegalArgumentException e) {
                canAccess = f.canAccess(null);
            }

            if (f.isAnnotationPresent(SerializeField.class)) {
                f.setAccessible(true);
            } else if (!canAccess) continue;

            if (f.getType().equals(boolean.class) || f.getType().equals(Boolean.class)) {
                gui.add(() -> {
                    try {
                        if (ImGui.checkbox(f.getName(), ((boolean) f.get(component))))
                            f.set(component, !((boolean) f.get(component)));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
            } else if (f.getType().equals(int.class) || f.getType().equals(Integer.class)) {
                gui.add(() -> {
                    try {
                        int[] v = {(int) f.get(component)};
                        if (f.isAnnotationPresent(Range.IntegerRange.class)) {
                            if (ImGui.dragInt(f.getName(), v, f.getAnnotation(Range.IntegerRange.class).min(),
                                    f.getAnnotation(Range.IntegerRange.class).max()))
                                f.set(component, v[0]);
                        } else {
                            if (ImGui.dragInt(f.getName(), v))
                                f.set(component, v[0]);
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
            } else if (f.getType().equals(float.class) || f.getType().equals(Float.class)) {
                gui.add(() -> {
                    try {
                        float[] v = {(float) f.get(component)};
                        if (f.isAnnotationPresent(Range.FloatRange.class)) {
                            if (ImGui.dragFloat(f.getName(), v, 0.01f, f.getAnnotation(Range.FloatRange.class).min(),
                                    f.getAnnotation(Range.FloatRange.class).max()))
                                f.set(component, v[0]);
                        } else {
                            if (ImGui.dragFloat(f.getName(), v, 0.01f))
                                f.set(component, v[0]);
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
            } else if (f.getType().equals(Vector2.class)) {
                gui.add(() -> {
                    try {
                        float[] v = {((Vector2) f.get(component)).x, ((Vector2) f.get(component)).y};
                        if (ImGui.dragFloat2(f.getName(), v, 0.01f))
                            ((Vector2) f.get(component)).set(v[0], v[1]);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
            } else if (f.getType().equals(String.class)) {
                gui.add(() -> {
                    try {
                        ImString s = new ImString();
                        s.set((String) f.get(component));
                        if (ImGui.inputText(f.getName(), s))
                            f.set(component, s.get());
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
            } else {
                gui.add(() -> {
                    try {
                        Class<?> type = f.getType();
                        if (type.isEnum()) {
                            if (ImGui.beginCombo(f.getName(), ((Enum<?>) f.get(component)).name())) {
                                for (var e : type.getEnumConstants()) {
                                    if (ImGui.selectable(e.toString())) {
                                        f.set(component, e);
//                                        ImGui.endCombo();
                                    }
                                }
                                ImGui.endCombo();
                            }
                            return;
                        }

                        Arrays.stream(type.getDeclaredMethods()).filter(m -> m.isAnnotationPresent(GuiSupplier.class)).forEach(m -> {
                            try {
                                m.invoke(f.get(component));
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new RuntimeException(e);
                            }
                        });

                    } catch (Exception ignored) {}
                });
            }
            //TODO: add more types

            f.setAccessible(canAccess);
        }


        return gui.get();
    }
}
