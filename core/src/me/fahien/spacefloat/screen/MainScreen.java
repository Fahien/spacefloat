package me.fahien.spacefloat.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

import me.fahien.spacefloat.actor.HudFactory;
import me.fahien.spacefloat.camera.MainCamera;
import me.fahien.spacefloat.component.AccelerationComponent;
import me.fahien.spacefloat.component.PlayerComponent;
import me.fahien.spacefloat.component.TransformComponent;
import me.fahien.spacefloat.component.VelocityComponent;
import me.fahien.spacefloat.controller.SpaceshipController;
import me.fahien.spacefloat.controller.SpaceshipController2D;
import me.fahien.spacefloat.game.SpaceFloatGame;
import me.fahien.spacefloat.system.CameraSystem;
import me.fahien.spacefloat.system.CollisionSystem;
import me.fahien.spacefloat.system.GravitySystem;
import me.fahien.spacefloat.system.PhysicSystem;
import me.fahien.spacefloat.system.RenderingSystem;

/**
 * The Main {@link SpaceFloatScreen}
 *
 * @author Fahien
 */
public class MainScreen extends StagedScreen {

	private Engine engine;
	private CameraSystem camera;
	private RenderingSystem rendering;
	private GravitySystem gravity;
	private PhysicSystem physic;
	private CollisionSystem collision;
	private SpaceshipController controller;

	public MainScreen() {
		MainCamera mainCamera = new MainCamera();
		camera = new CameraSystem(mainCamera);
		rendering = new RenderingSystem(mainCamera);
		gravity = new GravitySystem();
		physic = new PhysicSystem();
		collision = new CollisionSystem();
		controller = new SpaceshipController2D();
	}

	@Override
	public void show() {
		engine = getEngine();
		engine.addSystem(controller);
		engine.addSystem(collision);
		engine.addSystem(gravity);
		engine.addSystem(physic);
		engine.addSystem(camera);
		engine.addSystem(rendering);
		super.show();
	}

	@Override
	public void populate(Stage stage) {
		HudFactory factory = new HudFactory();
		BitmapFont font = getFont();
		stage.addActor(factory.getFpsActor(font));
		Entity player = camera.getPlayer();
		if (player != null) {
			AccelerationComponent accelerationComponent = player.getComponent(AccelerationComponent.class);
			VelocityComponent velocityComponent = player.getComponent(VelocityComponent.class);
			Vector3 velocity = velocityComponent.getVelocity();
			stage.addActor(factory.getVelocityActor(font, velocity));
			Vector3 acceleration = accelerationComponent.getAcceleration();
			stage.addActor(factory.getAccelerationActor(font, acceleration));
			Vector3 position = player.getComponent(TransformComponent.class).getPosition();
			stage.addActor(factory.getPositionActor(font, position));
			PlayerComponent playerComponent = player.getComponent(PlayerComponent.class);
			stage.addActor(factory.getFuelActor(getHud(), playerComponent));
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
		engine.removeSystem(camera);
		engine.removeSystem(rendering);
		engine.removeSystem(physic);
		engine.removeSystem(gravity);
		engine.removeSystem(collision);
		engine.removeSystem(controller);
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}
