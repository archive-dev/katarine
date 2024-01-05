package io.github.whoisamyy.test;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(144);
		config.setTitle("My GDX Game");
		int w = 1280, h = 760;
		config.setWindowedMode(w, h);
		config.useVsync(false);
		config.setResizable(false);
		Lwjgl3Application app = new Lwjgl3Application(new Game(w, h), config);
	}
}
