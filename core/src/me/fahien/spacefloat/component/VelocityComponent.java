package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import me.fahien.spacefloat.utils.JsonString;

/**
 * The Velocity {@link Component}
 */
public class VelocityComponent implements Component, Json.Serializable {

	private Vector3 velocity;

	public VelocityComponent() {
		velocity = new Vector3();
	}

	public Vector3 getVelocity() {
		return velocity;
	}

	@Override
	public void write(Json json) {
		json.writeValue(JsonString.JSON_X, velocity.x);
		json.writeValue(JsonString.JSON_Y, velocity.y);
		json.writeValue(JsonString.JSON_Z, velocity.z);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		velocity.x = jsonData.getFloat(JsonString.JSON_X);
		velocity.y = jsonData.getFloat(JsonString.JSON_Y);
		velocity.z = jsonData.getFloat(JsonString.JSON_Z);
	}
}
