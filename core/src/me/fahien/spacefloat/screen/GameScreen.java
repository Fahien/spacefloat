package me.fahien.spacefloat.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

import me.fahien.spacefloat.component.EnergyComponent;
import me.fahien.spacefloat.component.MissionComponent;
import me.fahien.spacefloat.component.MoneyComponent;
import me.fahien.spacefloat.component.RigidbodyComponent;
import me.fahien.spacefloat.controller.ReactorController;
import me.fahien.spacefloat.factory.HudFactory;
import me.fahien.spacefloat.factory.MissionFactory;
import me.fahien.spacefloat.system.BulletSystem;
import me.fahien.spacefloat.system.CameraSystem;
import me.fahien.spacefloat.system.DestinationSystem;
import me.fahien.spacefloat.system.MissionSystem;
import me.fahien.spacefloat.system.RenderSystem;

import static me.fahien.spacefloat.component.ComponentMapperEnumerator.energyMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.moneyMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.rigidMapper;
import static me.fahien.spacefloat.game.SpaceFloatGame.logger;

/**
 * The Game {@link SpaceFloatScreen}
 *
 * @author Fahien
 */
public class GameScreen extends SpaceFloatScreen {

	private Engine engine;
	private CameraSystem cameraSystem;
	private RenderSystem renderSystem;
	private BulletSystem bulletSystem;
	private DestinationSystem destinationSystem;
	private MissionSystem missionSystem;
	private ReactorController reactorController;

	private void initSystems() {
		cameraSystem = getCameraSystem();
		cameraSystem.setOffset(CameraSystem.POSITION_OFFSET);
		cameraSystem.setLookAt(CameraSystem.LOOKAT_OFFSET);
		bulletSystem = getBulletSystem();
		renderSystem = getRenderSystem();
		reactorController = getReactorController();
		destinationSystem = getDestinationSystem();
		missionSystem = getMissionSystem();
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
		logger.debug("Adding destination system to the engine");
		engine.addSystem(destinationSystem);
		logger.debug("Adding mission system to the engine");
		engine.addSystem(missionSystem);
	}

	@Override
	public void show() {
		initSystems();
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
		// Get the Player
		Entity player = getGame().getPlayer();
		// Add the Velocity Actor
		RigidbodyComponent rigidbodyComponent = rigidMapper.get(player);
		Vector3 velocity = rigidbodyComponent.getLinearVelocity();
		stage.addActor(factory.getVelocityActor(velocity));
		// Add the Energy Actor
		EnergyComponent energyComponent = energyMapper.get(player);
		stage.addActor(factory.getFuelActor(energyComponent));
		// Add the Money Actor
		MoneyComponent money = moneyMapper.get(player);
		stage.addActor(factory.getMoneyActor(money));
		// Add the Parcel Actor
		MissionComponent mission = MissionFactory.INSTANCE.getMissionComponent();
		stage.addActor(factory.getParcelActor(mission));
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		engine.update(delta);
	}

	@Override
	public void pause() {
		/*
			logger.info("Saving game");
			GameObjectFactory factory = getGameObjectFactory();
			if (factory != null) factory.saveObjects(engine.getEntities());
			MissionFactory.INSTANCE.saveMissions();
		 */
	}

	@Override
	public void hide() {
		super.hide();
		if (engine != null) {
			engine.removeSystem(cameraSystem);
			engine.removeSystem(bulletSystem);
			engine.removeSystem(renderSystem);
			engine.removeSystem(destinationSystem);
			engine.removeSystem(missionSystem);
			engine.removeSystem(reactorController);
			engine.removeAllEntities();
		}
	}
}
