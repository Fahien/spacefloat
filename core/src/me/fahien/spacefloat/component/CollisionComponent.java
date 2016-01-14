package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject.CollisionFlags;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import me.fahien.spacefloat.utils.JsonKey;

/**
 * The Collision {@link Component}
 *
 * @author Fahien
 */
public class CollisionComponent implements Component, Json.Serializable {
	private static final float DEFAULT_RADIUS = 128.0f;
	private final static short DEFAULT_GROUP = 1 << 1; // 10
	private final static short DEFAULT_MASK = 0; // Nothing

	private float radius;

	protected btCollisionShape shape;
	private short group;
	private short mask;
	private btCollisionObject collisionObject;

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
		setCollisionFlags();
	}

	public btCollisionObject getCollisionObject() {
		return collisionObject;
	}

	/**
	 * Sets the {@link CollisionFlags}
	 */
	protected void setCollisionFlags() {
		collisionObject.setCollisionFlags(collisionObject.getCollisionFlags() | CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
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
	 * Sets the position
	 */
	public void setPosition(Vector3 position) {
		collisionObject.getWorldTransform().trn(position);
	}

	/**
	 * Sets the {@link btCollisionObject} transform
	 */
	public void setTransform(Matrix4 transform) {
		collisionObject.setWorldTransform(transform);
	}

	public void dispose() {
		// Dispose Bullet shape
		if (shape != null && !shape.isDisposed()) shape.dispose();
		// Dispose Bullet collision object
		if (collisionObject != null && !collisionObject.isDisposed()) collisionObject.dispose();
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
