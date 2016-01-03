package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import me.fahien.spacefloat.utils.JsonString;

/**
 * The Transform {@link Component}
 *
 * @author Fahien
 */
public class TransformComponent implements Component, Json.Serializable {

	private Vector3 position;
	private Vector3 rotation;

	public TransformComponent() {
		this(new Vector3(), new Vector3());

	}

	public TransformComponent(Vector3 position) {
		this(position, new Vector3());
	}

	public TransformComponent(Vector3 position, Vector3 rotation) {
		this.position = position;
		this.rotation = rotation;
	}

	/**
	 * Returns the position
	 */
	public Vector3 getPosition() {
		return position;
	}

	/**
	 * Returns the rotation
	 */
	public Vector3 getRotation() {
		return rotation;
	}

	@Override
	public void write(Json json) {
		json.writeValue(JsonString.JSON_X, position.x);
		json.writeValue(JsonString.JSON_Y, position.y);
		json.writeValue(JsonString.JSON_Z, position.z);
		json.writeValue(JsonString.YAW, rotation.x);
		json.writeValue(JsonString.PITCH, rotation.y);
		json.writeValue(JsonString.ROLL, rotation.z);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		position.x = jsonData.getFloat(JsonString.JSON_X);
		position.y = jsonData.getFloat(JsonString.JSON_Y);
		position.z = jsonData.getFloat(JsonString.JSON_Z);
		rotation.x = jsonData.getFloat(JsonString.YAW);
		rotation.y = jsonData.getFloat(JsonString.PITCH);
		rotation.z = jsonData.getFloat(JsonString.ROLL);
	}
}
