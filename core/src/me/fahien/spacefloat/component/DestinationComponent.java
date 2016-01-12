package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import me.fahien.spacefloat.screen.SpaceFloatScreen;
import me.fahien.spacefloat.utils.JsonKey;

/**
 * The Destination {@link Component}
 *
 * @author Fahien
 */
public class DestinationComponent implements Component, Json.Serializable {

	private String name;

	private Vector3 position;
	private Vector3 indicator;

	public DestinationComponent() {
		position = new Vector3();
		indicator = new Vector3();
	}

	/**
	 * Returns the destination name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the destination name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the destination position
	 */
	public Vector3 getPosition() {
		return position;
	}

	/**
	 * Sets the destination position
	 */
	public void setPosition(Vector3 position){
		this.position = position;
	}

	/**
	 * Returns the indicator
	 */
	public Vector3 getIndicator() {
		return indicator;
	}

	/**
	 * Returns the indicator position
	 */
	public void updateIndicator(final Vector3 center) {
		indicator.set(position);
		indicator.sub(center);
		indicator.nor().scl(SpaceFloatScreen.HEIGHT);
		indicator.add(center);
	}

	@Override
	public void write(Json json) {
		json.writeValue(JsonKey.NAME, name);
		json.writeValue(JsonKey.X, position.x);
		json.writeValue(JsonKey.Y, position.y);
		json.writeValue(JsonKey.Z, position.z);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		name = jsonData.getString(JsonKey.NAME);
		position.x = jsonData.getShort(JsonKey.X);
		position.y = jsonData.getShort(JsonKey.Y);
		position.z = jsonData.getShort(JsonKey.Z);
	}
}
