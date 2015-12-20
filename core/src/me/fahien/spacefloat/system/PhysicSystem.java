package me.fahien.spacefloat.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector3;

import me.fahien.spacefloat.component.GraphicComponent;
import me.fahien.spacefloat.component.PositionComponent;
import me.fahien.spacefloat.component.VelocityComponent;

import static com.badlogic.ashley.core.ComponentMapper.getFor;

/**
 * The Physic {@link IteratingSystem}
 *
 * @author Fahien
 */
public class PhysicSystem extends IteratingSystem {
	private ComponentMapper<GraphicComponent> gm = getFor(GraphicComponent.class);
	private ComponentMapper<PositionComponent> pm = getFor(PositionComponent.class);
	private ComponentMapper<VelocityComponent> vm = getFor(VelocityComponent.class);

	public PhysicSystem() {
		super(Family.all(GraphicComponent.class, PositionComponent.class).get());
	}


	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		GraphicComponent graphic = gm.get(entity);
		PositionComponent position = pm.get(entity);
		VelocityComponent velocity = vm.get(entity);

		Vector3 p = position.getPosition();
		if (velocity != null) {
			Vector3 v = velocity.getVelocity();
			if (!v.equals(Vector3.Zero)) {
				p.x += v.x * deltaTime;
				p.y += v.y * deltaTime;
				p.z += v.z * deltaTime;
			}
		}

		graphic.setPosition(p);
	}
}
