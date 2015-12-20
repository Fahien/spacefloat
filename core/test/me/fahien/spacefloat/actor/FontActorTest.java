package me.fahien.spacefloat.actor;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * The {@link FontActor} Test Case
 *
 * @author Fahien
 */
public class FontActorTest {
	private static final String TEST_TEXT = "Test";
	private static final String ANOTHER_TEXT = "Another";
	private static final String SHORT_TEXT = "s";

	private FontActor actor;
	private BitmapFont font;

	@Before
	public void before() {
		font = new BitmapFont();
		actor = new FontActor(font, TEST_TEXT);
	}

	@Test
	public void couldSetLongerText() {
		actor.setText(ANOTHER_TEXT);
		assertEquals("Could not set longer text", ANOTHER_TEXT, actor.getText().toString());
	}

	@Test
	public void couldSetShorterText() {
		actor.setText(SHORT_TEXT);
		assertEquals("Could not set longer text", SHORT_TEXT, actor.getText().toString());
	}

	@After
	public void after() {
		font.dispose();
	}
}
