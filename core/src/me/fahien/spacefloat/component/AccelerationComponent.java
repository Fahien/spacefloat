package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import me.fahien.spacefloat.utils.JsonString;

/**
 * The Acceleration {@link Component}
 *
 * @author Fahien
 */
public class AccelerationComponent implements Component, Json.Serializable {

	private Vector3 gravity;
	private Vector3 eulerAnglesAcceleration;

	public AccelerationComponent() {
		gravity = new Vector3();
		eulerAnglesAcceleration = new Vector3();
	}

	/**
	 * Returns the gravity
	 */
	public Vector3 getAcceleration() {
		return gravity;
	}

	/**
	 * Returns the Euler Angles Acceleration
	 */
	public Vector3 getEulerAnglesAcceleration() {
		return eulerAnglesAcceleration;
	}

	@Override
	public void write(Json json) {
		json.writeValue(JsonString.JSON_X, gravity.x);
		json.writeValue(JsonString.JSON_Y, gravity.y);
		json.writeValue(JsonString.JSON_Z, gravity.z);
		json.writeValue(JsonString.YAW, eulerAnglesAcceleration.x);
		json.writeValue(JsonString.PITCH, eulerAnglesAcceleration.y);
		json.writeValue(JsonString.ROLL, eulerAnglesAcceleration.z);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		gravity.x = jsonData.getFloat(JsonString.JSON_X);
		gravity.y = jsonData.getFloat(JsonString.JSON_Y);
		gravity.z = jsonData.getFloat(JsonString.JSON_Z);
		eulerAnglesAcceleration.x = jsonData.getFloat(JsonString.YAW);
		eulerAnglesAcceleration.y = jsonData.getFloat(JsonString.PITCH);
		eulerAnglesAcceleration.z = jsonData.getFloat(JsonString.ROLL);
	}
}
