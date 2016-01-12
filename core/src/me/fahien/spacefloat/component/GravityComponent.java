package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btManifoldPoint;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import me.fahien.spacefloat.entity.GameObject;
import me.fahien.spacefloat.utils.JsonKey;

import static me.fahien.spacefloat.component.ComponentMapperEnumerator.rigidMapper;

/**
 * The Gravity {@link Component}
 *
 * @author Fahien
 */
public class GravityComponent extends CollisionComponent {
	public static final float G = 1024*1024*16f;
	private static final float DEFAULT_MASS = 64f;
	private static final float GRAVITY_RADIUS = 128.0f;
	public static final short GRAVITY_GROUP = 1 << 2; // 4 - Planet
	private static final short DEFAULT_MASK = 1 | 2; // 1 - Player + 2 - Object

	private float mass;

	public GravityComponent() {
		super(GRAVITY_RADIUS, GRAVITY_GROUP, DEFAULT_MASK);
		this.mass = DEFAULT_MASS;
	}

	@Override
	protected void setCollisionFlags() {
		setCollisionFlags(CollisionFlags.CF_NO_CONTACT_RESPONSE);
	}

	@Override
	public void collideWith(float delta, btManifoldPoint collisionPoint, GameObject source, GameObject target) {
		collideWith(rigidMapper.get(target));
	}

	protected Vector3 gravity = new Vector3();
	protected Vector3 rigidbodyPosition = new Vector3();

	private void collideWith(RigidbodyComponent rigidbodyComponent) {
		if (rigidbodyComponent != null) {
			// Set gravity vector equal to this collision object position
			getPosition(gravity);
			// Get the rigid body position
			rigidbodyComponent.getPosition(rigidbodyPosition);
			// Compute the attractive vector
			gravity.sub(rigidbodyPosition).nor().scl(G * mass);
			// Apply attraction
			rigidbodyComponent.applyCentralForce(gravity);
		}
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
