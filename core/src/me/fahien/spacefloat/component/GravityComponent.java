package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import me.fahien.spacefloat.utils.JsonString;

/**
 * The Gravity {@link Component}
 *
 * @author Fahien
 */
public class GravityComponent implements Component, Json.Serializable {
	public static final float G = 0.5f;
	private static final float DEFAULT_MASS = 1f;

	private float mass;
	private ObjectSet<Entity> collisions;

	public GravityComponent() {
		this(DEFAULT_MASS);
	}

	public GravityComponent(float mass) {
		this.mass = mass;
		collisions = new ObjectSet<>();
	}

	/**
	 * Returns the mass
	 */
	public float getMass() {
		return mass;
	}

	/**
	 * Adds a collision
	 */
	public void addCollision(Entity entity) {
		collisions.add(entity);
	}

	/**
	 * Tests whether collide with an entity
	 */
	public boolean collideWith(Entity entity) {
		return collisions.contains(entity);
	}

	/**
	 * Removes a collision
	 */
	public void removeCollision(Entity entity) {
		collisions.remove(entity);
	}

	@Override
	public void write(Json json) {
		json.writeValue(JsonString.MASS, mass);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		mass = jsonData.getFloat(JsonString.MASS);
	}
}
