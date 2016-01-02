package me.fahien.spacefloat.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

import me.fahien.spacefloat.actor.HudFactory;
import me.fahien.spacefloat.camera.MainCamera;
import me.fahien.spacefloat.component.AccelerationComponent;
import me.fahien.spacefloat.component.EnergyComponent;
import me.fahien.spacefloat.component.TransformComponent;
import me.fahien.spacefloat.component.VelocityComponent;
import me.fahien.spacefloat.controller.SpaceshipController;
import me.fahien.spacefloat.controller.SpaceshipController2D;
import me.fahien.spacefloat.game.SpaceFloatGame;
import me.fahien.spacefloat.system.CameraSystem;
import me.fahien.spacefloat.system.CollisionSystem;
import me.fahien.spacefloat.system.GravitySystem;
import me.fahien.spacefloat.system.PhysicSystem;
import me.fahien.spacefloat.system.RenderSystem;

/**
 * The Main {@link SpaceFloatScreen}
 *
 * @author Fahien
 */
public class MainScreen extends StagedScreen {

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
		MainCamera mainCamera = getCamera();
		cameraSystem.setCamera(mainCamera);
		renderSystem.setCamera(mainCamera);
		renderSystem.setParticleSystem(getParticleSystem());

		engine = getEngine();
		engine.addSystem(spaceshipController);
		engine.addSystem(collisionSystem);
		engine.addSystem(gravitySystem);
		engine.addSystem(physicSystem);
		engine.addSystem(cameraSystem);
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
			SpaceFloatGame.logger.error("Error creating the HUD: player is null");
		}
	}

	@Override
	public void prerender(float delta) {
		engine.update(delta);
	}

	@Override
	public void hide() {
		super.hide();
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
