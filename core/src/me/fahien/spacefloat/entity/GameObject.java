package me.fahien.spacefloat.entity;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import static me.fahien.spacefloat.game.SpaceFloatGame.logger;
import static me.fahien.spacefloat.utils.JsonKey.CLASS;
import static me.fahien.spacefloat.utils.JsonKey.COMPONENTS;
import static me.fahien.spacefloat.utils.JsonKey.NAME;

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
		json.writeValue(NAME, name);
		json.writeValue(COMPONENTS, getComponents());
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		name = jsonData.getString(NAME);
		JsonValue components = jsonData.get(COMPONENTS);
		if (components != null) {
			components = components.child;
			if (components != null) {
				for (JsonValue jsonComponent = components.child; jsonComponent != null; jsonComponent = jsonComponent.next) {
					try {
						Component component = (Component) json.readValue(Class.forName(jsonComponent.getString(CLASS)), jsonComponent);
						add(component);
					} catch (ClassNotFoundException e) {
						logger.error("Error reading components of a game object", e);
					}
				}
			}
		}
	}
}
