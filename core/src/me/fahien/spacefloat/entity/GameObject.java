package me.fahien.spacefloat.entity;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import static me.fahien.spacefloat.game.SpaceFloatGame.logger;
import static me.fahien.spacefloat.utils.JsonString.JSON_CLASS;
import static me.fahien.spacefloat.utils.JsonString.JSON_COMPONENTS;
import static me.fahien.spacefloat.utils.JsonString.JSON_NAME;

/**
 * The SpaceFloat {@link Entity}
 *
 * @author Fahien
 */
public class GameObject extends Entity implements Json.Serializable {

	private String name;

	public GameObject() {}

	public GameObject(String name) {
		this.name = name;
	}

	/**
	 * Returns the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void write(Json json) {
		json.writeValue(JSON_NAME, name);
		json.writeValue(JSON_COMPONENTS, getComponents());
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		name = jsonData.getString(JSON_NAME);
		JsonValue components = jsonData.get(JSON_COMPONENTS);
		if (components != null) {
			components = components.child;
			if (components != null) {
				for (JsonValue jsonComponent = components.child; jsonComponent != null; jsonComponent = jsonComponent.next) {
					try {
						Component component = (Component) json.readValue(Class.forName(jsonComponent.getString(JSON_CLASS)), jsonComponent);
						add(component);
					} catch (ClassNotFoundException e) {
						logger.error("Error reading components of a game object", e);
					}
				}
			}
		}
	}
}
