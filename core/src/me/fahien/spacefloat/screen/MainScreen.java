package me.fahien.spacefloat.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

import me.fahien.spacefloat.actor.ActorFactory;
import me.fahien.spacefloat.camera.MainCamera;
import me.fahien.spacefloat.component.TransformComponent;
import me.fahien.spacefloat.component.VelocityComponent;
import me.fahien.spacefloat.controller.SpaceshipController;
import me.fahien.spacefloat.system.CameraSystem;
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
	private PhysicSystem physic;
	private SpaceshipController controller;

	public MainScreen() {
		MainCamera mainCamera = new MainCamera();
		camera = new CameraSystem(mainCamera);
		rendering = new RenderingSystem(mainCamera);
		physic = new PhysicSystem();
		controller = new SpaceshipController();
	}

	@Override
	public void show() {
		engine = getEngine();
		engine.addSystem(camera);
		engine.addSystem(rendering);
		engine.addSystem(physic);
		engine.addSystem(controller);
		super.show();
	}

	@Override
	public void populate(Stage stage) {
		ActorFactory factory = new ActorFactory();
		BitmapFont font = getFont();
		stage.addActor(factory.getFpsActor(font));
		Entity player = camera.getPlayer();
		Vector3 velocity = player.getComponent(VelocityComponent.class).getVelocity();
		stage.addActor(factory.getVelocityActor(font, velocity));
		Vector3 position = player.getComponent(TransformComponent.class).getPosition();
		stage.addActor(factory.getPositionActor(font, position));
	}

	@Override
	public void prerender(float delta) {
		engine.update(delta);
	}

	@Override
	public void hide() {
		super.hide();
		engine.removeSystem(rendering);
		engine.removeSystem(physic);
		engine.removeSystem(camera);
		engine.removeSystem(controller);
	}

	@Override
	public void dispose() {
		super.dispose();
		if (rendering != null) rendering.dispose();
	}
}
