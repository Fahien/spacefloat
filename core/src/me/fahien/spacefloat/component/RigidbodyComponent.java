package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
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
	private final static short DEFAULT_GROUP = 1; // 1 - Player
	private final static short ALL_FLAG = -1;
	private static final Vector3 LINEAR_FACTOR = new Vector3(1.0f, 0.0f, 1.0f);

	private float mass;
	private float radius;
	private short group;
	private short mask;

	private btCollisionShape shape;
	private btRigidBodyConstructionInfo constructionInfo;
	private btRigidBody rigidbody;

	protected Matrix4 bt_transform = new Matrix4();
	protected Vector3 bt_position = new Vector3();
	protected Vector3 bt_velocity = new Vector3();

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
		rigidbody.setLinearFactor(LINEAR_FACTOR);
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
	public Matrix4 getTransform() {
		return bt_transform;
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
		bt_transform.getTranslation(position);
	}

	/**
	 * Returns the {@link btRigidBody} linear velocity
	 */
	public Vector3 getLinearVelocity() {
		return bt_velocity;
	}

	/**
	 * Returns the {@link btRigidBody} angular velocity
	 */
	public Vector3 getAngularVelocity() {
		return rigidbody.getAngularVelocity();
	}

	/**
	 * Updates local transform
	 */
	public void update() {
		rigidbody.getWorldTransform(bt_transform);
		bt_transform.getTranslation(bt_position);
		bt_velocity.set(rigidbody.getLinearVelocity());
	}

	/**
	 * Rotate rigid body
	 */
	public void rotate(Quaternion quaternion) {
		bt_transform.set(quaternion);
		bt_transform.trn(bt_position);
		rigidbody.setWorldTransform(bt_transform);
	}

	/**
	 * Disposes resources
	 */
	public void dispose() {
		// Dispose Bullet shape
		if (shape != null && !shape.isDisposed()) shape.dispose();
		// Dispose Bullet construction info
		if (constructionInfo != null && !constructionInfo.isDisposed()) constructionInfo.dispose();
		// Dispose Bullet rigid body
		if (rigidbody != null && !rigidbody.isDisposed()) rigidbody.dispose();
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
