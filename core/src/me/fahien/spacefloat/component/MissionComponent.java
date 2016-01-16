package me.fahien.spacefloat.component;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject.CollisionFlags;

import me.fahien.spacefloat.mission.Mission;

/**
 * Mission {@link CollisionComponent}
 *
 * @author Fahien
 */
public class MissionComponent extends CollisionComponent {
	private static final float PARCEL_RADIUS = 90f;
	private static final short PARCEL_GROUP = 8; // Event
	private static final short PARCEL_MASK = 1|4; // Player + Planet
	private static final float HANDLING_TIME = 0.003f; // 3 seconds

	private static float handlingTime = HANDLING_TIME;

	private Mission mission;
	private boolean collecting;
	private boolean delivering;
	private Vector3 destinationPosition;

	public MissionComponent() {
		super(PARCEL_RADIUS, PARCEL_GROUP, PARCEL_MASK);
	}

	/**
	 * Returns the {@link Mission}
	 */
	public Mission getMission() {
		return mission;
	}

	/**
	 * Sets the {@link Mission}
	 */
	public void setMission(Mission mission) {
		this.mission = mission;
	}

	/**
	 * Returns the handling time
	 */
	public float getHandlingTime() {
		return handlingTime;
	}

	/**
	 * Adds time to the handling time
	 */
	public void addHandlingTime(float time) {
		handlingTime += time;
	}

	/**
	 * Reset the handling time
	 */
	public void resetHandlingTime() {
		if (handlingTime != HANDLING_TIME) handlingTime = HANDLING_TIME;
	}

	/**
	 * Tests whether is collecting
	 */
	public boolean isCollecting() {
		return collecting;
	}

	/**
	 * Sets collecting
	 */
	public void setCollecting(boolean collecting) {
		this.collecting = collecting;
	}

	/**
	 * Tests whether is delivering
	 */
	public boolean isDelivering() {
		return delivering;
	}

	/**
	 * Sets delivering
	 */
	public void setDelivering(boolean delivering) {
		this.delivering = delivering;
	}

	/**
	 * Returns the destination position
	 */
	public Vector3 getDestinationPosition() {
		return destinationPosition;
	}

	/**
	 * Sets the destination position
	 */
	public void setDestinationPosition(final Vector3 destinationPosition) {
		this.destinationPosition = destinationPosition;
	}

	@Override
	protected void setCollisionFlags() {
		getCollisionObject().setCollisionFlags(CollisionFlags.CF_NO_CONTACT_RESPONSE);
	}
}
