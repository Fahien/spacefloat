package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import me.fahien.spacefloat.utils.JsonKey;

/**
 * The Gravity {@link Component}
 *
 * @author Fahien
 */
public class GravityComponent extends CollisionComponent {
	public static final float G = 0.5f;
	private static final float DEFAULT_MASS = 1f;
	private static final float PLANET_RADIUS = 128.0f;
	public static final short PLANET_GROUP = 1 << 2;
	private static final short DEFAULT_MASK = 1 | HurtComponent.HURT_GROUP;

	private float mass;

	public GravityComponent() {
		super(PLANET_RADIUS, PLANET_GROUP, DEFAULT_MASK);
		this.mass = DEFAULT_MASS;
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
		json.writeValue(JsonKey.MASS, mass);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		super.read(json, jsonData);
		mass = jsonData.getFloat(JsonKey.MASS);
	}
}
