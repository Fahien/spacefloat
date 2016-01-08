package me.fahien.spacefloat.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import me.fahien.spacefloat.component.EnergyComponent;
import me.fahien.spacefloat.controller.CameraController;
import me.fahien.spacefloat.game.SpaceFloatGame;
import me.fahien.spacefloat.system.CollisionSystem;

/**
 * Space Float Preferences
 *
 * @author Fahien
 */
public class SpaceFloatPreferences {
	private static final String CONFIG_FILE = "spacefloat";

	protected static final String LOGGER_LEVEL = "loggerLevel";
	protected static final String ACCELERATION = "acceleration";
	protected static final String RECHARGE_POWER = "rechargePower";
	protected static final String REACTOR_CONSUME = "reactorConsume";
	protected static final String SHIELD_CONSUME = "shieldConsume";
	protected static final String CAMERA_TYPE = "cameraType";
	protected static final String CAMERA_ZOOM = "cameraZoom";

	private Preferences preferences;

	public SpaceFloatPreferences() {
		preferences = Gdx.app.getPreferences(CONFIG_FILE);
	}

	/**
	 * Returns the {@link Preferences}
	 */
	public Preferences getPreferences() {
		return preferences;
	}

	/**
	 * Saves the {@link Preferences}
	 */
	public void save() {
		preferences.putInteger(LOGGER_LEVEL, SpaceFloatGame.LOGGER_LEVEL);
		preferences.putString(CAMERA_TYPE, CameraController.CAMERA_TYPE);
		preferences.putFloat(CAMERA_ZOOM, CameraController.CAMERA_ZOOM);
		preferences.putFloat(RECHARGE_POWER, CollisionSystem.RECHARGE_POWER);
		preferences.putFloat(SHIELD_CONSUME, EnergyComponent.SHIELD_CONSUME);
		preferences.flush();
	}

	/**
	 * Loads and injects the {@link Preferences}
	 */
	public void load() {
		SpaceFloatGame.LOGGER_LEVEL = preferences.getInteger(LOGGER_LEVEL, SpaceFloatGame.LOGGER_LEVEL);
		CameraController.CAMERA_TYPE = preferences.getString(CAMERA_TYPE, CameraController.CAMERA_TYPE);
		CameraController.setCameraZoom(preferences.getFloat(CAMERA_ZOOM, CameraController.CAMERA_ZOOM));
		CollisionSystem.RECHARGE_POWER = preferences.getFloat(RECHARGE_POWER, CollisionSystem.RECHARGE_POWER);
		EnergyComponent.SHIELD_CONSUME = preferences.getFloat(SHIELD_CONSUME, EnergyComponent.SHIELD_CONSUME);
	}
}
