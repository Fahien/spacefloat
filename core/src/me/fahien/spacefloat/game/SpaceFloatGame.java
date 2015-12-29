package me.fahien.spacefloat.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.utils.Logger;

import me.fahien.spacefloat.screen.SpaceFloatScreen;
import me.fahien.spacefloat.screen.ScreenEnumerator;

/**
 * Proto Fast {@link Game}
 *
 * @author Fahien
 */
public class SpaceFloatGame extends Game {
	private static final String logo = "Powered by\n" +
			"╔═╗┌─┐┌─┐┌─┐┌─┐╔═╗┬  ┌─┐┌─┐┌┬┐\n" +
			"╚═╗├─┘├─┤│  ├┤ ╠╣ │  │ │├─┤ │ \n" +
			"╚═╝┴  ┴ ┴└─┘└─┘╚  ┴─┘└─┘┴ ┴ ┴ ";

	private static final int LOGGER_LEVEL = Logger.DEBUG;
	private static final String SYSTEM_PATH = "system/";
	private static final String SYSTEM_FONT = SYSTEM_PATH + "font.fnt";

	public static final Logger logger = new Logger(SpaceFloatGame.class.getSimpleName(), LOGGER_LEVEL);

	private AssetManager assetManager;
	private BitmapFont font;
	private Engine engine;

	public SpaceFloatGame() {
		assetManager = new AssetManager();
		engine = new Engine();
	}

	/**
	 * Returns the {@link AssetManager}
	 */
	public AssetManager getAssetManager() {
		return assetManager;
	}

	/**
	 * Returns the {@link BitmapFont}
	 */
	public BitmapFont getFont() {
		return font;
	}


	/**
	 * Returns the Ashley {@link Engine}
	 */
	public Engine getEngine() {
		return engine;
	}

	protected void loadFont() {
		assetManager.load(SYSTEM_FONT, BitmapFont.class);
		assetManager.finishLoading();
		font = assetManager.get(SYSTEM_FONT);
	}

	/**
	 * Sets the screen using {@link ScreenEnumerator}
	 */
	public void setScreen(ScreenEnumerator screenEnumerator) {
		SpaceFloatScreen screen = screenEnumerator.getScreen();
		if (!screen.isInitialized()) injectDependencies(screen);
		setScreen(screen);
	}

	/**
	 * Inject Dependencies in a {@link SpaceFloatScreen}
	 */
	private void injectDependencies(SpaceFloatScreen screen) {
		screen.setAssetManager(assetManager);
		screen.setFont(font);
		screen.setEngine(engine);
		screen.setGame(this);
		screen.setInitialized(true);
	}

	@Override
	public void create() {
		Gdx.app.setLogLevel(LOGGER_LEVEL);
		logger.info(logo);
		loadFont();
		Bullet.init();
		setScreen(ScreenEnumerator.LOADING);
		logger.debug("Game initialized");
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		super.dispose();
		assetManager.dispose();
		logger.debug("Game disposed");
	}
}
