package com.lufi.terrafying.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.esotericsoftware.minlog.Log;
import com.lufi.terrafying.Terrafying;
import com.lufi.terrafying.net.TerrafyingServer;

public class DesktopLauncher {
	public static void main (String[] arg) {		
		Log.set(Log.LEVEL_WARN);
		if(arg.length > 0 && arg[0].contentEquals("server")) {
			if(arg[1] != null)
				TerrafyingServer.the().start(arg[1]);
			else 
				TerrafyingServer.the().start("world");
		} else {
			LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
			config.width = 1280;
			config.height = 720;
			config.title = "Terrafying";
			config.addIcon("icon.png", FileType.Internal);
			new LwjglApplication(new Terrafying(), config);
		}
	} 
}
