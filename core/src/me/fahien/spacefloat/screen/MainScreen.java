package me.fahien.spacefloat.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

import me.fahien.spacefloat.actor.HudFactory;
import me.fahien.spacefloat.component.EnergyComponent;
import me.fahien.spacefloat.component.RigidbodyComponent;
import me.fahien.spacefloat.controller.ReactorController;
import me.fahien.spacefloat.system.BulletSystem;
import me.fahien.spacefloat.system.CameraSystem;
import me.fahien.spacefloat.system.RenderSystem;

import static me.fahien.spacefloat.component.ComponentMapperEnumerator.energyMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.rigidMapper;
import static me.fahien.spacefloat.game.SpaceFloatGame.logger;

/**
 * The Main {@link SpaceFloatScreen}
 *
 * @author Fahien
 */
public class MainScreen extends SpaceFloatScreen {

	private Engine engine;
	private CameraSystem cameraSystem;
	private RenderSystem renderSystem;
	private BulletSystem bulletSystem;
	private ReactorController reactorController;

	public MainScreen() {
		cameraSystem = new CameraSystem();
		renderSystem = new RenderSystem();
		bulletSystem = new BulletSystem();
		reactorController = new ReactorController();
	}

	private void injectSystemsDependencies() {
		Camera mainCamera = getCamera();
		logger.debug("Injecting camera into camera system");
		cameraSystem.setCamera(mainCamera);
		logger.debug("Injecting camera into render system");
		renderSystem.setCamera(mainCamera);
		logger.debug("Injecting input multiplexer into reactor controller");
		reactorController.setInputMultiplexer(getInputMultiplexer());
		logger.debug("Injecting particle system into reactor controller");
		reactorController.setParticleSystem(getParticleSystem());
		logger.debug("Injecting particle system into render system");
		renderSystem.setParticleSystem(getParticleSystem());
	}

	private void addSystemsToEngine(Engine engine) {
		/** The RenderSystem must be added before the BulletSystem */
		logger.debug("Adding render system to the engine");
		engine.addSystem(renderSystem);
		logger.debug("Adding bullet system to the engine");
		engine.addSystem(bulletSystem);
		logger.debug("Adding reactor controller to the engine");
		engine.addSystem(reactorController);
		logger.debug("Adding camera system to the engine");
		engine.addSystem(cameraSystem);
	}

	@Override
	public void show() {
		injectSystemsDependencies();
		engine = getEngine();
		addSystemsToEngine(engine);
		super.show();
	}



	@Override
	public void populate(Stage stage) {
		// Create the Hud factory
		HudFactory factory = getHudFactory();
		// Add the Fps Actor to the Stage
		stage.addActor(factory.getFpsActor());
		// Get the player from a PlayerController
		Entity player = cameraSystem.getPlayer();
		if (player != null) {
			RigidbodyComponent rigidbodyComponent = rigidMapper.get(player);
			Vector3 velocity = rigidbodyComponent.getLinearVelocity();
			stage.addActor(factory.getVelocityActor(velocity));

			EnergyComponent energyComponent = energyMapper.get(player);
			stage.addActor(factory.getFuelActor(energyComponent));
		} else {
			logger.error("Error creating the HUD: player is null");
		}
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		engine.update(delta);
	}

	@Override
	public void hide() {
		super.hide();
		if (engine == null) return;
		engine.removeSystem(cameraSystem);
		engine.removeSystem(bulletSystem);
		engine.removeSystem(reactorController);
		engine.removeSystem(renderSystem);
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}
