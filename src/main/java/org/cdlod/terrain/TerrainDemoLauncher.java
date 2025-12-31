package org.cdlod.terrain;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class TerrainDemoLauncher {
	public static void main(String[] args) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		config.setTitle("Simple Terrain Demo");
		config.setWindowedMode(1280, 720);
		config.useVsync(true);
		config.setForegroundFPS(60);

		new Lwjgl3Application(new TerrainDemo(), config);
	}
}

