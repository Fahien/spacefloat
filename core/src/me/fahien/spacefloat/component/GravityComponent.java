package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject.CollisionFlags;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import me.fahien.spacefloat.utils.JsonKey;

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
		getCollisionObject().setCollisionFlags(CollisionFlags.CF_NO_CONTACT_RESPONSE);
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

	public float getMass() {
		return mass;
	}
}
