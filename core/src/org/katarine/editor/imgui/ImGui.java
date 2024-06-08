package org.katarine.editor.imgui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import imgui.ImGuiIO;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

import java.util.HashMap;
import java.util.HashSet;

public final class ImGui {
    public static boolean controlsInput = false;
    private static ImGuiImplGlfw imGuiGlfw;
    private static ImGuiImplGl3 imGuiGl3;
    private static long windowHandle;
    static boolean isInitialized;

    public static void init() {
        if (isInitialized) return;
        imGuiGlfw = new ImGuiImplGlfw();
        imGuiGl3 = new ImGuiImplGl3();
        windowHandle = ((Lwjgl3Graphics) Gdx.graphics).getWindow().getWindowHandle();
        imgui.ImGui.createContext();
        ImGuiIO io = imgui.ImGui.getIO();
        io.setIniFilename(null);
        io.getFonts().addFontDefault();
        io.getFonts().build();
        imGuiGlfw.init(windowHandle, true);
        imGuiGl3.init("#version 330 core");
        isInitialized = true;
    }

    private static InputProcessor tmp;
    static void start() {
        if (tmp != null) {
            Gdx.input.setInputProcessor(tmp);
            tmp = null;
            controlsInput = false;
        }
        imGuiGlfw.newFrame();
        imgui.ImGui.newFrame();

//        System.out.println(panels);
    }

    private static Gui gui;

    public static synchronized void render() {
        start();
        if (gui != null) gui.render();

        for (var gui : guis.values()) {
            gui.render();
        }
        panels.addAll(panelAddition);
        panelAddition.clear();
        panels.forEach(panel -> {
            if (panel.getGui()!=null)
                panel.render();
        });


        end();
    }

    public static final HashMap<String, Gui> guis = new HashMap<>();

    private static final HashSet<Panel> panels = new HashSet<>();

    private static final HashSet<Panel> panelAddition = new HashSet<>();

    public static void addPanel(Panel panel) {
        panelAddition.add(panel);
    }
    public static void removePanel(Panel panel) {
        panels.remove(panel);
    }
    public static void setGui(Gui gui) {
        ImGui.gui = gui;
    }

    static void end() {
        imgui.ImGui.render();
        imGuiGl3.renderDrawData(imgui.ImGui.getDrawData());

        if (imgui.ImGui.getIO().getWantCaptureKeyboard() || imgui.ImGui.getIO().getWantCaptureMouse()) {
            tmp = Gdx.input.getInputProcessor();
            Gdx.input.setInputProcessor(null);
            controlsInput = true;
        }
    }

    public static void dispose() {
        imGuiGl3.dispose();
        imGuiGl3 = null;
        imGuiGlfw.dispose();
        imGuiGlfw = null;
        imgui.ImGui.destroyContext();
    }
}
