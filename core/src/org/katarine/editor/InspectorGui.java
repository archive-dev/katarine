package org.katarine.editor;

import imgui.ImGui;
import imgui.flag.ImGuiPopupFlags;
import org.katarine.components.Component;
import org.katarine.components.Transform2D;
import org.katarine.objects.GameObject;
import org.katarine.ui.imgui.AppendableGui;
import org.katarine.ui.imgui.Gui;
import org.katarine.ui.imgui.GuiBuilder;
import org.katarine.utils.Utils;
import org.katarine.utils.serialization.annotations.HideInInspector;
import org.katarine.utils.structs.ClassHashSet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

final class InspectorGui {
    public static AppendableGui generate(GameObject obj) {
        final GuiBuilder gui = new GuiBuilder();

        gui.add(ctxMenu);
        gui.add(() -> {
            Transform2D transform2D = obj.transform;
            if (ImGui.treeNode(transform2D.getClass().getSimpleName())) {
                GuiGenerator.generate(transform2D).render();
                ImGui.treePop();
            }

            for (var c : obj.getComponents()) {
                if (c.getClass().isAnnotationPresent(HideInInspector.class)) continue;
                if (ImGui.treeNode(c.getClass().getSimpleName())) {
                    GuiGenerator.generate(c).render();
                    ImGui.treePop();
                }
            }
        });

        return gui.get();
    }
    private static final ClassHashSet componentsClasses = new ClassHashSet();
    static {
        componentsClasses.addAll(Utils.loadedClasses.getSubClassesOf(Component.class));
    }

    private static final Gui ctxMenu;
    static {
        ctxMenu = () -> {
            if (ImGui.beginPopupContextWindow(ImGuiPopupFlags.MouseButtonRight)) {
                if (!ImGui.beginMenu("Add Component")) {
                    ImGui.endPopup();
                    return;
                }
                if (ImGui.beginListBox("Components")) {
                    for (var c : componentsClasses) {
                        if (c.isAnnotationPresent(HideInInspector.class) | Modifier.isAbstract(c.getModifiers())) continue;
                        if (ImGui.menuItem(c.getSimpleName())) {
                            try {
                                Component comp;
                                //noinspection unchecked
                                SceneViewGui.selectedGameObject.addComponent(
                                        comp = ((Class<? extends Component>) c).getDeclaredConstructor().newInstance()
                                );

                            } catch (InstantiationException | IllegalAccessException |
                                     InvocationTargetException | NoSuchMethodException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    ImGui.endListBox();
                }
                ImGui.endMenu();

                ImGui.endPopup();
            }
        };
    }
}
