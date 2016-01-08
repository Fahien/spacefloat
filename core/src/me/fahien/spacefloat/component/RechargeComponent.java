package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btGhostObject;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import me.fahien.spacefloat.utils.JsonKey;

/**
 * The Recharge Component
 *
 * @author Fahien
 */
public class RechargeComponent implements Component, Json.Serializable {
	private static final float RECHARGE_RADIUS = 100.0f;
	public final static short RECHARGE_GROUP = 1 << 3; // 8
	private final static short SPACESHIP_MASK = 1; // The spaceship

	private float radius;
	private short group;
	private short mask;

	private btCollisionShape shape;
	private btGhostObject ghostObject;

	/**
	 * Offset for the energy station model
	 */
	private static final Vector3 offset = new Vector3(-115f, 0f, 207f);

	public RechargeComponent() {
		this(RECHARGE_RADIUS, RECHARGE_GROUP, SPACESHIP_MASK);
	}

	public RechargeComponent(float radius, short group, short mask) {
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
	 * Creates the {@link btCollisionShape}
	 */
	protected void createShape() {
		shape = new btSphereShape(radius);
	}

	/**
	 * Creates the {@link btGhostObject}
	 */
	public void createGhostObject() {
		if (ghostObject != null) return;
		if (shape == null) createShape();
		ghostObject = new btGhostObject();
		ghostObject.setCollisionShape(shape);
		ghostObject.setCollisionFlags(btGhostObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
	}

	/**
	 * Returns the {@link btGhostObject}
	 */
	public btGhostObject getGhostObject() {
		return ghostObject;
	}

	protected Matrix4 m_transform = new Matrix4();

	/**
	 * Sets the {@link btGhostObject} transform
	 */
	public void setTransform(Matrix4 transform) {
		m_transform.set(transform);
		m_transform.translate(offset);
		ghostObject.setWorldTransform(m_transform);
	}

	public void dispose() {
		if (shape != null) shape.dispose();
		if (ghostObject != null) ghostObject.dispose();
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

	/**
	 * Returns the {@link btGhostObject} position
	 */
	public void getPosition(Vector3 position) {
		ghostObject.getWorldTransform().getTranslation(position);
	}
}
