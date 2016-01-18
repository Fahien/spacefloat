package me.fahien.spacefloat.mission;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import me.fahien.spacefloat.entity.GameObject;
import me.fahien.spacefloat.utils.JsonKey;

/**
 * The Mission
 *
 * @author Fahien
 */
public class Mission implements Json.Serializable, Comparable<Mission> {

	private String name;

	private Vector3 position;
	private String destination;

	private String messageInitial;
	private String messageEnding;

	private boolean collected;
	private boolean delivered;

	private int reward;

	public Mission() {
		position = new Vector3();
	}

	/**
	 * Returns the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Returns the initial position
	 */
	public Vector3 getPosition() {
		return position;
	}

	/**
	 * Sets the initial position
	 */
	public void setPosition(final Vector3 position) {
		this.position = position;
	}

	/**
	 * Returns the destination {@link GameObject} name
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * Sets the destination {@link GameObject} name
	 */
	public void setDestination(final String destination) {
		this.destination = destination;
	}

	/**
	 * Returns the initial message
	 */
	public String getMessageInitial() {
		return messageInitial;
	}

	/**
	 * Sets the initial message
	 */
	public void setMessageInitial(final String messageInitial) {
		this.messageInitial = messageInitial;
	}

	/**
	 * Returns the ending message
	 */
	public String getMessageEnding() {
		return messageEnding;
	}

	/**
	 * Sets the engine message
	 */
	public void setMessageEnding(final String messageEnding) {
		this.messageEnding = messageEnding;
	}

	/**
	 * Tests whether is collected
	 */
	public boolean isCollected() {
		return collected;
	}

	/**
	 * Sets collected
	 */
	public void setCollected(final boolean collected) {
		this.collected = collected;
	}

	/**
	 * Tests whether is delivered
	 */
	public boolean isDelivered() {
		return delivered;
	}

	/**
	 * Sets delivered
	 */
	public void setDelivered(final boolean delivered) {
		this.delivered = delivered;
	}

	/**
	 * Returns the reward
	 */
	public int getReward() {
		return reward;
	}

	/**
	 * Sets the reward
	 */
	public void setReward(final int reward) {
		if (reward >= 0) {
			this.reward = reward;
		}
	}

	/**
	 * Adds reward
	 */
	public void addReward(int reward) {
		reward += this.reward;
		this.reward = (reward >= 0) ? reward : 0;
	}

	@Override
	public void write(Json json) {
		json.writeValue(JsonKey.NAME, name);
		json.writeValue(JsonKey.X, position.x);
		json.writeValue(JsonKey.Y, position.y);
		json.writeValue(JsonKey.Z, position.z);
		json.writeValue(JsonKey.DESTINATION, destination);
		json.writeValue(JsonKey.COLLECTED, collected);
		json.writeValue(JsonKey.DELIVERED, delivered);
		json.writeValue(JsonKey.MESSAGE_INITIAL, messageInitial);
		json.writeValue(JsonKey.MESSAGE_ENDING, messageEnding);
		json.writeValue(JsonKey.REWARD, reward);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		name = jsonData.getString(JsonKey.NAME);
		position.x = jsonData.getFloat(JsonKey.X);
		position.y = jsonData.getFloat(JsonKey.Y);
		position.z = jsonData.getFloat(JsonKey.Z);
		destination = jsonData.getString(JsonKey.DESTINATION);
		collected = jsonData.getBoolean(JsonKey.COLLECTED);
		delivered = jsonData.getBoolean(JsonKey.DELIVERED);
		messageInitial = jsonData.getString(JsonKey.MESSAGE_INITIAL);
		messageEnding = jsonData.getString(JsonKey.MESSAGE_ENDING);
		setReward(jsonData.getInt(JsonKey.REWARD));
	}

	@Override
	public int compareTo(Mission mission) {
		return name.compareTo(mission.getName());
	}
}