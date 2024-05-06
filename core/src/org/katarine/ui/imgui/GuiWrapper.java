package org.katarine.ui.imgui;

public class GuiWrapper implements Gui {
    private Gui gui = null;

    public final Gui getGui() {
        return gui;
    }

    public final void setGui(Gui gui) {
        this.gui = gui;
    }

    @Override
    public void render() {
        if (gui != null) gui.render();
    }
}
