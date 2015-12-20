package me.fahien.spacefloat.factory;

import com.badlogic.gdx.utils.Array;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import me.fahien.spacefloat.game.GdxTestRunner;
import me.fahien.spacefloat.component.GraphicComponent;
import me.fahien.spacefloat.entity.GameObject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * The {@link GameObjectFactory} Test Case
 *
 * @author Fahien
 */
@RunWith(GdxTestRunner.class)
public class GameObjectFactoryTest {
	private static final String SPACESHIP_NAME = "Spaceship";
	private static final String MODEL_NAME = "player";

	private GameObjectFactory factory;

	@Before
	public void before() {
		factory = new GameObjectFactory();
	}

	@Test
	public void couldSaveTheSpaceship() {
		GameObject object = new GameObject();
		object.setName(SPACESHIP_NAME);
		GraphicComponent graphic = new GraphicComponent();
		graphic.setName(MODEL_NAME);
		object.add(graphic);
		factory.save(object);
	}

	@Test
	public void couldLoadTheSpaceship() {
		GameObject object = factory.load(SPACESHIP_NAME);
		assertEquals("The name is not equals to " + SPACESHIP_NAME, SPACESHIP_NAME, object.getName());
		GraphicComponent graphic = object.getComponent(GraphicComponent.class);
		assertNotNull("The spaceship has no graphic component", graphic);
		assertEquals("The graphic name is not equals to " + MODEL_NAME, MODEL_NAME, graphic.getName());
	}

	@Test
	public void couldLoadAllObjects() {
		Array<GameObject> objects = factory.loadObjects();
		assertNotNull("Objects array is null", objects);
		assertTrue("The array does not contain any object", objects.size > 0);
	}
}
