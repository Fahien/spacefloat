package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import me.fahien.spacefloat.utils.JsonString;

/**
 * The Collision {@link Component}
 */
public class CollisionComponent implements Component, Json.Serializable {
	private static final float DEFAULT_RADIUS = 1.0f;

	private float radius;
	private btCollisionShape shape;

	public CollisionComponent() {
		this(DEFAULT_RADIUS);
	}

	public CollisionComponent(float radius) {
		this.radius = radius;
	}

	/**
	 * Returns the radius
	 */
	public float getRadius() {
		return radius;
	}

	/**
	 * Sets the {@link btCollisionShape}
	 */
	public void setShape(btCollisionShape shape) {
		this.shape = shape;
	}

	@Override
	public void write(Json json) {
		json.writeValue(JsonString.RADIUS, radius);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		radius = jsonData.getFloat(JsonString.RADIUS);
	}

	public btCollisionShape getShape() {
		return shape;
	}
}
