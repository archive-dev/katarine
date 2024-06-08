package org.katarine.editor;

import com.badlogic.gdx.Gdx;
import imgui.ImGui;
import imgui.flag.ImGuiMouseButton;
import imgui.flag.ImGuiPopupFlags;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.flag.ImGuiWindowFlags;
import org.katarine.components.Component;
import org.katarine.editor.imgui.*;
import org.katarine.objects.EntityManager;
import org.katarine.objects.GameObject;
import org.katarine.scenes.Scene;
import org.katarine.utils.Utils;
import org.katarine.utils.input.InputSystem;
import org.katarine.utils.structs.ClassHashSet;
import org.katarine.utils.structs.tree.Node;
import org.katarine.utils.structs.tree.Tree;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;

import static org.katarine.editor.Inspector.componentsClasses;

class SceneHierarchy {
    private static final int leafFlags = ImGuiTreeNodeFlags.OpenOnDoubleClick | ImGuiTreeNodeFlags.Leaf;
    private static final int treeNodeFlags = ImGuiTreeNodeFlags.OpenOnArrow | ImGuiTreeNodeFlags.OpenOnDoubleClick
            | ImGuiTreeNodeFlags.SpanAvailWidth;

    private final EntityManager entityManager;

    public final Gui ctxGui;

    private final Scene scene;

    public SceneHierarchy(Scene scene) {
        this.scene = scene;
        this.entityManager = scene.getEntityManager();

        this.ctxGui = () -> {
            if (imgui.ImGui.beginPopupContextWindow(ImGuiPopupFlags.MouseButtonRight) ||
                    imgui.ImGui.beginPopupContextVoid(ImGuiPopupFlags.MouseButtonRight)) {
                if (imgui.ImGui.treeNodeEx("New object", ImGuiTreeNodeFlags.Leaf)) {
                    if (imgui.ImGui.isItemClicked(0)) {
                        GameObject go = this.entityManager.instantiate(GameObject.class);
                        go.create();
                        go.transform.pos.set(InputSystem.getMoveEvent().getMousePosition());
                    }
                    imgui.ImGui.treePop();
                }

                imgui.ImGui.endPopup();
            }
        };
    }

    final HashMap<GameObject, Gui> selection = new HashMap<>();

    public Panel generatePanel() {
        Panel ret = new Panel("Hierarchy");
        var gui = new ScenePanelGui(scene.getGameObjectTree(), scene);
        gui.add(ctxGui);
        ret.setGui(gui);

        return ret;
    }

    static class ScenePanelGui extends AppendableGui {
        private final Tree<GameObject> tree;
        private final Scene scene;

        public ScenePanelGui(Tree<GameObject> tree, Scene scene) {
            this.tree = tree;
            this.scene = scene;
        }

        @Override
        public void render() {
            int selected = 0;

            if (ImGui.treeNodeEx(scene.getName(), treeNodeFlags | selected)) {
                for (var c : tree.getChildren()) {
                    render(c, scene);
                }
                ImGui.treePop();
            }
            super.render();
        }

