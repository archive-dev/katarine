package io.github.whoisamyy.editor;

import imgui.ImGui;
import imgui.flag.ImGuiMouseButton;
import imgui.flag.ImGuiPopupFlags;
import imgui.flag.ImGuiTreeNodeFlags;
import io.github.whoisamyy.editor.components.EditorObjectComponent;
import io.github.whoisamyy.logging.LogLevel;
import io.github.whoisamyy.logging.Logger;
import io.github.whoisamyy.objects.GameObject;
import io.github.whoisamyy.ui.imgui.AppendableGui;
import io.github.whoisamyy.ui.imgui.Gui;
import io.github.whoisamyy.ui.imgui.GuiBuilder;
import io.github.whoisamyy.utils.input.AbstractInputHandler;
import io.github.whoisamyy.utils.structs.tree.Node;

public class SceneViewGui {
    private static final int leafFlags = ImGuiTreeNodeFlags.OpenOnDoubleClick | ImGuiTreeNodeFlags.Leaf;
    private static final int treeNodeFlags = ImGuiTreeNodeFlags.OpenOnArrow | ImGuiTreeNodeFlags.OpenOnDoubleClick
            | ImGuiTreeNodeFlags.SpanAvailWidth;

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

    private static final Gui ctxMenu;
    static {
        ctxMenu = () -> {
            if (imgui.ImGui.beginPopupContextWindow(ImGuiPopupFlags.MouseButtonRight)) {
                if (imgui.ImGui.treeNodeEx("New object", ImGuiTreeNodeFlags.Leaf)) {
                    if (imgui.ImGui.isItemClicked(0)) {
                        GameObject go = GameObject.instantiate(GameObject.class);
                        go.create();
                        go.transform.pos.set(AbstractInputHandler.getMoveEvent().getMousePosition());
                        new Logger(LogLevel.DEBUG).debug(go.transform.pos);
                    }
                    imgui.ImGui.treePop();
                }

                imgui.ImGui.endPopup();
            }
        };
    }

    public Gui generateGameObjectTree(GameObject obj) {
        return () -> generateGameObjectTree(obj.toTree()).render();
    }

    public static Gui getCtxMenu() {
        return ctxMenu;
    }
}