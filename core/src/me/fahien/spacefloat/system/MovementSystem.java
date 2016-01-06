package me.fahien.spacefloat.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector3;

import me.fahien.spacefloat.component.AccelerationComponent;
import me.fahien.spacefloat.component.VelocityComponent;

import static com.badlogic.ashley.core.Family.all;
import static com.badlogic.gdx.math.MathUtils.clamp;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.accelerationMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.velocityMapper;

/**
 * The Movement {@link IteratingSystem}
 *
 * @author Fahien
 */
public class MovementSystem extends IteratingSystem {
	/** Close to Zeros */
	private static final float ROTATIONVELOCITY_CTZ = 0.005f;
	private static final float MAX_ROTATIONVELOCITY = 4.0f;

	protected VelocityComponent m_velocityComponent;
	protected AccelerationComponent m_accelerationComponent;
	protected Vector3 m_velocity;
	protected Vector3 m_rotationVelocity;
	protected Vector3 m_acceleration;
	protected Vector3 m_rotationAcceleration;
	protected Vector3 m_tempAcceleration;

	public MovementSystem() {
		super(all(VelocityComponent.class, AccelerationComponent.class).get());
		m_tempAcceleration = new Vector3();
	}

	@Override
	protected void processEntity(Entity entity, float delta) {
		// Get the components of the current entity
		m_velocityComponent = velocityMapper.get(entity);
		m_accelerationComponent = accelerationMapper.get(entity);

		// Get the velocity
		m_velocity = m_velocityComponent.getVelocity();

		// Get the acceleration
		m_acceleration = m_accelerationComponent.getAcceleration();

		// Update the velocity is acceleration is not zero
		if (!m_acceleration.equals(Vector3.Zero)) {
			m_tempAcceleration.set(m_acceleration);
			m_velocity.add(m_tempAcceleration.scl(delta));
		}

		// Get the rotation velocity
		m_rotationVelocity = m_velocityComponent.getEulerAnglesVelocity();

		// Get the euler angles acceleration
		m_rotationAcceleration = m_accelerationComponent.getEulerAnglesAcceleration();

		// Update the rotation velocity if the acceleration is not zero
		if (!m_rotationAcceleration.equals(Vector3.Zero)) {
			m_rotationAcceleration.x = clamp(m_rotationAcceleration.x + m_rotationAcceleration.x * delta,
					-MAX_ROTATIONVELOCITY, MAX_ROTATIONVELOCITY);
			m_rotationAcceleration.y = clamp(m_rotationAcceleration.y + m_rotationAcceleration.y * delta,
					-MAX_ROTATIONVELOCITY, MAX_ROTATIONVELOCITY);
			m_rotationAcceleration.z = clamp(m_rotationAcceleration.z + m_rotationAcceleration.z * delta,
					-MAX_ROTATIONVELOCITY, MAX_ROTATIONVELOCITY);
		} else if (m_rotationAcceleration.len2() < ROTATIONVELOCITY_CTZ) {
			m_rotationAcceleration.x = m_rotationAcceleration.y = m_rotationAcceleration.z = 0.0f;
		}
	}
}
