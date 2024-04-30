package io.github.whoisamyy.editor;

import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import imgui.ImGui;
import imgui.flag.ImGuiMouseButton;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.type.ImString;
import io.github.whoisamyy.components.Component;
import io.github.whoisamyy.components.Transform2D;
import io.github.whoisamyy.editor.components.EditorObjectComponent;
import io.github.whoisamyy.objects.GameObject;
import io.github.whoisamyy.ui.imgui.AppendableGui;
import io.github.whoisamyy.ui.imgui.GuiBuilder;
import io.github.whoisamyy.utils.serialization.annotations.HideInInspector;
import io.github.whoisamyy.utils.serialization.annotations.Range;
import io.github.whoisamyy.utils.serialization.annotations.SerializeField;
import io.github.whoisamyy.utils.structs.tree.Node;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;

public class GuiGenerator {
    public GuiGenerator() {
    }

    public AppendableGui generate(Component component) {
        final GuiBuilder gui = new GuiBuilder();
        Field[] fields = component.getClass().getDeclaredFields();

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
                        }
                    } catch (Exception ignored) {}
                });
            }
            //TODO: add more types

            f.setAccessible(canAccess);
        }


        return gui.get();
    }

    public AppendableGui generate(GameObject obj) {
        final GuiBuilder gui = new GuiBuilder();

        gui.add(() -> {
           Transform2D transform2D = obj.transform;
           if (ImGui.treeNode(transform2D.getClass().getSimpleName())) {
               generate(transform2D).render();
               ImGui.treePop();
           }

           for (var c : obj.getComponents()) {
               if (c.getClass().isAnnotationPresent(HideInInspector.class)) continue;
               if (ImGui.treeNode(c.getClass().getSimpleName())) {
                   generate(c).render();
                   ImGui.treePop();
               }
           }
        });

        return gui.get();
    }

    public AppendableGui generate(GameObject... objects) {
        final GuiBuilder gui = new GuiBuilder();

        gui.add(() -> {
            for (var o : objects) {
                if (ImGui.treeNode(o.getName())) {
                    generate(o).render();
                    ImGui.treePop();
                }
            }
        });

        return gui.get();
    }

    private static final int leafFlags = ImGuiTreeNodeFlags.OpenOnDoubleClick | ImGuiTreeNodeFlags.Leaf;
    private static final int treeNodeFlags = ImGuiTreeNodeFlags.OpenOnArrow | ImGuiTreeNodeFlags.OpenOnDoubleClick
            | ImGuiTreeNodeFlags.SpanAvailWidth;

    public AppendableGui generateGameObjectsTree(Collection<GameObject> objects) {
        final GuiBuilder gui = new GuiBuilder();

        gui.add(() -> {
            for (var o : objects) {
                if (o.getClass().isAnnotationPresent(HideInInspector.class)) continue;
                if (!o.getChildren().isEmpty()) {
                    if (ImGui.treeNodeEx(o.getName(), treeNodeFlags)) {
                        generateGameObjectTree(o.toTree()).render();
                        ImGui.treePop();
                    }
                } else {
                    if (ImGui.treeNodeEx(o.getName(), leafFlags)) {
                        generateGameObjectTree(o.toTree()).render();
                        ImGui.treePop();
                    }
                }
            }
        });

        return gui.get();
    }

    public static GameObject selectedGameObject;

    public AppendableGui generateGameObjectTree(Node<GameObject> tree) {
        final GuiBuilder gui = new GuiBuilder();

        gui.add(() -> {
            int selected = 0;
            try {
                selected = tree.get().getComponent(EditorObjectComponent.class).selected ? 1 : 0;
            } catch (NullPointerException ignored) {}
            if (!tree.getChildren().isEmpty()) {
                if (ImGui.treeNodeEx(tree.get().getName(), treeNodeFlags | selected)) {
                    try {
                        if (ImGui.isItemClicked(ImGuiMouseButton.Left)) {
                            if (!ImGui.getIO().getKeyCtrl()) {
                                EditorObjectComponent.selection.forEach(go ->
                                        go.getComponent(EditorObjectComponent.class).selected = false);
                                EditorObjectComponent.selection.clear();
                            }
                            boolean b;
                            b = tree.get().getComponent(EditorObjectComponent.class).selected =
                                    !tree.get().getComponent(EditorObjectComponent.class).selected;
                            if (b)
                                EditorObjectComponent.selection.add(tree.get());
                        }
                    } catch (NullPointerException ignored) {}
                    for (var c : tree.getChildren()) {
                        generateGameObjectTree(c).render();
                    }

                    ImGui.treePop();
                }
            } else {
                if (ImGui.treeNodeEx(tree.get().getName(), leafFlags | selected)) {
                    try {
                        if (ImGui.isItemClicked(ImGuiMouseButton.Left)) {
                            if (!ImGui.getIO().getKeyCtrl()) {
                                EditorObjectComponent.selection.forEach(go ->
                                        go.getComponent(EditorObjectComponent.class).selected = false);
                                EditorObjectComponent.selection.clear();
                            }
                            boolean b;
                            b = tree.get().getComponent(EditorObjectComponent.class).selected =
                                    !tree.get().getComponent(EditorObjectComponent.class).selected;
                            if (b)
                                EditorObjectComponent.selection.add(tree.get());
                        }
                    } catch (NullPointerException ignored) {}

                    ImGui.treePop();
                }
            }
        });

        return gui.get();
    }
}
