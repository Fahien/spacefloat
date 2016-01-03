package me.fahien.spacefloat.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.assets.AssetManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import me.fahien.spacefloat.component.GraphicComponent;
import me.fahien.spacefloat.game.GdxTestRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * The {@link LoadingScreen} Test Case
 *
 * @author Fahien
 */
public class LoadingScreenTest {

	private LoadingScreen screen;
	private Engine engine;

	@Before
	public void before() {
		screen = (LoadingScreen) ScreenEnumerator.LOADING.getScreen();
		AssetManager assetManager = new AssetManager();
		screen.setAssetManager(assetManager);
		engine = new Engine();
		screen.setEngine(engine);
		try {
			screen.show();
		} catch (IllegalArgumentException e) {
			GdxTestRunner.logger.error("Cannot show the loading screen: " + e.getMessage());
		}
	}

	@Test
	public void hasAnAssetManager() {
		assertNotNull("The screen has no asset manager", screen.getAssetManager());
	}

	@Test
	public void canLoadObjects() {
		screen.loadObjects(engine);
		assertTrue("The engine has no entities", engine.getEntities().size() > 0);
	}

	@Test
	public void canInjectModels() {
		screen.loadObjects(engine);
		screen.loadModels(engine);
		screen.getAssetManager().finishLoading();
		screen.injectGraphics(engine.getEntities());
		Family family = Family.all(GraphicComponent.class).get();
		ImmutableArray<Entity> entities = engine.getEntitiesFor(family);
		for (Entity entity : entities) {
			GraphicComponent graphic = entity.getComponent(GraphicComponent.class);
			assertNotNull("A graphic component has no instance", graphic.getInstance());
		}
	}

	@After
	public void after() {
		screen.dispose();
	}
}
