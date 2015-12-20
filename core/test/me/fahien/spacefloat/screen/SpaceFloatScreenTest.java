package me.fahien.spacefloat.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import me.fahien.spacefloat.game.GdxTestRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * The {@link SpaceFloatScreen} Test Case
 *
 * @author Fahien
 */
@RunWith(GdxTestRunner.class)
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
	public void couldInitializeAScreen() {
		screen.setInitialized(true);
		screen.setAssetManager(new AssetManager());
		screen.setFont(new BitmapFont());
		screen.setEngine(new Engine());
		assertTrue("The screen is not initialized", screen.isInitialized());
		assertNotNull("The asset manager is null", screen.getAssetManager());
		assertNotNull("The font is null", screen.getFont());
		assertNotNull("The engine is null", screen.getEngine());
	}

	@After
	public void after() {
		screen.dispose();
		assertFalse("The screen is still initialized", screen.isInitialized());
		assertNull("The asset manager is not null", screen.getAssetManager());
		assertNull("The font is not null", screen.getFont());
		assertNull("The engine is not null", screen.getEngine());
	}
}
