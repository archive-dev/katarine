package io.github.whoisamyy.katarine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import io.github.whoisamyy.core.Window;
import io.github.whoisamyy.editor.Editor;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
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
		Lwjgl3Application app = new Lwjgl3Application(Window.create(Editor.class, w, h), config);
	}
}
