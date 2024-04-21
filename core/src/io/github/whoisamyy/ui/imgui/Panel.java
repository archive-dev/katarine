package io.github.whoisamyy.ui.imgui;

import com.badlogic.gdx.Gdx;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;

/**
 * Acts as wrapper for everything between {@link imgui.ImGui#begin(String)} and {@link imgui.ImGui#end()}
 */
public final class Panel extends ImGuiObject {
    private String name;
    private Gui gui;
    /**
     * ImGuiWindowFlags
     */
    private int flags = ImGuiWindowFlags.None;

    public Panel(String name) {
        this.name = name;
    }

    public Panel(String name, Gui gui) {
        this.name = name;
        this.gui = gui;
    }

    public Panel(String name, int flags) {
        this.name = name;
        this.flags = flags;
    }

    public Panel(String name, Gui gui, int flags) {
        this.name = name;
        this.gui = gui;
        this.flags = flags;
    }

    public void setGui(Gui gui) {
        this.gui = gui;
    }

    public void render() {
        ImGui.begin(name, flags);

        if (ImGui.getWindowPosX()<0) ImGui.setWindowPos(0, ImGui.getWindowPosY());

        if (ImGui.getWindowPosX()>Gdx.graphics.getWidth()-ImGui.getWindowSizeX()) ImGui.setWindowPos(Gdx.graphics.getWidth()-ImGui.getWindowSizeX(),
                ImGui.getWindowPosY());

        if (ImGui.getWindowPosY()<0) ImGui.setWindowPos(ImGui.getWindowPosX(), 0);

        if (ImGui.getWindowPosY()>Gdx.graphics.getHeight()-ImGui.getWindowSizeY()) ImGui.setWindowPos(ImGui.getWindowPosX(),
                Gdx.graphics.getHeight()-ImGui.getWindowSizeY());

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

    public Gui getGui() {
        return gui;
    }
}
