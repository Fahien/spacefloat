package me.fahien.spacefloat.screen;

import com.badlogic.ashley.core.Engine;

import me.fahien.spacefloat.system.RenderingSystem;

/**
 * The Main {@link SpaceFloatScreen}
 *
 * @author Fahien
 */
public class MainScreen extends SpaceFloatScreen {

	private Engine engine;
	private RenderingSystem rendering;

	public MainScreen() {
		rendering = new RenderingSystem();
	}

	@Override
	public void show() {
		super.show();
		engine = getEngine();
		engine.addSystem(rendering);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		engine.update(delta);
	}

	@Override
	public void dispose() {
		super.dispose();
		if (rendering != null) rendering.dispose();
	}
}
