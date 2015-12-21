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

/**
 * The Physic {@link IteratingSystem}
 *
 * @author Fahien
 */
public class PhysicSystem extends IteratingSystem {
	private ComponentMapper<GraphicComponent> gm = getFor(GraphicComponent.class);
	private ComponentMapper<TransformComponent> tm = getFor(TransformComponent.class);
	private ComponentMapper<VelocityComponent> vm = getFor(VelocityComponent.class);
	private ComponentMapper<AccelerationComponent> am = getFor(AccelerationComponent.class);

	private Vector3 tempAcceleration;
	private Quaternion quaternion;

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
		GraphicComponent graphic = gm.get(entity);
		TransformComponent transform = tm.get(entity);
		VelocityComponent velocity = vm.get(entity);
		AccelerationComponent acceleration = am.get(entity);

		Vector3 position = transform.getPosition();
		Vector3 rotation = transform.getRotation();
		if (velocity != null) {
			Vector3 v = velocity.getVelocity();
			Vector3 vr = velocity.getEulerAnglesVelocity();
			if (acceleration != null) {
				updateVelocity(acceleration.getAcceleration(), v, graphic, delta);
				updateRotationVelocity(acceleration.getEulerAnglesAcceleration(), vr, delta);
			}
			if (!vr.equals(Vector3.Zero)) {
				rotation.x += vr.x * delta;
				rotation.y += vr.y * delta;
				rotation.z += vr.z * delta;
			} else {
				if (rotation.x < 0.025f && rotation.x > -0.025f) rotation.x = 0;
				if (rotation.y < 0.025f && rotation.y > -0.025f) rotation.y = 0;
				if (rotation.z < 0.025f && rotation.z > -0.025f) rotation.z = 0;
			}
			if (!v.equals(Vector3.Zero)) {
				position.x += v.x * delta;
				position.y += v.y * delta;
				position.z += v.z * delta;
			}
			quaternion = new Quaternion();
			quaternion.setEulerAnglesRad(rotation.x, rotation.y, rotation.z);
			graphic.getInstance().transform.rotate(quaternion);
			graphic.setPosition(position);
		}
	}

	/**
	 * Updates the rotation velocity
	 */
	private void updateRotationVelocity(Vector3 rotationAcceleration, Vector3 rotationVelocity, float delta) {
		if (!rotationAcceleration.equals(Vector3.Zero)) {
			// Updates the velocity of rotation
			rotationVelocity.x += rotationAcceleration.x * delta;
			rotationVelocity.y += rotationAcceleration.y * delta;
			rotationVelocity.z += rotationAcceleration.z * delta;
		} else {
			if (rotationVelocity.len2() < 0.5f) {
				rotationVelocity.x = rotationVelocity.y = rotationVelocity.z = 0.0f;
			}
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
			if (velocity.len2() < 1f) {
				velocity.x = velocity.y = velocity.z = 0.0f;
			}
		}
	}
}
