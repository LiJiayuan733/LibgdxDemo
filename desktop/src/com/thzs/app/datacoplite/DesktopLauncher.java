package com.thzs.app.datacoplite;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import static com.thzs.app.datacoplite.core.Game.APP_TITLE;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(120);
		config.setDecorated(false);
		config.setTitle(APP_TITLE);
		config.setWindowIcon(Files.FileType.Internal,"logo.png");;
		config.setBackBufferConfig(8,8,8,8,16,0,20);
		config.setWindowedMode(1920,1080);
		new Lwjgl3Application(new Views(), config);
	}
}
