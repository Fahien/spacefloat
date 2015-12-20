package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import me.fahien.spacefloat.utils.JsonString;

/**
 * The Position {@link Component}
 *
 * @author Fahien
 */
public class PositionComponent implements Component, Json.Serializable {

	private Vector3 position;

	public PositionComponent() {
		position = new Vector3();
	}

	public Vector3 getPosition() {
		return position;
	}

	@Override
	public void write(Json json) {
		json.writeValue(JsonString.JSON_X, position.x);
		json.writeValue(JsonString.JSON_Y, position.y);
		json.writeValue(JsonString.JSON_Z, position.z);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		position.x = jsonData.getFloat(JsonString.JSON_X);
		position.y = jsonData.getFloat(JsonString.JSON_Y);
		position.z = jsonData.getFloat(JsonString.JSON_Z);
	}
}
