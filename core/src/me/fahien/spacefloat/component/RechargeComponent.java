package me.fahien.spacefloat.component;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject.CollisionFlags;

/**
 * The Recharge {@link CollisionComponent}
 *
 * @author Fahien
 */
public class RechargeComponent extends CollisionComponent {
	private static final float RECHARGE_RADIUS = 100.0f;
	public final static short RECHARGE_GROUP = 1 << 3; // 8
	private final static short SPACESHIP_MASK = 1; // The spaceship

	/**
	 * Offset for the energy station model
	 */
	private static final Vector3 offset = new Vector3(-115f, 0f, 207f);

	public RechargeComponent() {
		super(RECHARGE_RADIUS, RECHARGE_GROUP, SPACESHIP_MASK);
	}

	@Override
	protected void setCollisionFlags() {
		getCollisionObject().setCollisionFlags(CollisionFlags.CF_NO_CONTACT_RESPONSE);
	}

	protected Matrix4 m_transform = new Matrix4();

	/**
	 * Sets the {@link btCollisionObject} transform
	 */
	@Override
	public void setTransform(Matrix4 transform) {
		m_transform.set(transform);
		m_transform.translate(offset);
		getCollisionObject().setWorldTransform(m_transform);
	}
}
