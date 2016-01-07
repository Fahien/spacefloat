package me.fahien.spacefloat.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * The {@link GraphicComponent} Test Case
 *
 * @author Fahien
 */
public class GraphicComponentTest {
	private static final String TEST_MODEL = "cargo.g3db";

	private GraphicComponent graphic;
	private Model model;

	@Before
	public void before() {
		graphic = new GraphicComponent(TEST_MODEL);
	}

	@Test
	public void couldSetModelInstance() {
		AssetManager assetManager = new AssetManager();
		assetManager.load("models/" + TEST_MODEL, Model.class);
		assetManager.finishLoading();
		model = assetManager.get("models/" + TEST_MODEL);
		ModelInstance instance = new ModelInstance(model);
		graphic.setInstance(instance);
	}

	@Test
	public void couldMoveInstance() {
		couldSetModelInstance();
		Vector3 position = new Vector3();
		graphic.getPosition(position);
		assertEquals("The instance is not at ZERO", Vector3.Zero, position);
		Vector3 positionOne = new Vector3(1, 1, 1);
		graphic.setPosition(positionOne);
		graphic.getPosition(position);
		assertEquals("The instance is not at ONE", positionOne, position);
	}

	@After
	public void after() {
		if (model != null) model.dispose();
	}
}
