package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import me.fahien.spacefloat.utils.JsonString;

/**
 * The Gravity {@link Component}
 *
 * @author Fahien
 */
public class GravityComponent extends CollisionComponent {
	public static final float G = 0.5f;
	private static final float DEFAULT_MASS = 1f;

	private float mass;

	public GravityComponent() {
		this(DEFAULT_MASS);
	}

	public GravityComponent(float mass) {
		this.mass = mass;
	}

	/**
	 * Returns the mass
	 */
	public float getMass() {
		return mass;
	}

	@Override
	public void write(Json json) {
		super.write(json);
		json.writeValue(JsonString.MASS, mass);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		super.read(json, jsonData);
		mass = jsonData.getFloat(JsonString.MASS);
	}
}
