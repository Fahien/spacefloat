package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import me.fahien.spacefloat.utils.JsonKey;

import static me.fahien.spacefloat.game.SpaceFloatGame.logger;

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
	 * Returns the acceleration
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
		json.writeValue(JsonKey.X, acceleration.x);
		json.writeValue(JsonKey.Y, acceleration.y);
		json.writeValue(JsonKey.Z, acceleration.z);
		json.writeValue(JsonKey.YAW, eulerAnglesAcceleration.x);
		json.writeValue(JsonKey.PITCH, eulerAnglesAcceleration.y);
		json.writeValue(JsonKey.ROLL, eulerAnglesAcceleration.z);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		acceleration.x = jsonData.getFloat(JsonKey.X);
		acceleration.y = jsonData.getFloat(JsonKey.Y);
		acceleration.z = jsonData.getFloat(JsonKey.Z);
		eulerAnglesAcceleration.x = jsonData.getFloat(JsonKey.YAW);
		eulerAnglesAcceleration.y = jsonData.getFloat(JsonKey.PITCH);
		eulerAnglesAcceleration.z = jsonData.getFloat(JsonKey.ROLL);
	}

	public void collide(Vector3 normal) {
		normal.nor();
		float dot = acceleration.dot(normal);
		logger.debug("Colliding dot: " + dot);
		if (dot < 0) {
			normal.scl(dot);
			acceleration.sub(normal);
			logger.debug("Acceleration after colliding: " + acceleration);
		}
	}
}
