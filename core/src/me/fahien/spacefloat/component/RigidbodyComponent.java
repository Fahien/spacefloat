package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import me.fahien.spacefloat.utils.JsonKey;

/**
 * Rigidbody {@link Component}
 *
 * @author Fahien
 */
public class RigidbodyComponent implements Component, Json.Serializable {
	private static final Vector3 localInertia = new Vector3();
	private final static float DEFAULT_MASS = 1f;
	private final static float DEFAULT_RADIUS = 10f;
	private final static short DEFAULT_GROUP = 1;
	private final static short ALL_FLAG = -1;

	private float mass;
	private float radius;
	private short group;
	private short mask;

	private btCollisionShape shape;
	private btRigidBodyConstructionInfo constructionInfo;
	private btRigidBody rigidbody;

	public RigidbodyComponent() {
		this(DEFAULT_MASS, DEFAULT_RADIUS, DEFAULT_GROUP, ALL_FLAG);
	}

	public RigidbodyComponent(float mass, float radius, short group, short mask) {
		this.mass = mass;
		this.radius = radius;
		this.group = group;
		this.mask = mask;
	}

	/**
	 * Returns the local inertia
	 */
	protected Vector3 getLocalInertia() {
		return localInertia;
	}

	/**
	 * Sets the mass
	 */
	public void setMass(float mass) {
		this.mass = mass;
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
	public void createShape() {
		shape = new btSphereShape(radius);
	}

	/**
	 * Returns the {@link btCollisionShape}
	 */
	public btCollisionShape getShape() {
		return shape;
	}

	/**
	 * Sets the {@link btCollisionShape}
	 */
	public void setShape(btBoxShape shape) {
		this.shape = shape;
	}

	/**
	 * Calculates the local inertia
	 */
	protected void calculateLocalInertia() {
		if (mass > 0f && shape != null) {
			shape.calculateLocalInertia(mass, localInertia);
		}
		else {
			localInertia.set(0, 0, 0);
		}
	}

	/**
	 * Creates the {@link btRigidBodyConstructionInfo}
	 */
	protected void createConstructionInfo() {
		calculateLocalInertia();
		constructionInfo = new btRigidBodyConstructionInfo(mass, null, shape, localInertia);
	}

	/**
	 * Returns the {@link btRigidBodyConstructionInfo}
	 */
	protected btRigidBodyConstructionInfo getConstructionInfo() {
		return constructionInfo;
	}

	/**
	 * Creates the {@link btRigidBody}
	 */
	public void createRigidbody() {
		if (rigidbody != null) return;
		if (shape == null) createShape();
		if (constructionInfo == null) createConstructionInfo();
		rigidbody = new btRigidBody(constructionInfo);
		rigidbody.setCollisionFlags(rigidbody.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
	}

	/**
	 * Returns the {@link btRigidBody}
	 */
	public btRigidBody getRigidbody() {
		return rigidbody;
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
	 * Returns the {@link btRigidBody} transform
	 */
	public void getTransform(Matrix4 transform) {
		rigidbody.getWorldTransform(transform);
	}

	/**
	 * Sets the {@link btRigidBody} transform
	 */
	public void setTransform(Matrix4 transform) {
		rigidbody.setWorldTransform(transform);
	}

	/**
	 * Applies a central force
	 */
	public void applyCentralForce(Vector3 force) {
		rigidbody.applyCentralForce(force);
	}

	/**
	 * Returns the {@link btRigidBody} position
	 */
	public void getPosition(Vector3 position) {
		rigidbody.getWorldTransform().getTranslation(position);
	}

	/**
	 * Disposes resources
	 */
	public void dispose() {
		// Dispose Bullet shape
		if (shape != null) shape.dispose();
		// Dispose Bullet construction info
		if (constructionInfo != null) constructionInfo.dispose();
		// Dispose Bullet rigid body
		if (rigidbody != null) rigidbody.dispose();
	}

	@Override
	public void write(Json json) {
		json.writeValue(JsonKey.MASS, mass);
		json.writeValue(JsonKey.RADIUS, radius);
		json.writeValue(JsonKey.GROUP, group);
		json.writeValue(JsonKey.MASK, mask);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		mass = jsonData.getFloat(JsonKey.MASS);
		radius = jsonData.getFloat(JsonKey.RADIUS);
		group = jsonData.getShort(JsonKey.GROUP);
		mask = jsonData.getShort(JsonKey.MASK);
	}
}
