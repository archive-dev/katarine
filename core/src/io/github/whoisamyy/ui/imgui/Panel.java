package io.github.whoisamyy.ui.imgui;

import imgui.ImGui;

/**
 * Acts as wrapper for everything between {@link imgui.ImGui#begin(String)} and {@link imgui.ImGui#end()}
 */
public final class Panel extends ImGUIObject {
    private String name;
    private Gui gui;

    public Panel(String name) {
        this.name = name;
    }

    public Panel(String name, Gui gui) {
        this.name = name;
        this.gui = gui;
    }

    public void setGui(Gui gui) {
        this.gui = gui;
    }

    public void render() {
        ImGui.begin(name);
        gui.render();
        ImGui.end();
    }

    public static void startPanel(String name, Gui gui) {
        ImGui.begin(name);
        gui.render();
        ImGui.end();
    }

    public static void startPanel(Gui gui) {
        startPanel("null", gui);
    }
}
