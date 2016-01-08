package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import me.fahien.spacefloat.utils.JsonKey;

/**
 * The Transform {@link Component}
 *
 * @author Fahien
 */
public class TransformComponent implements Component, Json.Serializable {

	private Vector3 position;
	private Vector3 eulerAngles;

	public TransformComponent() {
		this(new Vector3(), new Vector3());

	}

	public TransformComponent(Vector3 position) {
		this(position, new Vector3());
	}

	public TransformComponent(Vector3 position, Vector3 eulerAngles) {
		this.position = position;
		this.eulerAngles = eulerAngles;
	}

	/**
	 * Returns the position
	 */
	public Vector3 getPosition() {
		return position;
	}

	/**
	 * Returns the eulerAngles
	 */
	public Vector3 getEulerAngles() {
		return eulerAngles;
	}

	@Override
	public void write(Json json) {
		json.writeValue(JsonKey.X, position.x);
		json.writeValue(JsonKey.Y, position.y);
		json.writeValue(JsonKey.Z, position.z);
		json.writeValue(JsonKey.YAW, eulerAngles.x);
		json.writeValue(JsonKey.PITCH, eulerAngles.y);
		json.writeValue(JsonKey.ROLL, eulerAngles.z);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		position.x = jsonData.getFloat(JsonKey.X);
		position.y = jsonData.getFloat(JsonKey.Y);
		position.z = jsonData.getFloat(JsonKey.Z);
		eulerAngles.x = jsonData.getFloat(JsonKey.YAW);
		eulerAngles.y = jsonData.getFloat(JsonKey.PITCH);
		eulerAngles.z = jsonData.getFloat(JsonKey.ROLL);
	}
}
