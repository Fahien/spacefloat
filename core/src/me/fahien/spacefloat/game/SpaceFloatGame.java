package me.fahien.spacefloat.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.graphics.g3d.particles.batches.PointSpriteParticleBatch;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import me.fahien.spacefloat.actor.ControlMessageActor;
import me.fahien.spacefloat.component.MissionComponent;
import me.fahien.spacefloat.factory.GameObjectFactory;
import me.fahien.spacefloat.factory.HudFactory;
import me.fahien.spacefloat.camera.MainOrthographicCamera;
import me.fahien.spacefloat.camera.MainPerspectiveCamera;
import me.fahien.spacefloat.factory.MissionFactory;
import me.fahien.spacefloat.screen.ScreenEnumerator;
import me.fahien.spacefloat.screen.SpaceFloatScreen;
import me.fahien.spacefloat.system.CameraSystem;
import me.fahien.spacefloat.utils.SpaceFloatPreferences;

/**
 * SpaceFloat {@link Game}
 *
 * @author Fahien
 */
public class SpaceFloatGame extends Game {
	private static final String logo = "Powered by\n" +
			"╔═╗┌─┐┌─┐┌─┐┌─┐╔═╗┬  ┌─┐┌─┐┌┬┐\n" +
			"╚═╗├─┘├─┤│  ├┤ ╠╣ │  │ │├─┤ │ \n" +
			"╚═╝┴  ┴ ┴└─┘└─┘╚  ┴─┘└─┘┴ ┴ ┴ ";

	public static int LOGGER_LEVEL = Logger.INFO;

	private static final String SYSTEM_PATH = "system/";
	private static final String SYSTEM_FONT = SYSTEM_PATH + "font.fnt";
	private static final String SYSTEM_HUD = SYSTEM_PATH + "hud.atlas";

	public static Logger logger = new Logger(SpaceFloatGame.class.getSimpleName(), LOGGER_LEVEL);

	private AssetManager assetManager;

	private Json json;
	private GameObjectFactory gameObjectFactory;
	private MissionFactory missionFactory;

	private Engine engine;
	private Camera camera;
	private ParticleSystem particleSystem;

	private BitmapFont font;
	private TextureAtlas hud;
	private HudFactory hudFactory;

	private InputMultiplexer inputMultiplexer;

	private Viewport viewport;
	private Stage stage;

	/**
	 * Loads the {@link SpaceFloatPreferences}
	 */
	public void loadPreferences() {
		logger.info("Loading preferences");
		SpaceFloatPreferences preferences = new SpaceFloatPreferences();
		preferences.load();
		preferences.save();
	}

	/**
	 * Initializes the {@link Logger}
	 */
	public void initLogger() {
		Gdx.app.setLogLevel(LOGGER_LEVEL);
		logger.setLevel(LOGGER_LEVEL);
	}

	/**
	 * Initializes the {@link AssetManager}
	 */
	public void initAssetManager() {
		assetManager = new AssetManager();
	}

	/**
	 * Returns the {@link AssetManager}
	 */
	public AssetManager getAssetManager() {
		return assetManager;
	}

	/**
	 * Initializes {@link Json}
	 */
	public void initJson() {
		json = new Json();
	}

	/**
	 * Initializes the {@link Engine}
	 */
	public void initEngine() {
		engine = new Engine();
	}

	/**
	 * Returns the Ashley {@link Engine}
	 */
	public Engine getEngine() {
		return engine;
	}

	/**
	 * Initializes the {@link Camera}
	 */
	public void initCamera() {
		if (CameraSystem.CAMERA_TYPE.equals("perspective")) {
			camera = new MainPerspectiveCamera();
		} else {
			camera = new MainOrthographicCamera();
		}
	}

	/**
	 * Loads the {@link BitmapFont}
	 */
	protected void loadFont() {
		assetManager.load(SYSTEM_FONT, BitmapFont.class);
		assetManager.finishLoading();
		font = assetManager.get(SYSTEM_FONT);
	}

	/**
	 * Returns the {@link BitmapFont}
	 */
	public BitmapFont getFont() {
		return font;
	}

