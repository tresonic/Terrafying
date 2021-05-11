package com.lufi.terrafying.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.lufi.terrafying.Terrafying;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		config.title = "Terrafying";
		// add icon in the future!!!
		// config.addIcon(path, fileType);
		new LwjglApplication(new Terrafying(), config);
	} 
}
