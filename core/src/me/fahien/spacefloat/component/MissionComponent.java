package me.fahien.spacefloat.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.bullet.collision.btManifoldPoint;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import me.fahien.spacefloat.actor.ControlMessageActor;
import me.fahien.spacefloat.actor.HudFactory;
import me.fahien.spacefloat.entity.GameObject;
import me.fahien.spacefloat.game.SpaceFloat;
import me.fahien.spacefloat.game.SpaceFloatGame;
import me.fahien.spacefloat.utils.JsonKey;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.removeActor;
import static me.fahien.spacefloat.game.SpaceFloatGame.logger;

/**
 * Mission {@link CollisionComponent}
 *
 * @author Fahien
 */
public class MissionComponent extends CollisionComponent {
	private static final float PARCEL_RADIUS = 90f;
	private static final short PARCEL_GROUP = 8; // Event
	private static final short PARCEL_MASK = 1|4; // Player + Planet
	private static final float HANDLING_TIME = 3f; // 3 seconds

	/**
	 * The {@link GameObject} name of destination
	 */
	private String destination;

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

	@Override
	public void collideWith(final btManifoldPoint collisionPoint, final GameObject source, final GameObject target) {

		// If is not collected and collide with Player
		if (!collected && target.isPlayer()) {

			handlingTime -= Gdx.graphics.getDeltaTime();
			if (handlingTime <= 0f) {
				handlingTime = HANDLING_TIME;
				collected = true;
				source.remove(MissionComponent.class);
				SpaceFloat.GAME.getGame().getEngine().removeEntity(source);
				target.add(this);
				logger.debug("Parcel collected");
				logger.debug("Creating message");
				SpaceFloatGame game = SpaceFloat.GAME.getGame();
				Stage stage = game.getStage();
				HudFactory factory = game.getHudFactory();
				ControlMessageActor controlMessageActor = factory.getMessageActor("Hello! This is Mission Control! The parcel you have picked up has to be delivered to the Earth!");
				controlMessageActor.addListener(new InputListener() {
					@Override
					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						event.getTarget().addAction(removeActor());
						logger.debug("Removing actor");
						return true;
					}
				});
				stage.addActor(controlMessageActor);
			}
		} else if (!delivered && destination.equals(target.getName())) {

			handlingTime -= Gdx.graphics.getDeltaTime();
			if (handlingTime <= 0f) {
				handlingTime = HANDLING_TIME;
				delivered = true;
				source.remove(MissionComponent.class);
				logger.debug("Parcel delivered");logger.debug("Creating message");
				SpaceFloatGame game = SpaceFloat.GAME.getGame();
				Stage stage = game.getStage();
				HudFactory factory = game.getHudFactory();
				ControlMessageActor controlMessageActor = factory.getMessageActor("Well done, space courier!");
				controlMessageActor.addListener(new InputListener() {
					@Override
					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						event.getTarget().addAction(removeActor());
						logger.debug("Removing actor");
						return true;
					}
				});
				stage.addActor(controlMessageActor);
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
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		destination = jsonData.getString(JsonKey.DESTINATION);
		collected = jsonData.getBoolean(JsonKey.COLLECTED);
		delivered = jsonData.getBoolean(JsonKey.DELIVERED);
	}
}
