package me.fahien.spacefloat.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector3;

import me.fahien.spacefloat.component.GravityComponent;
import me.fahien.spacefloat.component.PlayerComponent;
import me.fahien.spacefloat.component.TransformComponent;
import me.fahien.spacefloat.component.VelocityComponent;

import static com.badlogic.ashley.core.ComponentMapper.getFor;

/**
 * The Gravity {@link EntitySystem}
 */
public class GravitySystem extends IteratingSystem {
	private static final float MAX_DISTANCE = 2048f;

	private ComponentMapper<GravityComponent> gm = getFor(GravityComponent.class);
	private ComponentMapper<VelocityComponent> am = getFor(VelocityComponent.class);
	private ComponentMapper<TransformComponent> tm = getFor(TransformComponent.class);


	// Temp variables
	private GravityComponent gravity;
	private TransformComponent transform;
	private Vector3 playerVelocity;
	private Vector3 playerPosition;
	private Vector3 distance;

	public GravitySystem() {
		super(Family.all(GravityComponent.class, TransformComponent.class).get());
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		ImmutableArray<Entity> entities = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
		if (entities.size() > 0) {
			Entity player = entities.first();
			playerVelocity = am.get(player).getVelocity();
			playerPosition = tm.get(player).getPosition();
			distance = new Vector3();
		}
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		gravity = gm.get(entity);
		transform = tm.get(entity);
		distance.set(transform.getPosition());
		float dist = distance.dst(playerPosition);
		if (dist < MAX_DISTANCE) {
			distance.sub(playerPosition);
			dist = GravityComponent.G * gravity.getMass();
			distance.nor().scl(dist);
			playerVelocity.add(distance);
		}
	}
}
