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
	private static final String LONGER_TEXT = "Longer";
	private static final String SHORT_TEXT = "Sh";

	private FontActor actor;
	private BitmapFont font;

	@Before
	public void before() {
		font = new BitmapFont();
		actor = new FontActor(font, TEST_TEXT);
	}

	@Test
	public void canSetLongerText() {
		actor.setText(LONGER_TEXT);
		assertEquals("Could not set longer text", LONGER_TEXT, actor.getText().toString());
	}

	@Test
	public void canSetShorterText() {
		actor.setText(SHORT_TEXT);
		assertEquals("Could not set shorter text", SHORT_TEXT, actor.getText().toString());
	}

	@After
	public void after() {
		font.dispose();
	}
}
