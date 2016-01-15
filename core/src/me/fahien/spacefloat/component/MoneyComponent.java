package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import me.fahien.spacefloat.utils.JsonKey;

/**
 * The Money {@link Component}
 *
 * @author Fahien
 */
public class MoneyComponent implements Component, Json.Serializable {

	private int money;
	private int reward;

	public MoneyComponent() {}

	/**
	 * Returns the money
	 */
	public int getMoney() {
		return money;
	}

	/**
	 * Sets the money
	 */
	public void setMoney(final int money) {
		if (money >= 0) {
			this.money = money;
		}
	}

	/**
	 * Adds money
	 */
	public void addMoney(int money) {
		money += this.money;
		if (money >= 0) {
			setMoney(money);
		} else throw new GdxRuntimeException("Not enough money");
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
		this.reward = (reward >= 0 ) ? reward : 0;
	}

	@Override
	public void write(Json json) {
		json.writeValue(JsonKey.MONEY, money);
		json.writeValue(JsonKey.REWARD, reward);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		setMoney(jsonData.getInt(JsonKey.MONEY));
		setReward(jsonData.getInt(JsonKey.REWARD));
	}
}
