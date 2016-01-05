package me.fahien.spacefloat.system;

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

import static com.badlogic.ashley.core.Family.all;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.gravityMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.hurtMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.transformMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.velocityMapper;

/**
 * The Gravity {@link EntitySystem}
 *
 * @author Fahien
 */
public class GravitySystem extends IteratingSystem {
	public static final float MAX_DISTANCE = 2048f;

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
			playerVelocity = velocityMapper.get(player).getVelocity();
			playerPosition = transformMapper.get(player).getPosition();
			distanceVector = new Vector3();
		}
	}

	protected GravityComponent m_gravity;
	protected TransformComponent m_transform;
	protected CollisionComponent m_playerCollision;

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		m_gravity = gravityMapper.get(entity);
		// If the mass is zero do nothing
		if (m_gravity.getMass() == 0f) return;
		// If the planet collide with player do not apply gravity
		if (m_gravity.collideWith(player)) return;
		m_transform = transformMapper.get(entity);
		m_playerCollision = hurtMapper.get(player);
		// Set distance vector equal to the planet position
		distanceVector.set(m_transform.getPosition());
		// Calculate the distance from the player
		float distance = distanceVector.dst(playerPosition);
		// If is within the area and is not collided
		if (distance < MAX_DISTANCE && distance > m_gravity.getRadius() + m_playerCollision.getRadius() / 2) {
			// Compute the attractive vector
			distanceVector.sub(playerPosition);
			distance = GravityComponent.G * m_gravity.getMass();
			distanceVector.nor().scl(distance);
			playerVelocity.add(distanceVector);
		}
	}
}
