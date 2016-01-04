package me.fahien.spacefloat.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static me.fahien.spacefloat.game.GdxTestRunner.logger;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * The {@link SpaceFloatScreen} Test Case
 *
 * @author Fahien
 */
public class SpaceFloatScreenTest {

	private SpaceFloatScreen screen;

	@Before
	public void before() {
		screen = new SpaceFloatScreen();
	}

	@Test
	public void shouldNotBeInitialized() {
		assertFalse("The screen is initialized", screen.isInitialized());
	}

	@Test
	public void canInitializeAScreen() {
		screen.setInitialized(true);
		screen.setAssetManager(new AssetManager());
		screen.setFont(new BitmapFont());
		screen.setHud(new TextureAtlas());
		screen.setEngine(new Engine());
		screen.setViewport(new FitViewport(SpaceFloatScreen.WIDTH, SpaceFloatScreen.HEIGHT));
		try {
			screen.setStage(new Stage(screen.getViewport()));
		} catch (IllegalArgumentException e) {
			logger.error("Could not initialaze stage during tests");
			assertNull("The stage is not null", screen.getStage());
		}
		assertTrue("The screen is not initialized", screen.isInitialized());
		assertNotNull("The asset manager is null", screen.getAssetManager());
		assertNotNull("The font is null", screen.getFont());
		assertNotNull("The hud is null", screen.getHud());
		assertNotNull("The engine is null", screen.getEngine());
		assertNotNull("The viewport is null", screen.getViewport());
	}

	@After
	public void after() {
		screen.dispose();
		assertFalse("The screen is still initialized", screen.isInitialized());
		assertNull("The asset manager is not null", screen.getAssetManager());
		assertNull("The font is not null", screen.getFont());
		assertNull("The hud is not null", screen.getHud());
		assertNull("The engine is not null", screen.getEngine());
		assertNull("The viewport is not null", screen.getViewport());
		assertNull("The stage is not null", screen.getStage());
	}
}
