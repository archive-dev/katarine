package io.github.whoisamyy.ui.imgui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import imgui.ImGuiIO;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

import java.util.HashSet;

public final class ImGui {
    private static ImGuiImplGlfw imGuiGlfw;
    private static ImGuiImplGl3 imGuiGl3;

    public static void init() {
        imGuiGlfw = new ImGuiImplGlfw();
        imGuiGl3 = new ImGuiImplGl3();
        long windowHandle = ((Lwjgl3Graphics) Gdx.graphics).getWindow().getWindowHandle();
        imgui.ImGui.createContext();
        ImGuiIO io = imgui.ImGui.getIO();
        io.setIniFilename(null);
        io.getFonts().addFontDefault();
        io.getFonts().build();
        imGuiGlfw.init(windowHandle, true);
        imGuiGl3.init("#version 330 core");
    }

    private static InputProcessor tmp;
    private static void start() {
        if (tmp != null) {
            Gdx.input.setInputProcessor(tmp);
            tmp = null;
        }

        imGuiGlfw.newFrame();
        imgui.ImGui.newFrame();

        if (gui != null) gui.render();
        panels.forEach(Panel::render);
    }

    private static Gui gui;

    public static void render() {
        start();
        end();
    }

    private static final HashSet<Panel> panels = new HashSet<>();

    public static void addPanel(Panel panel) {
        panels.add(panel);
    }

    public static void setGui(Gui gui) {
        ImGui.gui = gui;
    }

    private static void end() {
        imgui.ImGui.render();
        imGuiGl3.renderDrawData(imgui.ImGui.getDrawData());

        if (imgui.ImGui.getIO().getWantCaptureKeyboard() || imgui.ImGui.getIO().getWantCaptureMouse()) {
            tmp = Gdx.input.getInputProcessor();
            Gdx.input.setInputProcessor(null);
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
