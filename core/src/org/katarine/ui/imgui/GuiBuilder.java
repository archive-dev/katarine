package org.katarine.ui.imgui;

public class GuiBuilder {
    private final AppendableGui gui = new AppendableGui();

    public GuiBuilder() {}

    public GuiBuilder add(Gui gui) {
        this.gui.add(gui);
        return this;
    }

    public AppendableGui get() {
        return this.gui;
    }
}