package me.fahien.spacefloat.screen;

import com.badlogic.ashley.core.Engine;

import me.fahien.spacefloat.camera.MainCamera;
import me.fahien.spacefloat.system.CameraSystem;
import me.fahien.spacefloat.system.PhysicSystem;
import me.fahien.spacefloat.system.RenderingSystem;

/**
 * The Main {@link SpaceFloatScreen}
 *
 * @author Fahien
 */
public class MainScreen extends SpaceFloatScreen {

	private Engine engine;
	private CameraSystem camera;
	private RenderingSystem rendering;
	private PhysicSystem physic;

	public MainScreen() {
		MainCamera mainCamera = new MainCamera();
		camera = new CameraSystem(mainCamera);
		rendering = new RenderingSystem(mainCamera);
		physic = new PhysicSystem();
	}

	@Override
	public void show() {
		super.show();
		engine = getEngine();
		engine.addSystem(camera);
		engine.addSystem(rendering);
		engine.addSystem(physic);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		engine.update(delta);
	}

	@Override
	public void hide() {
		super.hide();
		engine.removeSystem(rendering);
		engine.removeSystem(physic);
		engine.removeSystem(camera);
	}

	@Override
	public void dispose() {
		super.dispose();
		if (rendering != null) rendering.dispose();
	}
}
