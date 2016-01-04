package me.fahien.spacefloat.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import me.fahien.spacefloat.camera.MainCamera;
import me.fahien.spacefloat.component.EnergyComponent;
import me.fahien.spacefloat.controller.SpaceshipController;
import me.fahien.spacefloat.controller.SpaceshipController2D;
import me.fahien.spacefloat.game.SpaceFloatGame;
import me.fahien.spacefloat.system.CollisionSystem;

/**
 * SpaceFloat Preferences
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
	protected static final String CAMERA_FAR = "cameraFar";

	private Preferences preferences;

	public SpaceFloatPreferences() {
		preferences = Gdx.app.getPreferences(CONFIG_FILE);
	}

	public Preferences getPreferences() {
		return preferences;
	}

	public void save() {
		preferences.putInteger(LOGGER_LEVEL, SpaceFloatGame.LOGGER_LEVEL);
		preferences.putFloat(CAMERA_FAR, MainCamera.FAR);
		preferences.putFloat(ACCELERATION, SpaceshipController2D.ACCELERATION);
		preferences.putFloat(RECHARGE_POWER, CollisionSystem.RECHARGE_POWER);
		preferences.putFloat(REACTOR_CONSUME, SpaceshipController.REACTOR_CONSUMES);
		preferences.putFloat(SHIELD_CONSUME, EnergyComponent.SHIELD_CONSUME);
		preferences.flush();
	}

	/**
	 * Load and inject the {@link Preferences}
	 */
	public void load() {
		SpaceFloatGame.LOGGER_LEVEL = preferences.getInteger(LOGGER_LEVEL, SpaceFloatGame.LOGGER_LEVEL);
		MainCamera.FAR = preferences.getFloat(CAMERA_FAR, MainCamera.FAR);
		SpaceshipController2D.ACCELERATION = preferences.getFloat(ACCELERATION, SpaceshipController2D.ACCELERATION);
		CollisionSystem.RECHARGE_POWER = preferences.getFloat(RECHARGE_POWER, CollisionSystem.RECHARGE_POWER);
		SpaceshipController.REACTOR_CONSUMES = preferences.getFloat(REACTOR_CONSUME, SpaceshipController.REACTOR_CONSUMES);
		EnergyComponent.SHIELD_CONSUME = preferences.getFloat(SHIELD_CONSUME, EnergyComponent.SHIELD_CONSUME);
	}
}
