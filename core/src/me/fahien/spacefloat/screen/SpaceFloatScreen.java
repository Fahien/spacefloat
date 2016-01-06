package me.fahien.spacefloat.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

import me.fahien.spacefloat.game.SpaceFloatGame;

import static me.fahien.spacefloat.game.SpaceFloatGame.logger;

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
	private ParticleSystem particleSystem;
	private AssetManager assetManager;
	private BitmapFont font;
	private TextureAtlas hud;
	private Engine engine;
	private Camera camera;
	private Viewport viewport;
	private Stage stage;

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
	 * Returns the {@link ParticleSystem}
	 */
	public ParticleSystem getParticleSystem() {
		return particleSystem;
	}

	/**
	 * Sets the {@link ParticleSystem}
	 */
	public void setParticleSystem(ParticleSystem particleSystem) {
		this.particleSystem = particleSystem;
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

	/**
	 * Returns the {@link Camera}
	 */
	public Camera getCamera() {
		return camera;
	}

	/**
	 * Sets the {@link Camera}
	 */
	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	/**
	 * Returns the {@link Viewport}
	 */
	public Viewport getViewport() {
		return viewport;
	}

	/**
	 * Sets the {@link Viewport}
	 */
	public void setViewport(Viewport viewport) {
		this.viewport = viewport;
	}

	/**
	 * Returns the {@link Stage}
	 */
	public Stage getStage() {
		return stage;
	}

	/**
	 * Sets the {@link Stage}
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	/**
	 * Populates the {@link Stage}
	 */
	public void populate(Stage stage) {}

	@Override
	public void show() {
		logger.debug("Showing screen");
		Gdx.input.setCatchBackKey(true);
		logger.debug("Populating stage");
		populate(stage);
	}

	/**
	 * Update is called before drawing the stage
	 */
	public void update(float delta) {
		stage.act(delta);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
			logger.debug("Exiting");
			Gdx.app.exit();
		}
		update(delta);
		if (stage != null) stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		if (viewport != null) viewport.update(width, height);
	}

	@Override
	public void pause() {
		logger.debug("Pausing screen");
	}

	@Override
	public void resume() {
		logger.debug("Resuming screen");
	}

	@Override
	public void hide() {
		logger.debug("Hiding screen");
		dispose();
	}

	@Override
	public void dispose() {
		logger.debug("Disposing screen");
		engine = null;
		font = null;
		hud = null;
		assetManager = null;
		viewport = null;
		if (stage != null) stage.clear();
		stage = null;
		initialized = false;
	}
}
