package io.github.whoisamyy.test;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import io.github.whoisamyy.editor.Editor;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(144);
		config.setTitle("My GDX Game");
		int w = 1280, h = 720;
		config.setWindowedMode(w, h);
		config.useVsync(false);
		config.setResizable(true);
		config.setWindowSizeLimits(1280, 720, 1280*5, 720*5);
		Lwjgl3Application app = new Lwjgl3Application(new Editor(w, h), config);
	}
}
