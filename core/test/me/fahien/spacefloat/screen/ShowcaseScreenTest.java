package me.fahien.spacefloat.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import me.fahien.spacefloat.game.GdxTestRunner;

import static me.fahien.spacefloat.game.GdxTestRunner.logger;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * The {@link ShowcaseScreen} Test Case
 *
 * @author Fahien
 */
@RunWith(GdxTestRunner.class)
public class ShowcaseScreenTest {
	protected static final String MODEL = "player";

	private ShowcaseScreen screen = (ShowcaseScreen) ScreenEnumerator.SHOWCASE.getScreen();

	@Test
	public void shouldHaveACamera() {
		assertNotNull("The showcase screen has no camera", screen.getCamera());
	}

	@Test
	public void couldInitializeTheStage() {
		try {
			screen.initStage();
			assertNotNull("The showcase screen has no stage", screen.getStage());
		} catch (IllegalArgumentException e) {
			logger.error("Could not initialize the stage during tests: " + e.getMessage());
		}
	}

	@Test
	public void couldLoadResources() {
		AssetManager assetManager = new AssetManager();
		screen.setAssetManager(assetManager);
		screen.loadModel(MODEL);
		while (assetManager.getProgress() < 1.0f) {
			assetManager.update();
		}
		assertNotNull("The player model is null", assetManager.get(ShowcaseScreen.MODELS_DIR + MODEL + ShowcaseScreen.G3DB_EXT));
	}

	@Test
	public void couldInitializeInstanceAfterLoadResources() {
		couldLoadResources();
		screen.updateInstance(MODEL);
		assertNotNull("The instance is null", screen.getInstance());
	}

	@Test
	public void couldInitializeTheBatch() {
		try {
			screen.initBatch();
			assertNotNull(screen.getBatch());
		} catch (GdxRuntimeException e){
			logger.error("Could not initialize the Model Batch during tests: " + e.getMessage());
		}
	}

	@Test
	public void couldInitializeTheEnvironment() {
		screen.initEnvironment();
		assertNotNull("The environment is null", screen.getEnvironment());
	}

	private void createTheModelList() {
		String modelList = "";
		FileHandle[] files = Gdx.files.local(ShowcaseScreen.MODELS_DIR).list();
		for (FileHandle file : files) {
			if (file.path().endsWith(ShowcaseScreen.G3DB_EXT)) {
				modelList += file.nameWithoutExtension() + "\n";
			}
		}
		FileHandle fileList = Gdx.files.local(ShowcaseScreen.MODEL_LIST);
		fileList.writeString(modelList, false);
	}

	@Test
	public void couldLoadTheListOfModels() {
		createTheModelList();
		screen.loadInternalModels();
		Array<String> list = screen.getModelList();
		Assert.assertNotNull("The model list is null", list);
		Assert.assertNotEquals("The model list is empty", list.size);
	}

	@Test
	public void couldLoadNextModel() {
		AssetManager assetManager = new AssetManager();
		screen.setAssetManager(assetManager);
		screen.loadNextModel();
		assertNull("The instance is not null", screen.getInstance());
		screen.loadInternalModels();
		screen.loadNextModel();
		assetManager.finishLoading();
		screen.updateInstanceWithCurrentModel();
		assertNotNull("The instance is null", screen.getInstance());
	}

	@Test
	public void couldLoadPreviousModel() {
		AssetManager assetManager = new AssetManager();
		screen.setAssetManager(assetManager);
		screen.loadPreviousModel();
		assertNull("The instance is not null", screen.getInstance());
		screen.loadInternalModels();
		screen.loadPreviousModel();
		assetManager.finishLoading();
		screen.updateInstanceWithCurrentModel();
		assertNotNull("The instance is null", screen.getInstance());
	}

	@Test
	public void couldLoadLocalModels() {
		AssetManager assetManager = new AssetManager();
		screen.setAssetManager(assetManager);
		screen.loadLocalModels();
		assertNotNull(screen.getModelList());
	}

	@After
	public void after() {
		screen.dispose();
	}
}
