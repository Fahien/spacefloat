package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import me.fahien.spacefloat.game.SpaceFloatGame;
import me.fahien.spacefloat.utils.JsonString;

/**
 * The Energy {@link Component}
 *
 * @author Fahien
 */
public class EnergyComponent implements Component, Json.Serializable {
	protected static final float CHARGE_MAX_DEFAULT = 32f;
	protected static final float CHARGE_MAX_LOWER_LIMIT = 1f;
	protected static final float CHARGE_MIN = 0f;
	public static float SHIELD_CONSUME = 0.0005f;

	private float charge;
	private float chargeMax;

	public EnergyComponent() {
		this(CHARGE_MAX_DEFAULT);
	}

	public EnergyComponent(float chargeMax) {
		this.chargeMax = chargeMax;
		charge = chargeMax;
	}

	/**
	 * Returns the charge
	 */
	public float getCharge() {
		return charge;
	}

	/**
	 * Sets the charge
	 */
	public void setCharge(float charge) {
		if (charge < CHARGE_MIN) {
			this.charge = CHARGE_MIN;
		} else if (charge > chargeMax) {
			this.charge = chargeMax;
		} else {
			this.charge = charge;
		}
	}

	/**
	 * Adds charge
	 */
	public void addCharge(float charge) {
		charge += this.charge;
		setCharge(charge);
	}

	/**
	 * Absorb an hurt according to velocity and collision normal
	 */
	public void hurt(Vector3 velocity, Vector3 normal) {
		normal.nor();
		float dot = 2 * velocity.dot(normal);
		normal.scl(dot);
		float charge = -normal.len2() * SHIELD_CONSUME;
		if (charge > -2.0f) return;
		SpaceFloatGame.logger.info("Shield: " + charge);
		addCharge(charge);
	}

	/**
	 * Returns the charge upper limit
	 */
	public float getChargeMax() {
		return chargeMax;
	}

	/**
	 * Sets the charge upper limit
	 */
	public void setChargeMax(float chargeMax) {
		this.chargeMax = (chargeMax < CHARGE_MAX_LOWER_LIMIT) ? CHARGE_MAX_LOWER_LIMIT : chargeMax;
	}

	@Override
	public void write(Json json) {
		json.writeValue(JsonString.CHARGEMAX, chargeMax);
		json.writeValue(JsonString.CHARGE, charge);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		chargeMax = jsonData.getInt(JsonString.CHARGEMAX);
		charge = jsonData.getFloat(JsonString.CHARGE);
	}
}
