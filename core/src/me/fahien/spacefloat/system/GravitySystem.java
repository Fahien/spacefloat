package me.fahien.spacefloat.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector3;

import me.fahien.spacefloat.component.CollisionComponent;
import me.fahien.spacefloat.component.GravityComponent;
import me.fahien.spacefloat.component.PlayerComponent;
import me.fahien.spacefloat.component.TransformComponent;
import me.fahien.spacefloat.component.VelocityComponent;

import static com.badlogic.ashley.core.ComponentMapper.getFor;
import static com.badlogic.ashley.core.Family.all;

/**
 * The Gravity {@link EntitySystem}
 *
 * @author Fahien
 */
public class GravitySystem extends IteratingSystem {
	private static final float MAX_DISTANCE = 2048f;

	private ComponentMapper<GravityComponent> gm = getFor(GravityComponent.class);
	private ComponentMapper<VelocityComponent> am = getFor(VelocityComponent.class);
	private ComponentMapper<TransformComponent> tm = getFor(TransformComponent.class);
	private ComponentMapper<CollisionComponent> cm = getFor(CollisionComponent.class);

	private Entity player;
	private Vector3 playerVelocity;
	private Vector3 playerPosition;
	private Vector3 distanceVector;

	public GravitySystem() {
		super(all(GravityComponent.class, TransformComponent.class).get());
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		ImmutableArray<Entity> entities = engine.getEntitiesFor(all(PlayerComponent.class).get());
		if (entities.size() > 0) {
			player = entities.first();
			playerVelocity = am.get(player).getVelocity();
			playerPosition = tm.get(player).getPosition();
			distanceVector = new Vector3();
		}
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		GravityComponent gravity = gm.get(entity);
		// If the planet collide with player do not apply gravity
		if (gravity.collideWith(player)) return;
		TransformComponent transform = tm.get(entity);
		CollisionComponent collision = cm.get(entity);
		// Set distance vector equal to the planet position
		distanceVector.set(transform.getPosition());
		// Calculate the distance from the player
		float distance = distanceVector.dst(playerPosition);
		// If is within the area and is not collided
		if (distance < MAX_DISTANCE && distance > collision.getRadius() + 2) {
			// Compute the attractive vector
			distanceVector.sub(playerPosition);
			distance = GravityComponent.G * gravity.getMass();
			distanceVector.nor().scl(distance);
			playerVelocity.add(distanceVector);
		}
	}
}
