package me.fahien.spacefloat.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

import me.fahien.spacefloat.actor.HudFactory;
import me.fahien.spacefloat.component.AccelerationComponent;
import me.fahien.spacefloat.component.EnergyComponent;
import me.fahien.spacefloat.component.TransformComponent;
import me.fahien.spacefloat.component.VelocityComponent;
import me.fahien.spacefloat.controller.SpaceshipController;
import me.fahien.spacefloat.controller.SpaceshipController2D;
import me.fahien.spacefloat.system.CameraSystem;
import me.fahien.spacefloat.system.CollisionSystem;
import me.fahien.spacefloat.system.GravitySystem;
import me.fahien.spacefloat.system.PhysicSystem;
import me.fahien.spacefloat.system.RenderSystem;

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
	private GravitySystem gravitySystem;
	private PhysicSystem physicSystem;
	private CollisionSystem collisionSystem;
	private SpaceshipController spaceshipController;

	public MainScreen() {
		cameraSystem = new CameraSystem();
		renderSystem = new RenderSystem();
		gravitySystem = new GravitySystem();
		physicSystem = new PhysicSystem();
		collisionSystem = new CollisionSystem();
		spaceshipController = new SpaceshipController2D();
	}

	@Override
	public void show() {
		logger.debug("Getting the main camera");
		Camera mainCamera = getCamera();
		logger.debug("Injecting camera into camera system");
		cameraSystem.setCamera(mainCamera);
		logger.debug("Injecting camera into render system");
		renderSystem.setCamera(mainCamera);
		logger.debug("Injecting particle system into render system");
		renderSystem.setParticleSystem(getParticleSystem());
		logger.debug("Getting the engine");
		engine = getEngine();
		logger.debug("Adding spaceship controller to the engine");
		engine.addSystem(spaceshipController);
		logger.debug("Adding collision system to the engine");
		engine.addSystem(collisionSystem);
		logger.debug("Adding gravity system to the engine");
		engine.addSystem(gravitySystem);
		logger.debug("Adding physic system to the engine");
		engine.addSystem(physicSystem);
		logger.debug("Adding camera system to the engine");
		engine.addSystem(cameraSystem);
		logger.debug("Adding render system to the engine");
		engine.addSystem(renderSystem);
		super.show();
	}

	@Override
	public void populate(Stage stage) {
		HudFactory factory = new HudFactory();
		BitmapFont font = getFont();
		stage.addActor(factory.getFpsActor(font));
		Entity player = cameraSystem.getPlayer();
		if (player != null) {
			AccelerationComponent accelerationComponent = player.getComponent(AccelerationComponent.class);
			VelocityComponent velocityComponent = player.getComponent(VelocityComponent.class);
			Vector3 velocity = velocityComponent.getVelocity();
			stage.addActor(factory.getVelocityActor(font, velocity));
			Vector3 acceleration = accelerationComponent.getAcceleration();
			stage.addActor(factory.getAccelerationActor(font, acceleration));
			Vector3 position = player.getComponent(TransformComponent.class).getPosition();
			stage.addActor(factory.getPositionActor(font, position));
			EnergyComponent energyComponent = player.getComponent(EnergyComponent.class);
			stage.addActor(factory.getFuelActor(getHud(), energyComponent));
		} else {
			logger.error("Error creating the HUD: player is null");
		}
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		logger.debug("Updating engine");
		engine.update(delta);
	}

	@Override
	public void hide() {
		super.hide();
		if (engine == null) return;
		engine.removeSystem(cameraSystem);
		engine.removeSystem(renderSystem);
		engine.removeSystem(physicSystem);
		engine.removeSystem(gravitySystem);
		engine.removeSystem(collisionSystem);
		engine.removeSystem(spaceshipController);
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}
