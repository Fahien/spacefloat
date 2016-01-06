package me.fahien.spacefloat.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector3;

import me.fahien.spacefloat.component.GravityComponent;
import me.fahien.spacefloat.component.PlayerComponent;
import me.fahien.spacefloat.component.TransformComponent;
import me.fahien.spacefloat.entity.GameObject;

import static com.badlogic.ashley.core.Family.all;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.gravityMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.transformMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.velocityMapper;
import static me.fahien.spacefloat.game.SpaceFloatGame.logger;

/**
 * The Gravity {@link EntitySystem}
 *
 * @author Fahien
 */
public class GravitySystem extends IteratingSystem {
	public static final float MAX_DISTANCE = 2048f;

	private GameObject player;
	private Vector3 playerVelocity;
	private Vector3 playerPosition;
	private Vector3 distanceVector;

	public GravitySystem() {
		super(all(GravityComponent.class, TransformComponent.class).get());
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		logger.info("Registering into the GravitySystem " + getEntities().size() + " game objects with a GravityComponent and a TransformComponent");
		ImmutableArray<Entity> entities = engine.getEntitiesFor(all(PlayerComponent.class).get());
		if (entities.size() > 0) {
			player = (GameObject) entities.first();
			playerVelocity = velocityMapper.get(player).getVelocity();
			playerPosition = transformMapper.get(player).getPosition();
			distanceVector = new Vector3();
		}
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		computeGravity(gravityMapper.get(entity), transformMapper.get(entity));
	}

	private void computeGravity(GravityComponent gravity, TransformComponent transform) {
		// If the mass is zero do nothing
		if (gravity.getMass() == 0f) return;

		// If the planet collide with player do not apply gravity
		if (gravity.collideWith(player)) return;

		// Set distance vector equal to the planet position
		distanceVector.set(transform.getPosition());
		// Calculate the distance from the player
		float distance = distanceVector.dst(playerPosition);
		// If is within the area and is not collided
		if (distance < MAX_DISTANCE && distance > gravity.getRadius()) {
			// Compute the attractive vector
			distanceVector.sub(playerPosition);
			distance = GravityComponent.G * gravity.getMass();
			distanceVector.nor().scl(distance);
			playerVelocity.add(distanceVector);
			logger.debug("Computing attraction on " + player.getName() + " - velocity: " + playerVelocity);
		}
	}
}
