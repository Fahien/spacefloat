package me.fahien.spacefloat.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.utils.GdxRuntimeException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import me.fahien.spacefloat.screen.SpaceFloatScreen;
import me.fahien.spacefloat.screen.ScreenEnumerator;

import static me.fahien.spacefloat.game.GdxTestRunner.logger;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * The {@link SpaceFloatGame} Test Case
 *
 * @author Fahien
 */
@RunWith(GdxTestRunner.class)
public class SpaceFloatGameTest {
	private static final String TEST_DIR = "test/";
	private static final String TEST_ASSET = TEST_DIR + "spaceship.png";
	private static final String MODELS_DIR = "models/";
	private static final String A_MODEL = MODELS_DIR + "player.g3db";

	private SpaceFloatGame game;

	@Before
	public void before() {
		game = new SpaceFloatGame();
	}



	@Test
	public void canGetTheAssetManager() {
		assertNotNull("The game has no asset manager", game.getAssetManager());
	}

	@Test
	public void canGetTheAshleyEngine() {
		assertNotNull("The game has no Ashley engine", game.getEngine());
	}

	@Test
	public void canLoadAnAsset() {
		AssetManager assetManager = game.getAssetManager();
		assetManager.load(TEST_ASSET, Texture.class);
		assetManager.finishLoading();
		assertNotNull("Could not get canLoadObjects asset: " + TEST_ASSET, assetManager.get(TEST_ASSET));
	}

	@Test
	public void canLoadAnAssetDirectory() {
		AssetManager assetManager = game.getAssetManager();
		FileHandle[] files = Gdx.files.local(TEST_DIR).list();
		for(FileHandle file : files) {
			assetManager.load(file.path(), Texture.class);
		}
		assetManager.finishLoading();
		for(FileHandle file : files) {
			assertNotNull("Could not get an asset: " + file.path(), assetManager.get(file.path(), Texture.class));
		}
	}

	@Test
	public void canInjectDependenciesInScreens() {
		game.loadFont();
		for (ScreenEnumerator screenEnum : ScreenEnumerator.values()) {
			try {
				game.setScreen(screenEnum);
			} catch (GdxRuntimeException|IllegalArgumentException e) {
				logger.error("Could not initialize the Model Batch during tests: " + e.getMessage());
			}
			SpaceFloatScreen screen = screenEnum.getScreen();
			assertTrue("The screen is not initialized", screen.isInitialized());
			assertNotNull("The screen has no asset manager", screen.getAssetManager());
			assertNotNull("The screen has no font", screen.getFont());
			assertNotNull("The screen has no engine", screen.getEngine());
			assertEquals("The screens are not equals", screen, game.getScreen());
		}
	}

	@Test
	public void shouldDisposeProperlyAScreenOnChangingIt() {
		SpaceFloatScreen mainScreen = ScreenEnumerator.SHOWCASE.getScreen();
		try {
			game.setScreen(ScreenEnumerator.SHOWCASE);
		} catch (GdxRuntimeException|IllegalArgumentException e) {
			logger.error("Could not initialize the Model Batch during tests: " + e.getMessage());
		}
		assertTrue("The screen is not initialized", mainScreen.isInitialized());
		try {
			game.setScreen(ScreenEnumerator.INFO);
		} catch (GdxRuntimeException|IllegalArgumentException e) {
			logger.error("Could not initialize the Model Batch during tests: " + e.getMessage());
		}
		assertFalse("The screen is not disposed properly", mainScreen.isInitialized());
	}

	@Test
	public void shouldShowTheMainScreenAfterCreate() {
		try {
			game.create();
		} catch (GdxRuntimeException|IllegalArgumentException e) {
			logger.error("Could not create the game during tests: " + e.getMessage());
		}
		assertEquals("The game is not showing the main screen",
				ScreenEnumerator.LOADING.getScreen(),
				game.getScreen());
	}

	@Test
	public void canLoadAModel() {
		AssetManager assetManager = game.getAssetManager();
		assetManager.load(A_MODEL, Model.class);
		assetManager.finishLoading();
		assertNotNull("Could not get car model: " + A_MODEL, assetManager.get(A_MODEL));
	}

	@Test
	public void canLoadAndGetTheFont() {
		game.loadFont();
		assertNotNull("Could not get the font", game.getFont());
	}

	@After
	public void after() {
		game.dispose();
	}
}
