package me.fahien.spacefloat.component;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btGhostObject;

import me.fahien.spacefloat.entity.GameObject;

import static me.fahien.spacefloat.component.ComponentMapperEnumerator.energyMapper;

/**
 * The Recharge Component
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

	/**
	 * Creates the {@link btCollisionObject}
	 */
	@Override
	public void createCollisionObject() {
		if (shape == null) createShape();
		setCollisionShape(shape);
		setCollisionFlags(btGhostObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
	}

	protected Matrix4 m_transform = new Matrix4();

	/**
	 * Sets the {@link btGhostObject} transform
	 */
	@Override
	public void setTransform(Matrix4 transform) {
		m_transform.set(transform);
		m_transform.translate(offset);
		setWorldTransform(m_transform);
	}

	protected EnergyComponent m_energyComponent;

	@Override
	public void collideWith(GameObject gameObject) {
		m_energyComponent = energyMapper.get(gameObject);
		if (m_energyComponent != null) {
			m_energyComponent.recharge();
		}
	}
}
