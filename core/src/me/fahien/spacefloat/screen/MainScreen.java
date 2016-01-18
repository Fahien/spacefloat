package me.fahien.spacefloat.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import me.fahien.spacefloat.actor.HudActor;
import me.fahien.spacefloat.actor.ScrollingFontActor;
import me.fahien.spacefloat.camera.MainPerspectiveCamera;
import me.fahien.spacefloat.component.GraphicComponent;
import me.fahien.spacefloat.component.PlayerComponent;
import me.fahien.spacefloat.component.TransformComponent;
import me.fahien.spacefloat.entity.GameObject;
import me.fahien.spacefloat.factory.HudFactory;
import me.fahien.spacefloat.game.SpaceFloat;
import me.fahien.spacefloat.system.BulletSystem;
import me.fahien.spacefloat.system.CameraSystem;
import me.fahien.spacefloat.system.RenderSystem;

import static me.fahien.spacefloat.game.SpaceFloatGame.logger;

/**
 * The Main {@link SpaceFloatScreen}
 *
 * @author Fahien
 */
public class MainScreen extends SpaceFloatScreen {
	private static final String CARGO_NAME = "cargo";
	private static final String CARGO_MODEL = "cargo.g3db";
	private static final String MODELS_DIR = "models/";
	private static final Vector3 CAMERA_POSITION_OFFSET = new Vector3(75f, 50f, -200f);
	private static final Vector3 CAMERA_LOOKAT_OFFSET = new Vector3(75f, 0f, 0f);
	private static final String MENU_ATLAS = "system/menu.atlas";
	private static final float BUTTON_X = 32f;
	private static final float BUTTON_Y = 22f;
	private static final float ROTATION_VELOCITY = 128f;

	private Camera mainCamera;

	private Engine engine;

	private CameraSystem cameraSystem;
	private BulletSystem bulletSystem;
	private RenderSystem renderSystem;

	private GameObject cargo;
	private GraphicComponent cargoGraphic;
	private Matrix4 transformGraphic;

	public MainScreen() {
		mainCamera = new MainPerspectiveCamera();
		cargo = new GameObject(CARGO_NAME);
		cargo.add(new TransformComponent());
		cargo.add(new PlayerComponent());
		cargoGraphic = new GraphicComponent(CARGO_MODEL);
		cargo.add(cargoGraphic);
	}

	private void initSystems() {
		cameraSystem = getCameraSystem();
		cameraSystem.setOffset(CAMERA_POSITION_OFFSET);
		cameraSystem.setLookAt(CAMERA_LOOKAT_OFFSET);
		bulletSystem = getBulletSystem();
		renderSystem = getRenderSystem();
	}

	private void injectSystemsDependencies() {
		logger.debug("Injecting camera into camera system");
		cameraSystem.setCamera(mainCamera);
		logger.debug("Injecting camera into render system");
		renderSystem.setCamera(mainCamera);
	}

	private void addSystemsToEngine(final Engine engine) {
		logger.debug("Adding render system to the engine");
		engine.addSystem(renderSystem);
		logger.debug("Adding camera system to the engine");
		engine.addSystem(cameraSystem);
		logger.debug("Adding bullet system to the engine");
		engine.addSystem(bulletSystem);
	}

	@Override
	public void show() {
		initSystems();
		injectSystemsDependencies();
		loadTheCargoModel(getAssetManager());
		engine = getEngine();
		engine.addEntity(cargo);
		addSystemsToEngine(engine);
		super.show();
	}

	private void loadTheCargoModel(final AssetManager assetManager) {
		if (cargoGraphic.getInstance() == null) {
			if (!assetManager.isLoaded(MODELS_DIR + CARGO_MODEL)) {
				assetManager.load(MODELS_DIR + CARGO_MODEL, Model.class);
				assetManager.finishLoading();
			}
			Model cargoModel = assetManager.get(MODELS_DIR + CARGO_MODEL, Model.class);
			ModelInstance cargoInstance = new ModelInstance(cargoModel);
			cargoGraphic.setInstance(cargoInstance);
			transformGraphic = cargoInstance.transform;
		}
	}

	@Override
	public void populate(final Stage stage) {
		// Load the menu atlas
		AssetManager assetManager = getAssetManager();
		assetManager.load(MENU_ATLAS, TextureAtlas.class);
		assetManager.finishLoading();
		TextureAtlas menu = assetManager.get(MENU_ATLAS);

		// Get Hud Factory
		HudFactory hudfactory = getHudFactory();
		// Set the menu atlas
		hudfactory.setMenu(menu);

		// Create text background
		HudActor textBackgroundActor = hudfactory.getTextBackgroundMenu();
		textBackgroundActor.setPosition(32f, 14f);
		stage.addActor(textBackgroundActor);

		// Create scrolling credits
		ScrollingFontActor creditsActor = hudfactory.getCreditsActor();
		creditsActor.setPosition(35f, 19f);
		creditsActor.setSize(156f, 111f);
		stage.addActor(creditsActor);

		// Create background
		HudActor backgroundActor = hudfactory.getBackgroundMenu();
		stage.addActor(backgroundActor);

		int i = 2;

		// Continue button
		if (getGameObjectFactory().hasObjects()) {
			HudActor continueActor = hudfactory.getContinueActor();
			continueActor.setX(BUTTON_X);
			continueActor.setY(HEIGHT - BUTTON_Y * i++);
			continueActor.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					((LoadingScreen) ScreenEnumerator.LOADING.getScreen()).setForceReload(false);
					SpaceFloat.GAME.setScreen(ScreenEnumerator.LOADING);
				}
			});
			stage.addActor(continueActor);
		}

		// Create new button
		HudActor newGameActor = hudfactory.getNewGameActor();
		newGameActor.setX(BUTTON_X);
		newGameActor.setY(HEIGHT - BUTTON_Y * i++);
		newGameActor.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((LoadingScreen) ScreenEnumerator.LOADING.getScreen()).setForceReload(true);
				SpaceFloat.GAME.setScreen(ScreenEnumerator.LOADING);
			}
		});
		stage.addActor(newGameActor);

		if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
			HudActor videoActor = hudfactory.getVideoActor();
			videoActor.setX(BUTTON_X);
			videoActor.setY(HEIGHT - BUTTON_Y * i++);
			stage.addActor(videoActor);
		}
		HudActor audioActor = hudfactory.getAudioActor();
		audioActor.setX(BUTTON_X);
		audioActor.setY(HEIGHT - BUTTON_Y * i++);
		stage.addActor(audioActor);

		HudActor exitActor = hudfactory.getExitActor();
		exitActor.setX(BUTTON_X);
		exitActor.setY(HEIGHT - BUTTON_Y * i);
		stage.addActor(exitActor);
	}

	private Quaternion quaternion = new Quaternion();
	private float rotation = 0;

	@Override
	public void update(float delta) {
		super.update(delta);

		rotation += delta * ROTATION_VELOCITY;
		quaternion.setEulerAnglesRad(-rotation, 0, 0);
		transformGraphic.set(quaternion);

		if (engine != null) {
			engine.update(delta);
		} else {
			logger.error("Engine is null");
			Gdx.app.exit();
		}
	}

	@Override
	public void hide() {
		super.hide();
		if (engine != null) {
			engine.removeSystem(cameraSystem);
			engine.removeSystem(bulletSystem);
			engine.removeSystem(renderSystem);
			engine.removeEntity(cargo);
		}
	}
}
