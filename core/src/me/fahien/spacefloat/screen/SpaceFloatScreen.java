package me.fahien.spacefloat.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

import me.fahien.spacefloat.factory.GameObjectFactory;
import me.fahien.spacefloat.factory.HudFactory;
import me.fahien.spacefloat.game.SpaceFloatGame;

import static java.lang.Math.min;
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

	private GameObjectFactory gameObjectFactory;
	private HudFactory hudFactory;

	private Engine engine;
	private Camera camera;

	private InputMultiplexer inputMultiplexer;

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
	public void setInitialized(final boolean initialized) {
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
	public void setGame(final SpaceFloatGame game) {
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
	public void setAssetManager(final AssetManager assetManager) {
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
	public void setParticleSystem(final ParticleSystem particleSystem) {
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
	public void setFont(final BitmapFont font) {
		this.font = font;
	}

	/**
	 * Returns the {@link GameObjectFactory}
	 */
	public GameObjectFactory getGameObjectFactory() {
		return gameObjectFactory;
	}

	/**
	 * Sets the {@link GameObjectFactory}
	 */
	public void setGameObejctFactory(final GameObjectFactory gameObejctFactory) {
		this.gameObjectFactory = gameObejctFactory;
	}

	/**
	 * Returns the HUD {@link TextureAtlas}
	 */
	public TextureAtlas getHud() {
		return hud;
	}

	/**
	 * Sets the HUD {@link TextureAtlas}
	 */
	public void setHud(final TextureAtlas hud) {
		this.hud = hud;
	}

	/**
	 * Returns the {@link HudFactory}
	 */
	public HudFactory getHudFactory() {
		return hudFactory;
	}

	/**
	 * sets the {@link HudFactory}
	 */
	public void setHudFactory(final HudFactory hudFactory) {
		this.hudFactory = hudFactory;
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
	public void setEngine(final Engine engine) {
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
	public void setCamera(final Camera camera) {
		this.camera = camera;
	}

	/**
	 * Returns the {@link InputMultiplexer}
	 */
	public InputMultiplexer getInputMultiplexer() {
		return inputMultiplexer;
	}

	/**
	 * Sets the {@link InputMultiplexer}
	 */
	public void setInputMultiplexer(final InputMultiplexer inputMultiplexer) {
		this.inputMultiplexer = inputMultiplexer;
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
	public void setViewport(final Viewport viewport) {
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
	public void setStage(final Stage stage) {
		this.stage = stage;
	}

	/**
	 * Populates the {@link Stage}
	 */
	public void populate(final Stage stage) {}

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
		delta = min(0.00001f, delta);
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
		hudFactory = null;
		assetManager = null;
		inputMultiplexer = null;
		viewport = null;
		if (stage != null) stage.getRoot().clearChildren();
		stage = null;
		initialized = false;
	}
}
