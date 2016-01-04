package me.fahien.spacefloat.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

import me.fahien.spacefloat.component.AccelerationComponent;
import me.fahien.spacefloat.component.GraphicComponent;
import me.fahien.spacefloat.component.TransformComponent;
import me.fahien.spacefloat.component.VelocityComponent;

import static com.badlogic.ashley.core.ComponentMapper.getFor;
import static com.badlogic.gdx.math.MathUtils.clamp;

/**
 * The Physic {@link IteratingSystem}
 *
 * @author Fahien
 */
public class PhysicSystem extends IteratingSystem {
	/** Close to Zeros */
	private static final float ROTATIONVELOCITY_CTZ = 0.005f;
	private static final float MAX_ROTATIONVELOCITY = 4.0f;
	private ComponentMapper<GraphicComponent> gm = getFor(GraphicComponent.class);
	private ComponentMapper<TransformComponent> tm = getFor(TransformComponent.class);
	private ComponentMapper<VelocityComponent> vm = getFor(VelocityComponent.class);
	private ComponentMapper<AccelerationComponent> am = getFor(AccelerationComponent.class);

	public PhysicSystem() {
		super(Family.all(GraphicComponent.class, TransformComponent.class).get());
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		applyTransforms(getEntities());
	}

	/**
	 * Apply initial transform to the entities
	 */
	private void applyTransforms(ImmutableArray<Entity> entities) {
		for (Entity entity : entities) {
			GraphicComponent graphic = gm.get(entity);
			TransformComponent transform = tm.get(entity);
			graphic.setFromEulerAngles(transform.getRotation());
			graphic.setPosition(transform.getPosition());
		}
	}

	protected GraphicComponent m_graphicComponent;
	protected TransformComponent m_transformComponent;
	protected VelocityComponent m_velocityComponent;
	protected AccelerationComponent m_accelerationComponent;
	protected Vector3 m_position;
	protected Vector3 m_rotation;
	protected Vector3 m_velocity;
	protected Vector3 m_rotationVelocity;
	protected Quaternion m_quaternion = new Quaternion();

	@Override
	protected void processEntity(Entity entity, float delta) {
		// Get the components of the current entity
		m_graphicComponent = gm.get(entity);
		m_transformComponent = tm.get(entity);
		m_velocityComponent = vm.get(entity);
		m_accelerationComponent = am.get(entity);

		if (m_velocityComponent != null) {
			// Get the position and the rotation
			m_position = m_transformComponent.getPosition();
			m_rotation = m_transformComponent.getRotation();
			// Get the velocity and rotationVelocity
			m_velocity = m_velocityComponent.getVelocity();
			m_rotationVelocity = m_velocityComponent.getEulerAnglesVelocity();
			if (m_accelerationComponent != null) {
				updateVelocity(m_accelerationComponent.getAcceleration(), m_velocity, delta);
				updateRotationVelocity(m_accelerationComponent.getEulerAnglesAcceleration(), m_rotationVelocity, delta);
			}
			if (!m_rotationVelocity.equals(Vector3.Zero)) {
				m_rotation.x += m_rotationVelocity.x * delta;
				m_rotation.y += m_rotationVelocity.y * delta;
				m_rotation.z += m_rotationVelocity.z * delta;
			}
			if (!m_velocity.equals(Vector3.Zero)) {
				m_position.x += m_velocity.x * delta;
				m_position.y += m_velocity.y * delta;
				m_position.z += m_velocity.z * delta;
			}
			m_quaternion.setEulerAnglesRad(m_rotation.x, m_rotation.y, m_rotation.z);
			m_graphicComponent.getInstance().transform.set(m_quaternion);
			m_graphicComponent.setPosition(m_position);
		}
	}

	/**
	 * Updates the rotation velocity
	 */
	private void updateRotationVelocity(Vector3 rotationAcceleration, Vector3 rotationVelocity, float delta) {
		if (!rotationAcceleration.equals(Vector3.Zero)) {
			// Updates the velocityComponent of rotation
			rotationVelocity.x = clamp(rotationVelocity.x + rotationAcceleration.x * delta,
					-MAX_ROTATIONVELOCITY, MAX_ROTATIONVELOCITY);
			rotationVelocity.y = clamp(rotationVelocity.y + rotationAcceleration.y * delta,
					-MAX_ROTATIONVELOCITY, MAX_ROTATIONVELOCITY);
			rotationVelocity.z = clamp(rotationVelocity.z + rotationAcceleration.z * delta,
					-MAX_ROTATIONVELOCITY, MAX_ROTATIONVELOCITY);
		} else if (rotationVelocity.len2() < ROTATIONVELOCITY_CTZ) {
			rotationVelocity.x = rotationVelocity.y = rotationVelocity.z = 0.0f;
		}
	}

	protected Vector3 m_tempAcceleration = new Vector3();

	/**
	 * Updates the velocity
	 */
	private void updateVelocity(Vector3 acceleration, Vector3 velocity, float delta) {
		if (!acceleration.equals(Vector3.Zero)) {
			m_tempAcceleration.set(acceleration);
			velocity.add(m_tempAcceleration.scl(delta));
		}
	}
}
