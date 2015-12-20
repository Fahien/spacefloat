package me.fahien.spacefloat.screen;

import org.junit.Test;
import org.junit.runner.RunWith;

import me.fahien.spacefloat.game.GdxTestRunner;

import static org.junit.Assert.assertNotNull;

/**
 * The {@link ScreenEnumerator} Test Case
 *
 * @author Fahien
 */
@RunWith(GdxTestRunner.class)
public class ScreenEnumeratorTest {

	@Test
	public void couldGetTheShowcaseScreen() {
		assertNotNull("The showcase screen is null", ScreenEnumerator.SHOWCASE.getScreen());
	}

	@Test
	public void couldGetTheInfoScreen() {
		assertNotNull("The info screen is null", ScreenEnumerator.INFO.getScreen());
	}

	@Test
	public void couldGetTheMainScreen() {
		assertNotNull("The main screen is null", ScreenEnumerator.MAIN.getScreen());
	}

	@Test
	public void couldGetTheLoadingScreen() {
		assertNotNull("The loading screen is null", ScreenEnumerator.LOADING.getScreen());
	}
}
