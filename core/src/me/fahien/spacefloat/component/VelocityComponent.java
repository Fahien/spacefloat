package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import me.fahien.spacefloat.utils.JsonKey;

/**
 * The Velocity {@link Component}
 *
 * @author Fahien
 */
public class VelocityComponent implements Component, Json.Serializable {
	private Vector3 velocity;
	private Vector3 angularVelocity;

	public VelocityComponent() {
		velocity = new Vector3();
		angularVelocity = new Vector3();
	}

	/**
	 * Returns the velocity
	 */
	public Vector3 getVelocity() {
		return velocity;
	}

	/**
	 * Sets the velocity
	 */
	public void setVelocity(Vector3 velocity) {
		this.velocity = velocity;
	}

	/**
	 * Returns the angular velocity
	 */
	public Vector3 getAngularVelocity() {
		return angularVelocity;
	}

	@Override
	public void write(Json json) {
		json.writeValue(JsonKey.X, velocity.x);
		json.writeValue(JsonKey.Y, velocity.y);
		json.writeValue(JsonKey.Z, velocity.z);
		json.writeValue(JsonKey.YAW, angularVelocity.x);
		json.writeValue(JsonKey.PITCH, angularVelocity.y);
		json.writeValue(JsonKey.ROLL, angularVelocity.z);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		velocity.x = jsonData.getFloat(JsonKey.X);
		velocity.y = jsonData.getFloat(JsonKey.Y);
		velocity.z = jsonData.getFloat(JsonKey.Z);
		angularVelocity.x = jsonData.getFloat(JsonKey.YAW);
		angularVelocity.y = jsonData.getFloat(JsonKey.PITCH);
		angularVelocity.z = jsonData.getFloat(JsonKey.ROLL);
	}

	public void setAngularVelocity(Vector3 angularVelocity) {
		this.angularVelocity = angularVelocity;
	}
}
