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
	private static final float VELOCITY_CTZ = 1.0f;
	private static final float MAX_ROTATIONVELOCITY = 4.0f;
	private ComponentMapper<GraphicComponent> gm = getFor(GraphicComponent.class);
	private ComponentMapper<TransformComponent> tm = getFor(TransformComponent.class);
	private ComponentMapper<VelocityComponent> vm = getFor(VelocityComponent.class);
	private ComponentMapper<AccelerationComponent> am = getFor(AccelerationComponent.class);

	// Temp variables
	protected GraphicComponent graphicComponent;
	protected TransformComponent transformComponent;
	protected VelocityComponent velocityComponent;
	protected AccelerationComponent accelerationComponent;
	protected Vector3 position;
	protected Vector3 rotation;
	protected Vector3 velocity;
	protected Vector3 rotationVelocity;
	protected Vector3 tempAcceleration;
	protected Quaternion quaternion;

	public PhysicSystem() {
		super(Family.all(GraphicComponent.class, TransformComponent.class).get());
		tempAcceleration = new Vector3();
		quaternion = new Quaternion();
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

	@Override
	protected void processEntity(Entity entity, float delta) {
		// Get the components of the current entity
		graphicComponent = gm.get(entity);
		transformComponent = tm.get(entity);
		velocityComponent = vm.get(entity);
		accelerationComponent = am.get(entity);

		if (velocityComponent != null) {
			// Get the position and the rotation
			position = transformComponent.getPosition();
			rotation = transformComponent.getRotation();
			// Get the velocity and rotationVelocity
			velocity = velocityComponent.getVelocity();
			rotationVelocity = velocityComponent.getEulerAnglesVelocity();
			if (accelerationComponent != null) {
				updateVelocity(accelerationComponent.getAcceleration(), velocity, graphicComponent, delta);
				updateRotationVelocity(accelerationComponent.getEulerAnglesAcceleration(), rotationVelocity, delta);
			}
			if (!rotationVelocity.equals(Vector3.Zero)) {
				rotation.x += rotationVelocity.x * delta;
				rotation.y += rotationVelocity.y * delta;
				rotation.z += rotationVelocity.z * delta;
			}
			if (!velocity.equals(Vector3.Zero)) {
				position.x += velocity.x * delta;
				position.y += velocity.y * delta;
				position.z += velocity.z * delta;
			}
			quaternion.setEulerAnglesRad(rotation.x, rotation.y, rotation.z);
			graphicComponent.getInstance().transform.set(quaternion);
			graphicComponent.setPosition(position);
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

	/**
	 * Updates the velocity
	 */
	private void updateVelocity(Vector3 acceleration, Vector3 velocity, GraphicComponent graphic, float delta) {
		if (!acceleration.equals(Vector3.Zero)) {
			tempAcceleration.x = acceleration.x;
			tempAcceleration.y = acceleration.y;
			tempAcceleration.z = acceleration.z;
			tempAcceleration.rot(graphic.getInstance().transform);
			// Updates velocity
			velocity.x += tempAcceleration.x * delta;
			velocity.y += tempAcceleration.y * delta;
			velocity.z += tempAcceleration.z * delta;
		} else {
			if (velocity.len2() < VELOCITY_CTZ) {
				velocity.x = velocity.y = velocity.z = 0.0f;
			}
		}
	}
}
