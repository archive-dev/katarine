package org.katarine.editor.imgui;

import org.katarine.systems.System;

public class ImGuiSystem extends System {
    @Override
    protected void start() {
        if (!ImGui.isInitialized) ImGui.init();
    }

    @Override
    protected void postUpdate() {
        ImGui.render();
    }
}
