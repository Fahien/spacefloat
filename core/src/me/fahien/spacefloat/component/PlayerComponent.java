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
	private static final float FUEL_MAX_DEFAULT = 19.0f;

	private float fuelMax;
	private Float fuel;
	private int money;

	public PlayerComponent() {
		fuelMax = FUEL_MAX_DEFAULT;
		fuel = FUEL_MAX_DEFAULT;
	}

	/**
	 * Returns the fuelMax
	 */
	public float getFuelMax() {
		return fuelMax;
	}

	/**
	 * Sets the fuelMax
	 */
	public void setFuelMax(float fuelMax) {
		this.fuelMax = fuelMax;
	}

	/**
	 * Returns the fuel
	 */
	public Float getFuel() {
		return fuel;
	}

	/**
	 * Sets the fuel
	 */
	public void setFuel(float fuel) {
		if (fuel > fuelMax) {
			fuel = (int)fuelMax;
		}
		if (fuel < 0) {
			fuel = 0;
		}
		this.fuel = fuel;
	}

	/**
	 * Adds fuel
	 */
	public void addFuel(float fuel){
		fuel += this.fuel;
		setFuel(fuel);
	}

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
		json.writeValue(JsonKey.FUELMAX, fuelMax);
		json.writeValue(JsonKey.FUEL, fuel);
		json.writeValue(JsonKey.MONEY, money);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		fuelMax = jsonData.getInt(JsonKey.FUELMAX);
		fuel = jsonData.getFloat(JsonKey.FUEL);
		money = jsonData.getInt(JsonKey.MONEY);
	}
}
