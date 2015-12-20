package me.fahien.spacefloat.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import me.fahien.spacefloat.game.SpaceFloatGame;

public class DesktopLauncher {
	private static final String WINDOW_TITLE = "SpaceFloat";
	private static final int WINDOW_WIDTH = 480;
	private static final int WINDOW_HEIGHT = 270;

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = WINDOW_TITLE;
		config.width = WINDOW_WIDTH;
		config.height = WINDOW_HEIGHT;
		new LwjglApplication(new SpaceFloatGame(), config);
	}
}
