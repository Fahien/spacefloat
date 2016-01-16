package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

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
	public void setName(final String name) {
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
	public void setPosition(final Vector3 position){
		this.position = position;
	}

	/**
	 * Returns the indicator
	 */
	public Vector3 getIndicator() {
		return indicator;
	}

	@Override
	public void write(final Json json) {
		json.writeValue(JsonKey.NAME, name);
	}

	@Override
	public void read(final Json json, final JsonValue jsonData) {
		name = jsonData.getString(JsonKey.NAME);
	}
}
