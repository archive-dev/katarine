package org.katarine;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import org.katarine.Game;
import org.katarine.core.Window;
import org.katarine.utils.Utils;

import java.io.IOException;

public class DesktopGameLauncher {
    public static void main (String[] arg) {
        try {
            Utils.init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.useVsync(false);
        config.setForegroundFPS(0);
        config.setIdleFPS(1);
        config.setTitle("Katarine");
        int w = 1280, h = 720;
        config.setWindowedMode(w, h);
        config.useVsync(false);
        config.setResizable(true);
        config.setWindowSizeLimits(1280, 720, 1280*5, 720*5);
        Lwjgl3Application app = new Lwjgl3Application(Window.create(Game.class, w, h), config);
    }
}
