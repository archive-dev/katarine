package io.github.whoisamyy.editor;

import imgui.ImGui;
import imgui.flag.ImGuiPopupFlags;
import imgui.flag.ImGuiTreeNodeFlags;
import io.github.whoisamyy.components.Component;
import io.github.whoisamyy.components.Transform2D;
import io.github.whoisamyy.logging.LogLevel;
import io.github.whoisamyy.logging.Logger;
import io.github.whoisamyy.objects.GameObject;
import io.github.whoisamyy.ui.imgui.AppendableGui;
import io.github.whoisamyy.ui.imgui.Gui;
import io.github.whoisamyy.ui.imgui.GuiBuilder;
import io.github.whoisamyy.utils.Utils;
import io.github.whoisamyy.utils.serialization.annotations.HideInInspector;
import io.github.whoisamyy.utils.structs.ClassHashSet;

import java.lang.reflect.InvocationTargetException;

public class InspectorGui {
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
                        if (c.isAnnotationPresent(HideInInspector.class)) continue;
                        if (ImGui.menuItem(c.getSimpleName())) {
                            try {

                                Component comp;
                                //noinspection unchecked
                                SceneViewGui.selectedGameObject.addComponent(
                                        comp = ((Class<? extends Component>) c).getDeclaredConstructor().newInstance()
                                );
                                comp.init();

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
