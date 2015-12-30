package me.fahien.spacefloat.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import me.fahien.spacefloat.game.SpaceFloatGame;

/**
 * Space Float {@link Screen}
 *
 * @author Fahien
 */
public class SpaceFloatScreen implements Screen {
	public static final int WIDTH = 480;
	public static final int HEIGHT = 270;

	private boolean initialized;
	private SpaceFloatGame game;
	private AssetManager assetManager;
	private BitmapFont font;
	private TextureAtlas hud;
	private Engine engine;

	/**
	 * Tests whether is initialized
	 */
	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * Sets initialized
	 */
	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	/**
	 * Returns the {@link SpaceFloatGame}
	 */
	public SpaceFloatGame getGame() {
		return game;
	}

	/**
	 * Sets the {@link SpaceFloatGame}
	 */
	public void setGame(SpaceFloatGame game) {
		this.game = game;
	}

	/**
	 * Returns the {@link AssetManager}
	 */
	public AssetManager getAssetManager() {
		return assetManager;
	}

	/**
	 * Sets the {@link AssetManager}
	 */
	public void setAssetManager(AssetManager assetManager) {
		this.assetManager = assetManager;
	}

	/**
	 * Returns the {@link BitmapFont}
	 */
	public BitmapFont getFont() {
		return font;
	}

	/**
	 * Sets the {@link BitmapFont}
	 */
	public void setFont(BitmapFont font) {
		this.font = font;
	}

	/**
	 * Sets the HUD {@link TextureAtlas}
	 */
	public void setHud(TextureAtlas hud) {
		this.hud = hud;
	}

	/**
	 * Returns the HUD {@link TextureAtlas}
	 */
	public TextureAtlas getHud() {
		return hud;
	}

	/**
	 * Returns the Ashley {@link Engine}
	 */
	public Engine getEngine() {
		return engine;
	}

	/**
	 * Sets the Ashley {@link Engine}
	 */
	public void setEngine(Engine engine) {
		this.engine = engine;
	}

	@Override
	public void show() {}

	@Override
	public void render(float delta) {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
	}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void dispose() {
		engine = null;
		font = null;
		hud = null;
		assetManager = null;
		initialized = false;
	}
}
