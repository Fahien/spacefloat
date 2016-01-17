package me.fahien.spacefloat.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.graphics.g3d.particles.batches.PointSpriteParticleBatch;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import me.fahien.spacefloat.actor.ControlMessageActor;
import me.fahien.spacefloat.audio.Audio;
import me.fahien.spacefloat.camera.MainOrthographicCamera;
import me.fahien.spacefloat.camera.MainPerspectiveCamera;
import me.fahien.spacefloat.controller.ReactorController;
import me.fahien.spacefloat.entity.GameObject;
import me.fahien.spacefloat.factory.GameObjectFactory;
import me.fahien.spacefloat.factory.HudFactory;
import me.fahien.spacefloat.factory.MissionFactory;
import me.fahien.spacefloat.screen.ScreenEnumerator;
import me.fahien.spacefloat.screen.SpaceFloatScreen;
import me.fahien.spacefloat.system.BulletSystem;
import me.fahien.spacefloat.system.CameraSystem;
import me.fahien.spacefloat.system.DestinationSystem;
import me.fahien.spacefloat.system.EmissionSystem;
import me.fahien.spacefloat.system.MissionSystem;
import me.fahien.spacefloat.system.RenderSystem;
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

	public static final String VERSION = "0.18";

	public static int LOGGER_LEVEL = Logger.DEBUG;

	public static final int WINDOW_WIDTH = 960;
	public static final int WINDOW_HEIGHT = 540;

	public static boolean DEBUG_ALL = false;

	private static final String SYSTEM_PATH = "system/";
	private static final String CURSOR_IMAGE = SYSTEM_PATH + "cursor.png";
	private static final String SYSTEM_FONT = SYSTEM_PATH + "font.fnt";
	private static final String SYSTEM_HUD = SYSTEM_PATH + "hud.atlas";

	public static Logger logger = new Logger(SpaceFloatGame.class.getSimpleName(), LOGGER_LEVEL);

	private AssetManager assetManager;
	private Audio audio;

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

	private ReactorController reactorController;
	private CameraSystem cameraSystem;
	private RenderSystem renderSystem;
	private BulletSystem bulletSystem;
	private DestinationSystem destinationSystem;
	private MissionSystem missionSystem;
	private EmissionSystem emissionSystem;

	private GameObject player;

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
	 * Loads the cursor
	 */
	public void loadCursor(final AssetManager assetManager, final Graphics graphics) {
		assetManager.load(CURSOR_IMAGE, Pixmap.class);
		assetManager.finishLoading();
		Pixmap cursor = assetManager.get(CURSOR_IMAGE, Pixmap.class);
		Cursor customCursor = graphics.newCursor(cursor, 1, 1);
		Gdx.graphics.setCursor(customCursor);
	}

	/**
	 * Returns the {@link AssetManager}
	 */
	public AssetManager getAssetManager() {
		return assetManager;
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
	public Camera createCamera(final String cameraType) {
		if (cameraType.equals("perspective")) {
			camera = new MainPerspectiveCamera();
		} else {
			camera = new MainOrthographicCamera();
		}
		return camera;
	}

	/**
	 * Loads the {@link BitmapFont}
	 */
	protected BitmapFont loadFont(final AssetManager assetManager) {
		assetManager.load(SYSTEM_FONT, BitmapFont.class);
		assetManager.finishLoading();
		return assetManager.get(SYSTEM_FONT, BitmapFont.class);
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
	public TextureAtlas loadHud(final AssetManager assetManager) {
		assetManager.load(SYSTEM_HUD, TextureAtlas.class);
		assetManager.finishLoading();
		return assetManager.get(SYSTEM_HUD, TextureAtlas.class);
	}

	/**
	 * Returns the HUD {@link TextureAtlas}
	 */
	public TextureAtlas getHud() {
		return hud;
	}

	public void initFactories(final Json json) {
		logger.debug("Initializing factories");
		gameObjectFactory = GameObjectFactory.INSTANCE;
		gameObjectFactory.setJson(json);

		missionFactory = MissionFactory.INSTANCE;
		missionFactory.setJson(json);
		missionFactory.setEngine(engine);
	}

	private Sound loadSelectSound(final AssetManager assetManager) {
		assetManager.load(Audio.SOUNDS_DIR + Audio.SELECT_SOUND, Sound.class);
		assetManager.finishLoading();
		return assetManager.get(Audio.SOUNDS_DIR + Audio.SELECT_SOUND, Sound.class);
	}

	private HudFactory createHudFactory(final TextureAtlas hud,
										final BitmapFont font,
										final Audio audio,
										final Sound selectSound,
										final Graphics graphics,
										final Application app) {
		HudFactory hudFactory = HudFactory.INSTANCE;
		hudFactory.setHud(hud);
		hudFactory.setFont(font);
		hudFactory.setAudio(audio);
		hudFactory.setSelectSound(selectSound);
		hudFactory.setGraphics(graphics);
		hudFactory.setApplication(app);
		return hudFactory;
	}

	/**
	 * Initializes the {@link InputMultiplexer}
	 */
	private InputMultiplexer createInputMultiplexer(final Input input) {
		logger.debug("Creating input multiplexer");
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		input.setInputProcessor(inputMultiplexer);
		return inputMultiplexer;
	}

	/**
	 * Initializes the {@link Viewport} and the {@link Stage}
	 */
	private void initViewportAndStage(final InputMultiplexer inputMultiplexer) {
		logger.debug("Creating viewport");
		viewport = new FitViewport(SpaceFloatScreen.WIDTH, SpaceFloatScreen.HEIGHT);
		logger.debug("Creating stage");
		stage = new Stage(viewport);
		stage.setDebugAll(DEBUG_ALL);
		stage.getRoot().addListener(new InputListener() {
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				if (keycode == Input.Keys.ESCAPE && getScreen() == ScreenEnumerator.GAME.getScreen()) {
					setScreen(ScreenEnumerator.MAIN);
					return true;
				}
				if (keycode == Input.Keys.BACK) {
					if (getScreen() == ScreenEnumerator.GAME.getScreen()) {
						setScreen(ScreenEnumerator.MAIN);
					}
					return true;
				}
				return false;
			}
		});
		inputMultiplexer.addProcessor(stage);
		Gdx.input.setCatchBackKey(true);
	}

	/**
	 * Initializes the {@link ParticleSystem}
	 */
	private ParticleSystem createParticleSystem(final Camera camera) {
		logger.debug("Creating particle system");
		particleSystem = ParticleSystem.get();
		particleSystem.removeAll();
		PointSpriteParticleBatch pointSpriteBatch = new PointSpriteParticleBatch();
		pointSpriteBatch.setCamera(camera);
		particleSystem.add(pointSpriteBatch);
		return particleSystem;
	}

	/**
	 * Initializes all {@link EntitySystem}s
	 */
	public void initSystems() {
		logger.debug("Creating camera system");
		cameraSystem = new CameraSystem();
		logger.debug("Creating render system");
		renderSystem = new RenderSystem();
		logger.debug("Creating bullet system");
		bulletSystem = new BulletSystem();
		logger.debug("Creating destination system");
		destinationSystem = new DestinationSystem();
		logger.debug("Creating mission system");
		missionSystem = new MissionSystem();
		logger.debug("Creating reactor controller");
		reactorController = new ReactorController();
		logger.debug("Creating emission system");
		emissionSystem = new EmissionSystem();
	}

	/**
	 * Returns the player
	 */
	public GameObject getPlayer() {
		return player;
	}

	/**
	 * Sets the player
	 */
	public void setPlayer(GameObject player) {
		this.player = player;
	}

	/**
	 * Sets the screen using {@link ScreenEnumerator}
	 */
	public void setScreen(ScreenEnumerator screenEnumerator) {
		SpaceFloatScreen screen = screenEnumerator.getScreen();

		logger.debug("Injecting dependencies into " + screen.getClass().getSimpleName());
		injectDependencies(screen);

		String currentScreen = "Null";
		if (this.screen != null) {
			currentScreen = this.screen.getClass().getSimpleName();
		}
		logger.debug("Switching screen: " + currentScreen + " → " + screen.getClass().getSimpleName());
		setScreen(screen);

	}

	/**
	 * Inject Dependencies in a {@link SpaceFloatScreen}
	 */
	private void injectDependencies(SpaceFloatScreen screen) {
		screen.setAssetManager(assetManager);
		screen.setAudio(audio);
		screen.setGameObejctFactory(gameObjectFactory);
		screen.setFont(font);
		screen.setHud(hud);
		screen.setHudFactory(hudFactory);
		screen.setEngine(engine);
		screen.setCamera(camera);
		screen.setParticleSystem(particleSystem);
		screen.setReactorController(reactorController);
		screen.setCameraSystem(cameraSystem);
		screen.setBulletSystem(bulletSystem);
		screen.setRenderSystem(renderSystem);
		screen.setDestinationSystem(destinationSystem);
		screen.setMissionSystem(missionSystem);
		screen.setEmissionSystem(emissionSystem);
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
		logger.debug("Initializing audio");
		audio = Audio.INSTANCE;
		logger.debug("Initializing asset manager");
		assetManager = new AssetManager();
		Texture.setAssetManager(assetManager);
		loadCursor(assetManager, Gdx.graphics);
		font = loadFont(assetManager);
		hud = loadHud(assetManager);
		Sound selectSound = loadSelectSound(assetManager);
		logger.debug("Initializing hud factory");
		hudFactory = createHudFactory(hud, font, audio, selectSound, Gdx.graphics, Gdx.app);
		logger.debug("Initializing engine");
		engine = new Engine();
		camera = createCamera(CameraSystem.CAMERA_TYPE);
		particleSystem = createParticleSystem(camera);
		inputMultiplexer = createInputMultiplexer(Gdx.input);
		initViewportAndStage(inputMultiplexer);
		logger.debug("Initializing bullet");
		Bullet.init();
		Json json = new Json();
		initFactories(json);
		initSystems();
		try {
			setScreen(ScreenEnumerator.MAIN);
		} catch (Exception e) {
			logger.error("Something gone wrong: " + e.getMessage());
			e.printStackTrace();
			Gdx.app.exit();
		}
	}

	@Override
	public void render() {
		try {
			super.render();
		} catch (Exception e) {
			logger.error("Something gone wrong: " + e.getMessage());
			e.printStackTrace();
			Gdx.app.exit();
		}
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void dispose() {
		super.dispose();
		if (hudFactory != null) {
			logger.debug("Disposing hud");
			hudFactory.dispose();
		}
		if (gameObjectFactory != null) {
			logger.debug("Disposing objects");
			gameObjectFactory.dispose();
		}
		if (stage != null) {
			logger.debug("Disposing stage");
			stage.dispose();
		}
		if (missionFactory != null) {
			logger.debug("Disposing mission");
			missionFactory.dispose();
		}
		if (reactorController != null) {
			logger.debug("Disposing reactor");
			reactorController.dispose();
		}
		if (bulletSystem != null) {
			logger.debug("Disposing bullet");
			bulletSystem.dispose();
		}
		if (renderSystem != null) {
			logger.debug("Disposing render");
			renderSystem.dispose();
		}
		if (emissionSystem != null) {
			logger.debug("Disposing emission");
			emissionSystem.dispose();
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

	public GameObjectFactory getGameObjectFactory() {
		return gameObjectFactory;
	}
}
