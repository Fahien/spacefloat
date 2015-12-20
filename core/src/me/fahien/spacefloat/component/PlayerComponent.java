package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/**
 * The Player {@link Component}
 */
public class PlayerComponent implements Component, Json.Serializable {
	@Override
	public void write(Json json) {}

	@Override
	public void read(Json json, JsonValue jsonData) {}
}
