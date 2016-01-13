package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btManifoldPoint;

import me.fahien.spacefloat.entity.GameObject;
import me.fahien.spacefloat.factory.MissionFactory;
import me.fahien.spacefloat.game.SpaceFloat;
import me.fahien.spacefloat.game.SpaceFloatGame;
import me.fahien.spacefloat.mission.Mission;

import static me.fahien.spacefloat.component.ComponentMapperEnumerator.collisionMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.destinationMapper;

/**
 * Mission {@link CollisionComponent}
 *
 * @author Fahien
 */
public class MissionComponent extends CollisionComponent {
	private static final float PARCEL_RADIUS = 80f;
	private static final short PARCEL_GROUP = 8; // Event
	private static final short PARCEL_MASK = 1|4; // Player + Planet
	private static final float HANDLING_TIME = 0.003f; // 3 seconds

	private static float handlingTime = HANDLING_TIME;

	private Mission mission;

	public MissionComponent() {
		super(PARCEL_RADIUS, PARCEL_GROUP, PARCEL_MASK);
	}

	/**
	 * Sets the {@link Mission}
	 */
	public void setMission(Mission mission) {
		this.mission = mission;
	}

	private GameObject m_player;

	@Override
	public void collideWith(float delta, final btManifoldPoint collisionPoint, final GameObject source, final GameObject target) {
		// If is not collected and collide with Player
		if (!mission.isCollected() && target.isPlayer()) {
			// Decrease hangling time
			handlingTime -= delta;

			if (handlingTime <= 0f) {
				m_player = target;
				// Reset handling time
				handlingTime = HANDLING_TIME;
				// Set collected
				mission.setCollected(true);
				// Remove this component from the parcel source
				source.remove(MissionComponent.class);

				SpaceFloatGame game = SpaceFloat.GAME.getGame();
				Engine engine = game.getEngine();
				// Remove the parcel from the engine
				engine.removeEntity(source);
				// Update player destination component
				for (Entity entity : engine.getEntities()) {
					if (((GameObject) entity).getName().equals(mission.getDestination())) {
						Vector3 position = new Vector3();
						collisionMapper.get(entity).getPosition(position);
						DestinationComponent destinationComponent = destinationMapper.get(target);
						destinationComponent.setPosition(position);
						destinationComponent.setName(mission.getDestination());
					}
				}
				// Update user data
				userData = target;
				// Add this component to the player
				target.add(this);
				// Enqueue first message
				game.enqueueMessage(mission.getMessageInitial());
			}
		} else if (!mission.isDelivered() && mission.getDestination().equals(target.getName())) {
			// Update handling time
			handlingTime -= delta;

			if (handlingTime <= 0f) {
				// Reset handling time
				handlingTime = HANDLING_TIME;
				// Set delivered
				mission.setDelivered(true);
				// Remove this component from the player
				m_player.remove(MissionComponent.class);
				DestinationComponent destinationComponent = destinationMapper.get(m_player);
				destinationComponent.setName(null);
				// Enqueue last message
				SpaceFloatGame game = SpaceFloat.GAME.getGame();
				game.enqueueMessage(mission.getMessageEnding());
				// Load next mission
				MissionFactory.INSTANCE.loadNextMission();
			}
		}
	}

	@Override
	protected void setCollisionFlags() {
		setCollisionFlags(CollisionFlags.CF_NO_CONTACT_RESPONSE);
	}
}
