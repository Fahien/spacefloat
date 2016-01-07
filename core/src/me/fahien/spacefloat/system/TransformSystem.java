package me.fahien.spacefloat.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

import me.fahien.spacefloat.component.GraphicComponent;
import me.fahien.spacefloat.component.TransformComponent;
import me.fahien.spacefloat.component.VelocityComponent;

import static com.badlogic.ashley.core.Family.all;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.graphicMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.transformMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.velocityMapper;

/**
 * The Transform {@link IteratingSystem}
 *
 * @author Fahien
 */
public class TransformSystem extends IteratingSystem {

	protected GraphicComponent m_graphicComponent;
	protected TransformComponent m_transformComponent;
	protected VelocityComponent m_velocityComponent;

	protected Vector3 m_rotationVelocity;
	protected Vector3 m_rotation;
	protected Vector3 m_velocity;
	protected Vector3 m_position;
	protected Quaternion m_quaternion;

	public TransformSystem() {
		super(all(GraphicComponent.class, VelocityComponent.class, TransformComponent.class).get());
		m_quaternion = new Quaternion();
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		// Apply initial position and rotation to model instances
		for (Entity entity : getEntities()) {
			m_graphicComponent = graphicMapper.get(entity);
			m_transformComponent = transformMapper.get(entity);
			m_graphicComponent.setFromEulerAnglesRad(m_transformComponent.getEulerAngles());
			m_graphicComponent.setPosition(m_transformComponent.getPosition());
		}
	}

	@Override
	protected void processEntity(Entity entity, float delta) {
		// Get the components of the current entity
		m_graphicComponent = graphicMapper.get(entity);
		m_transformComponent = transformMapper.get(entity);
		m_velocityComponent = velocityMapper.get(entity);

		// Get the rotation
		m_rotation = m_transformComponent.getEulerAngles();
		// Get the rotation velocity
		m_rotationVelocity = m_velocityComponent.getEulerAnglesVelocity();

		// Update rotation if rotation velocity is not zero
		if (!m_rotationVelocity.equals(Vector3.Zero)) {
			m_rotation.x += m_rotationVelocity.x * delta;
			m_rotation.y += m_rotationVelocity.y * delta;
			m_rotation.z += m_rotationVelocity.z * delta;
		}

		//Get the position
		m_position = m_transformComponent.getPosition();

		// Get the velocity
		m_velocity = m_velocityComponent.getVelocity();

		// Update position if velocity is not zero
		if (!m_velocity.equals(Vector3.Zero)) {
			m_position.x += m_velocity.x * delta;
			m_position.y += m_velocity.y * delta;
			m_position.z += m_velocity.z * delta;
		}

		// Update position and rotation of model instances
		m_quaternion.setEulerAnglesRad(m_rotation.x, m_rotation.y, m_rotation.z);
		m_graphicComponent.getInstance().transform.set(m_quaternion);
		m_graphicComponent.setPosition(m_position);
	}
}
