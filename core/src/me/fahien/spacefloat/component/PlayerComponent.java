package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import me.fahien.spacefloat.utils.JsonKey;

/**
 * The Player {@link Component}
 *
 * @author Fahien
 */
public class PlayerComponent implements Component, Json.Serializable {

	private int money;

	public PlayerComponent() {}

	/**
	 * Returns the money
	 */
	public int getMoney() {
		return money;
	}

	/**
	 * Sets the money
	 */
	public void setMoney(int money) {
		this.money = money;
	}

	@Override
	public void write(Json json) {
		json.writeValue(JsonKey.MONEY, money);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		money = jsonData.getInt(JsonKey.MONEY);
	}
}
