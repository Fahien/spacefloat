package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectSet;

import me.fahien.spacefloat.entity.GameObject;
import me.fahien.spacefloat.utils.JsonKey;

/**
 * The Collision {@link Component}
 *
 * @author Fahien
 */
public abstract class CollisionComponent implements Component, Json.Serializable {
	private static final float DEFAULT_RADIUS = 128.0f;
	private final static short DEFAULT_GROUP = 1 << 1; // 10
	private final static short DEFAULT_MASK = 0; // Nothing

	private ObjectSet<GameObject> collisions;
	private float radius;

	private btCollisionShape shape;
	protected btCollisionObject collisionObject;
	private short group;
	private short mask;

	public CollisionComponent() {
		this(DEFAULT_RADIUS);
	}

	public CollisionComponent(float radius) {
		this(radius, DEFAULT_GROUP, DEFAULT_MASK);
	}

	public CollisionComponent(float radius, short group, short mask) {
		this.radius = radius;
		this.group = group;
		this.mask = mask;
		collisions = new ObjectSet<>();
	}

	/**
	 * Adds a collision
	 */
	public void addCollision(GameObject object) {
		collisions.add(object);
	}

	/**
	 * Tests whether collide with an entity
	 */
	public boolean collideWith(GameObject object) {
		return collisions.contains(object);
	}

	/**
	 * Removes a collision
	 */
	public void removeCollision(GameObject object) {
		collisions.remove(object);
	}

	/**
	 * Returns the radius
	 */
	public float getRadius() {
		return radius;
	}

	/**
	 * Creates the {@link btCollisionShape}
	 */
	protected void createShape() {
		shape = new btSphereShape(radius);
	}

	/**
	 * Creates the {@link btCollisionObject}
	 */
	public void createCollisionObject() {
		if (collisionObject != null) return;
		if (shape == null) createShape();
		collisionObject = new btCollisionObject();
		collisionObject.setCollisionShape(shape);
		collisionObject.setCollisionFlags(collisionObject.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
	}

	/**
	 * Returns the {@link btCollisionObject}
	 */
	public btCollisionObject getCollisionObject() {
		return collisionObject;
	}

	/**
	 * Returns the bullet group
	 */
	public short getGroup() {
		return group;
	}

	/**
	 * Returns the bullet mask
	 */
	public short getMask() {
		return mask;
	}

	/**
	 * Returns the {@link btCollisionObject} position
	 */
	public void getPosition(Vector3 position) {
		collisionObject.getWorldTransform().getTranslation(position);
	}

	/**
	 * Returns the {@link btCollisionObject} transform
	 */
	public void getTransform(Matrix4 transform) {
		collisionObject.getWorldTransform(transform);
	}

	/**
	 * Sets the {@link btCollisionObject} transform
	 */
	public void setTransform(Matrix4 transform) {
		collisionObject.setWorldTransform(transform);
	}

	public void dispose() {
		// Dispose Bullet shape
		if (shape != null) shape.dispose();
		// Dispose Bullet collision object
		if (collisionObject != null) collisionObject.dispose();
	}

	@Override
	public void write(Json json) {
		json.writeValue(JsonKey.RADIUS, radius);
		json.writeValue(JsonKey.GROUP, group);
		json.writeValue(JsonKey.MASK, mask);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		radius = jsonData.getFloat(JsonKey.RADIUS);
		group = jsonData.getShort(JsonKey.GROUP);
		mask = jsonData.getShort(JsonKey.MASK);
	}
}
