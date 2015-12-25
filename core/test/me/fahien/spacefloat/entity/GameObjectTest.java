package me.fahien.spacefloat.entity;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * The {@link GameObject} Test Case
 *
 * @author Fahien
 */
public class GameObjectTest {
	private static final String TEST_NAME = "TestName";

	private GameObject object;

	@Before
	public void before() {
		object = new GameObject();
	}

	@Test
	public void canSetTheName() {
		object.setName(TEST_NAME);
		assertEquals("The name in not equal to " + TEST_NAME, TEST_NAME, object.getName());
	}
}
