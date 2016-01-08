package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btGhostObject;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import me.fahien.spacefloat.entity.GameObject;
import me.fahien.spacefloat.utils.JsonKey;

/**
 * The Gravity {@link Component}
 *
 * @author Fahien
 */
public class GravityComponent extends CollisionComponent {
	public static final float G = 64f;
	private static final float DEFAULT_MASS = 64f;
	private static final float PLANET_RADIUS = 128.0f;
	public static final short PLANET_GROUP = 1 << 2;
	private static final short DEFAULT_MASK = 1 | HurtComponent.HURT_GROUP;

	private float mass;

	public GravityComponent() {
		super(PLANET_RADIUS, PLANET_GROUP, DEFAULT_MASK);
		this.mass = DEFAULT_MASS;
	}

	@Override
	public void createCollisionObject() {
		if (shape == null) createShape();
		setCollisionShape(shape);
		setCollisionFlags(btGhostObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
	}

	@Override
	public void collideWith(GameObject gameObject) {
		collideWith(ComponentMapperEnumerator.rigidMapper.get(gameObject));
		collideWith(ComponentMapperEnumerator.hurtMapper.get(gameObject));

	}

	protected Vector3 gravity = new Vector3();
	protected Vector3 rigidbodyPosition = new Vector3();
	protected Vector3 rigidbodyVelocity;

	private void collideWith(RigidbodyComponent rigidbodyComponent) {
		if (rigidbodyComponent != null) {
			// Get velocity of the rigid body
			rigidbodyVelocity = rigidbodyComponent.getLinearVelocity();
			// Set gravity vector equal to this collision object position
			getPosition(gravity);
			// Get the rigid body position
			rigidbodyComponent.getPosition(rigidbodyPosition);
			// Compute the attractive vector
			gravity.sub(rigidbodyPosition).nor().scl(G * mass);
			// Apply attraction
			rigidbodyVelocity.add(gravity);
			// Update rigid body velocity
			rigidbodyComponent.setLinearVelocity(rigidbodyVelocity);
		}
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
