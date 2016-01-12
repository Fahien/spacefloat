package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btManifoldPoint;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import me.fahien.spacefloat.entity.GameObject;
import me.fahien.spacefloat.game.SpaceFloat;
import me.fahien.spacefloat.game.SpaceFloatGame;
import me.fahien.spacefloat.utils.JsonKey;

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

	/**
	 * The {@link GameObject} name of destination
	 */
	private String destination;

	private String messageInitial;
	private String messageEnding;

	private boolean collected;
	private boolean delivered;
	private float handlingTime = HANDLING_TIME;

	public MissionComponent() {
		super(PARCEL_RADIUS, PARCEL_GROUP, PARCEL_MASK);
	}

	public MissionComponent(String destination) {
		this();
		this.destination = destination;
	}

	/**
	 * Returns the {@link GameObject} name of destination
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * Sets the initial message
	 */
	public void setMessageInitial(String messageInitial) {
		this.messageInitial = messageInitial;
	}

	/**
	 * Sets the ending message
	 */
	public void setMessageEnding(String messageEnding) {
		this.messageEnding = messageEnding;
	}

	private GameObject m_player;

	@Override
	public void collideWith(float delta, final btManifoldPoint collisionPoint, final GameObject source, final GameObject target) {
		// If is not collected and collide with Player
		if (!collected && target.isPlayer()) {
			// Decrease hangling time
			handlingTime -= delta;

			if (handlingTime <= 0f) {
				m_player = target;
				// Reset handling time
				handlingTime = HANDLING_TIME;
				// Set collected
				collected = true;
				// Remove this component from the parcel source
				source.remove(MissionComponent.class);

				SpaceFloatGame game = SpaceFloat.GAME.getGame();
				Engine engine = game.getEngine();
				// Remove the parcel from the engine
				engine.removeEntity(source);
				// Update player destination component
				for (Entity entity : engine.getEntities()) {
					if (((GameObject) entity).getName().equals(destination)) {
						Vector3 position = new Vector3();
						collisionMapper.get(entity).getPosition(position);
						DestinationComponent destinationComponent = destinationMapper.get(target);
						destinationComponent.setPosition(position);
						destinationComponent.setName(destination);
					}
				}
				// Add this component to the player
				target.add(this);
				// Enqueue first message
				game.enqueueMessage(messageInitial);
			}
		} else if (!delivered && destination.equals(target.getName())) {
			// Update handling time
			handlingTime -= delta;

			if (handlingTime <= 0f) {
				// Reset handling time
				handlingTime = HANDLING_TIME;
				// Set delivered
				delivered = true;
				// Remove this component from the player
				m_player.remove(MissionComponent.class);
				DestinationComponent destinationComponent = destinationMapper.get(m_player);
				destinationComponent.setName(null);
				// Enqueue last message
				SpaceFloatGame game = SpaceFloat.GAME.getGame();
				game.enqueueMessage(messageEnding);
			}
		}
	}

	@Override
	protected void setCollisionFlags() {
		setCollisionFlags(CollisionFlags.CF_NO_CONTACT_RESPONSE);
	}

	@Override
	public void write(Json json) {
		json.writeValue(JsonKey.DESTINATION, destination);
		json.writeValue(JsonKey.COLLECTED, collected);
		json.writeValue(JsonKey.DELIVERED, delivered);
		json.writeValue(JsonKey.MESSAGE_INITIAL, messageInitial);
		json.writeValue(JsonKey.MESSAGE_ENDING, messageEnding);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		destination = jsonData.getString(JsonKey.DESTINATION);
		collected = jsonData.getBoolean(JsonKey.COLLECTED);
		delivered = jsonData.getBoolean(JsonKey.DELIVERED);
		messageInitial = jsonData.getString(JsonKey.MESSAGE_INITIAL);
		messageEnding = jsonData.getString(JsonKey.MESSAGE_ENDING);
	}
}
