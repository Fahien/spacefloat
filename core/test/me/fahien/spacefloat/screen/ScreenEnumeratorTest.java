package me.fahien.spacefloat.screen;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * The {@link ScreenEnumerator} Test Case
 *
 * @author Fahien
 */
public class ScreenEnumeratorTest {

	@Test
	public void canGetTheShowcaseScreen() {
		assertNotNull("The showcase screen is null", ScreenEnumerator.SHOWCASE.getScreen());
	}

	@Test
	public void canGetTheInfoScreen() {
		assertNotNull("The info screen is null", ScreenEnumerator.INFO.getScreen());
	}

	@Test
	public void canGetTheMainScreen() {
		assertNotNull("The main screen is null", ScreenEnumerator.GAME.getScreen());
	}

	@Test
	public void canGetTheLoadingScreen() {
		assertNotNull("The loading screen is null", ScreenEnumerator.LOADING.getScreen());
	}
}