	/**
	 * Loads the HUD {@link TextureAtlas}
	 */
	public void loadHud() {
		assetManager.load(SYSTEM_HUD, TextureAtlas.class);
		assetManager.finishLoading();
		this.hud = assetManager.get(SYSTEM_HUD);
	}

	/**
	 * Returns the HUD {@link TextureAtlas}
	 */
	public TextureAtlas getHud() {
		return hud;
	}

	public void initFactories() {
		gameObjectFactory = GameObjectFactory.INSTANCE;
		gameObjectFactory.setJson(json);

		missionFactory = MissionFactory.INSTANCE;
		missionFactory.setJson(json);
		missionFactory.setEngine(engine);
		missionFactory.setMissionComponent(new MissionComponent());

		hudFactory = HudFactory.INSTANCE;
		hudFactory.setHud(hud);
		hudFactory.setFont(font);
	}

	/**
	 * Initializes the {@link Viewport} and the {@link Stage}
	 */
	private void initViewportAndStage() {
		logger.debug("Creating viewport");
		viewport = new FitViewport(SpaceFloatScreen.WIDTH, SpaceFloatScreen.HEIGHT);
		logger.debug("Creating stage");
		stage = new Stage(viewport);
		stage.setDebugAll(false);
		inputMultiplexer.addProcessor(stage);
	}

	/**
	 * Initializes the {@link ParticleSystem}
	 */
	private void initParticleSystem() {
		logger.debug("Creating particle system");
		particleSystem = ParticleSystem.get();
		particleSystem.removeAll();
		PointSpriteParticleBatch pointSpriteBatch = new PointSpriteParticleBatch();
		pointSpriteBatch.setCamera(camera);
		particleSystem.add(pointSpriteBatch);
	}

	/**
	 * Initializes the {@link InputMultiplexer}
	 */
	private void initInputMultiplexer() {
		logger.debug("Creating input multiplexer");
		inputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	/**
	 * Sets the screen using {@link ScreenEnumerator}
	 */
	public void setScreen(ScreenEnumerator screenEnumerator) {
		SpaceFloatScreen screen = screenEnumerator.getScreen();
		if (!screen.isInitialized()) {
			logger.debug("Injecting dependencies into " + screen.getClass().getSimpleName());
			injectDependencies(screen);
		}
		if (screen == this.screen) return;
		String currentScreen = "Null";
		if (this.screen != null) currentScreen = this.screen.getClass().getSimpleName();
		logger.debug("Switching screen: " + currentScreen + " → " + screen.getClass().getSimpleName());
		setScreen(screen);
	}

	/**
	 * Inject Dependencies in a {@link SpaceFloatScreen}
	 */
	private void injectDependencies(SpaceFloatScreen screen) {
		screen.setAssetManager(assetManager);
		screen.setGameObejctFactory(gameObjectFactory);
		screen.setFont(font);
		screen.setHud(hud);
		screen.setHudFactory(hudFactory);
		screen.setEngine(engine);
		screen.setCamera(camera);
		screen.setParticleSystem(particleSystem);
		screen.setInputMultiplexer(inputMultiplexer);
		screen.setViewport(viewport);
		screen.setStage(stage);
		screen.setGame(this);
		screen.setInitialized(true);
	}

	@Override
	public void create() {
		Gdx.app.setLogLevel(LOGGER_LEVEL);
		logger.info(logo);
		loadPreferences();
		initLogger();
		initAssetManager();
		initEngine();
		initJson();
		loadFont();
		loadHud();
		initCamera();
		initParticleSystem();
		initInputMultiplexer();
		initViewportAndStage();
		Bullet.init();
		initFactories();
		setScreen(ScreenEnumerator.LOADING);
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void dispose() {
		super.dispose();
		if (missionFactory != null) {
			logger.debug("Disposing mission");
			missionFactory.dispose();
		}
		if (stage != null) {
			logger.debug("Disposing stage");
			stage.dispose();
		}
		logger.debug("Disposing asset manager");
		assetManager.dispose();
		logger.debug("Game disposed");
	}

	/**
	 * Enqueue a {@link ControlMessageActor}
	 */
	public void enqueueMessage(String message) {
		stage.addActor(hudFactory.getMessageActor(message));
	}
}
