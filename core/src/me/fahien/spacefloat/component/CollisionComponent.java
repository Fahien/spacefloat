package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectSet;

import me.fahien.spacefloat.utils.JsonString;

/**
 * The Collision {@link Component}
 *
 * @author Fahien
 */
public abstract class CollisionComponent extends btCollisionObject implements Component, Json.Serializable {
	private static final float DEFAULT_RADIUS = 128.0f;

	private ObjectSet<Entity> collisions;
	private float radius;

	public CollisionComponent() {
		this(DEFAULT_RADIUS);
	}

	public CollisionComponent(float radius) {
		this.radius = radius;
		collisions = new ObjectSet<>();
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

	/**
	 * Returns the radius
	 */
	public float getRadius() {
		return radius;
	}

	/**
	 * Returns the position
	 */
	public Vector3 getPosition(Vector3 position) {
		return getWorldTransform().getTranslation(position);
	}

	/**
	 * Sets the {@link btCollisionObject} transform
	 */
	public void setTransform(Matrix4 transform) {
		setWorldTransform(transform);
	}

	@Override
	public void write(Json json) {
		json.writeValue(JsonString.RADIUS, radius);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		radius = jsonData.getFloat(JsonString.RADIUS);
	}
}
