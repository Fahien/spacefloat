package me.fahien.spacefloat.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import me.fahien.spacefloat.game.SpaceFloat;
import me.fahien.spacefloat.game.SpaceFloatGame;

public class DesktopLauncher {
	private static final String WINDOW_TITLE = "SpaceFloat v" + SpaceFloatGame.VERSION;

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = WINDOW_TITLE;
		config.width = SpaceFloatGame.WINDOW_WIDTH;
		config.height = SpaceFloatGame.WINDOW_HEIGHT;
		new LwjglApplication(SpaceFloat.GAME.getGame(), config);
	}
}
