package org.katarine.editor.imgui;

import java.util.ArrayList;

public class AppendableGui implements Gui {
    private final ArrayList<Gui> guis = new ArrayList<>();

    @Override
    public void render() {
        guis.forEach(Gui::render);
    }

    public void add(Gui gui) {
        this.guis.add(gui);
    }

    public void removeGui(Gui gui) {
        this.guis.remove(gui);
    }

    public void removeGui(int index) {
        this.guis.remove(index);
    }
}
