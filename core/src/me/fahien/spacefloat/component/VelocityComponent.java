package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import me.fahien.spacefloat.utils.JsonString;

import static me.fahien.spacefloat.game.SpaceFloatGame.logger;

/**
 * The Velocity {@link Component}
 *
 * @author Fahien
 */
public class VelocityComponent implements Component, Json.Serializable {

	public static final int BOUNCE_LIMIT = 64;
	public static final float ABSORBE_FACTOR = 0.5f;
	private Vector3 velocity;
	private Vector3 eulerAnglesVelocity;

	public VelocityComponent() {
		velocity = new Vector3();
		eulerAnglesVelocity = new Vector3();
	}

	/**
	 * Returns the velocity
	 */
	public Vector3 getVelocity() {
		return velocity;
	}

	/**
	 * Returns the euler Angles
	 */
	public Vector3 getEulerAnglesVelocity() {
		return eulerAnglesVelocity;
	}

	/**
	 * Hurt with normal
	 */
	public void hurt(Vector3 normal) {
		normal.nor();
		logger.debug("Normal: " + normal);
		float dot = 2 * velocity.dot(normal);
		normal.scl(dot);
		logger.debug("Pre hurt velocity: " + velocity);
		velocity.sub(normal).scl(ABSORBE_FACTOR);
		logger.debug("Hurted velocity: " + velocity);
		if (velocity.len2() < BOUNCE_LIMIT) {
			logger.debug("Hurt absorbed");
			velocity.set(Vector3.Zero);
		}
	}

	/**
	 * Collide with normal
	 */
	public void collide(Vector3 normal) {
		normal.nor();
		float dot = velocity.dot(normal);
		logger.debug("Colliding dot: " + dot);
		if (dot > 0) {
			normal.scl(dot);
			velocity.sub(normal);
			logger.debug("Velocity after colliding: " + velocity);
			if (velocity.len2() < BOUNCE_LIMIT) {
				logger.debug("Hurt absorbed");
				velocity.set(Vector3.Zero);
			}
		}
	}

	@Override
	public void write(Json json) {
		json.writeValue(JsonString.JSON_X, velocity.x);
		json.writeValue(JsonString.JSON_Y, velocity.y);
		json.writeValue(JsonString.JSON_Z, velocity.z);
		json.writeValue(JsonString.YAW, eulerAnglesVelocity.x);
		json.writeValue(JsonString.PITCH, eulerAnglesVelocity.y);
		json.writeValue(JsonString.ROLL, eulerAnglesVelocity.z);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		velocity.x = jsonData.getFloat(JsonString.JSON_X);
		velocity.y = jsonData.getFloat(JsonString.JSON_Y);
		velocity.z = jsonData.getFloat(JsonString.JSON_Z);
		eulerAnglesVelocity.x = jsonData.getFloat(JsonString.YAW);
		eulerAnglesVelocity.y = jsonData.getFloat(JsonString.PITCH);
		eulerAnglesVelocity.z = jsonData.getFloat(JsonString.ROLL);
	}
}
