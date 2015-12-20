package me.fahien.spacefloat.entity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import me.fahien.spacefloat.game.GdxTestRunner;

/**
 * The {@link GameObject} Test Case
 *
 * @author Fahien
 */
@RunWith(GdxTestRunner.class)
public class GameObjectTest {
	private static final String TEST_NAME = "TestName";

	private GameObject object;

	@Before
	public void before() {
		object = new GameObject();
	}

	@Test
	public void couldSetTheName() {
		object.setName(TEST_NAME);
		Assert.assertEquals("The name in not equal to " + TEST_NAME, TEST_NAME, object.getName());
	}
}
