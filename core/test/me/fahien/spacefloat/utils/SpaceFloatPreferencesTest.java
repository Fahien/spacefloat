package me.fahien.spacefloat.utils;

import com.badlogic.gdx.Preferences;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import me.fahien.spacefloat.component.EnergyComponent;
import me.fahien.spacefloat.game.GdxTestRunner;
import me.fahien.spacefloat.system.CameraSystem;

/**
 * The {@link SpaceFloatPreferences} Test Case
 *
 * @author Fahien
 */
@RunWith(GdxTestRunner.class)
public class SpaceFloatPreferencesTest {

	private SpaceFloatPreferences preferences;

	@Before
	public void before() {
		preferences = new SpaceFloatPreferences();
	}

	@Test
	public void couldGetPreferences() {
		Preferences pref = preferences.getPreferences();
		Assert.assertNotNull("Preferences are null", pref);
	}

	@Test
	public void couldCreatePreferences() {
		Preferences prefs = preferences.getPreferences();
		prefs.putFloat(SpaceFloatPreferences.CAMERA_ZOOM, CameraSystem.CAMERA_ZOOM);
		prefs.putFloat(SpaceFloatPreferences.SHIELD_CONSUME, EnergyComponent.SHIELD_CONSUME);
	}
}