        private void render(Node<GameObject> tree, Scene scene) {
            int selected = 0;
            if (tree.get()!=null) {
                EditorObjectComponent rootEoc = tree.get().getComponent(EditorObjectComponent.class);
                selected = rootEoc.selected ? 1 : 0;
            } else {
                var root = new GameObject();
                root.setName(scene.getName());
                tree.set(root);
            }

            if (!tree.getChildren().isEmpty()) {
                if (ImGui.treeNodeEx(tree.get().getName(), treeNodeFlags | selected)) {
                    if (ImGui.isItemClicked(ImGuiMouseButton.Left)) {
                        if (!ImGui.getIO().getKeyCtrl()) {
                            EditorObjectComponent.selection.forEach(go->
                                    go.getComponent(EditorObjectComponent.class).selected = false);
                            EditorObjectComponent.selection.clear();
                        }
                        boolean b;
                        b = tree.get().getComponent(EditorObjectComponent.class).selected =
                                !tree.get().getComponent(EditorObjectComponent.class).selected;
                        if (b)
                            EditorObjectComponent.selection.add(tree.get());
                    }

                    for (var c : tree.getChildren()) {
                        render(c, scene);
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
        }
    }
}

class Inspector implements Gui {
    static final ClassHashSet componentsClasses = new ClassHashSet();
    static {
        componentsClasses.addAll(Utils.loadedClasses.getSubClassesOf(Component.class));
    }

    private final Gui ctxMenu;
    private final GameObject object;
    private final ArrayList<Component> components = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public Inspector(GameObject object) {
        this.object = object;
        this.components.addAll(object.getComponents());

        this.ctxMenu = () -> {
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
//                                Component comp;

                                object.addComponent(
                                        ((Class<? extends Component>) c).getDeclaredConstructor().newInstance()
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

    @Override
    public void render() {
        components.forEach(component -> {
            if (ImGui.treeNodeEx(component.getClass().getTypeName())) {
                GuiGenerator.generate(component).render();

                ImGui.treePop();
            }
        });

        ctxMenu.render();
    }
}

class InspectorPanel implements Gui {
    private final GameObject object;
    private final EditorObjectComponent eoc;

    public InspectorPanel(GameObject object) {
        this.object = object;
        this.eoc = object.getComponent(EditorObjectComponent.class);
    }

    @Override
    public void render() {
        if (!eoc.selected) return;

        imgui.ImGui.begin(object.getName()+"###", ImGuiWindowFlags.None);

        if (imgui.ImGui.getWindowPosX()<0) imgui.ImGui.setWindowPos(0, imgui.ImGui.getWindowPosY());

        if (imgui.ImGui.getWindowPosX()> Gdx.graphics.getWidth()- imgui.ImGui.getWindowSizeX()) imgui.ImGui.setWindowPos(Gdx.graphics.getWidth()- imgui.ImGui.getWindowSizeX(),
                imgui.ImGui.getWindowPosY());

        if (imgui.ImGui.getWindowPosY()<0) imgui.ImGui.setWindowPos(imgui.ImGui.getWindowPosX(), 0);

        if (imgui.ImGui.getWindowPosY()>Gdx.graphics.getHeight()- imgui.ImGui.getWindowSizeY()) imgui.ImGui.setWindowPos(imgui.ImGui.getWindowPosX(),
                Gdx.graphics.getHeight()- imgui.ImGui.getWindowSizeY());


        object.getComponents().forEach(component -> {
            if (component.getClass().isAnnotationPresent(HideInInspector.class)) return;
            if (imgui.ImGui.treeNodeEx(component.getClass().getTypeName())) {
                GuiGenerator.generate(component).render();

                imgui.ImGui.treePop();
            }
        });

        imgui.ImGui.end();

        if (imgui.ImGui.beginPopupContextWindow(ImGuiPopupFlags.MouseButtonMiddle)) {
            if (!imgui.ImGui.beginMenu("Add Component")) {
                imgui.ImGui.endPopup();
                return;
            }
            if (imgui.ImGui.beginListBox("Components")) {
                for (var c : componentsClasses) {
                    if (c.isAnnotationPresent(HideInInspector.class) | Modifier.isAbstract(c.getModifiers())) continue;
                    if (imgui.ImGui.menuItem(c.getSimpleName())) {
                        try {
//                                Component comp;

                            //noinspection unchecked
                            object.addComponent(
                                    ((Class<? extends Component>) c).getDeclaredConstructor().newInstance()
                            );

                        } catch (InstantiationException | IllegalAccessException |
                                 InvocationTargetException | NoSuchMethodException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                imgui.ImGui.endListBox();
            }
            imgui.ImGui.endMenu();

            imgui.ImGui.endPopup();
        }
    }
}
