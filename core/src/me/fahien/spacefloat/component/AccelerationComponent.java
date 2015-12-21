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

	private Vector3 acceleration;
	private Vector3 eulerAnglesAcceleration;

	public AccelerationComponent() {
		acceleration = new Vector3();
		eulerAnglesAcceleration = new Vector3();
	}

	/**
	 * Returns the Acceleration
	 */
	public Vector3 getAcceleration() {
		return acceleration;
	}

	/**
	 * Returns the Euler Angles Acceleration
	 */
	public Vector3 getEulerAnglesAcceleration() {
		return eulerAnglesAcceleration;
	}

	@Override
	public void write(Json json) {
		json.writeValue(JsonString.JSON_X, acceleration.x);
		json.writeValue(JsonString.JSON_Y, acceleration.y);
		json.writeValue(JsonString.JSON_Z, acceleration.z);
		json.writeValue(JsonString.YAW, eulerAnglesAcceleration.x);
		json.writeValue(JsonString.PITCH, eulerAnglesAcceleration.y);
		json.writeValue(JsonString.ROLL, eulerAnglesAcceleration.z);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		acceleration.x = jsonData.getFloat(JsonString.JSON_X);
		acceleration.y = jsonData.getFloat(JsonString.JSON_Y);
		acceleration.z = jsonData.getFloat(JsonString.JSON_Z);
		eulerAnglesAcceleration.x = jsonData.getFloat(JsonString.YAW);
		eulerAnglesAcceleration.y = jsonData.getFloat(JsonString.PITCH);
		eulerAnglesAcceleration.z = jsonData.getFloat(JsonString.ROLL);
	}
}
