package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import me.fahien.spacefloat.game.SpaceFloatGame;
import me.fahien.spacefloat.utils.JsonKey;

/**
 * The Energy {@link Component}
 *
 * @author Fahien
 */
public class EnergyComponent implements Component, Json.Serializable {
	protected static final float CHARGE_MAX_DEFAULT = 128f;
	protected static final float CHARGE_MAX_LOWER_LIMIT = 1f;
	protected static final float CHARGE_MIN = 0f;
	public static float SHIELD_CONSUME = 32768;

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
	public void hurt(float impulse) {
		float charge = - impulse / SHIELD_CONSUME;
		if (charge > -2.0f) return;
		addCharge(charge);
	}

	/**
	 * Tests whether has charge
	 */
	public boolean hasCharge() {
		return charge > 0f;
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

	/**
	 * Recharges energy
	 */
	public void recharge() {
		addCharge(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void write(Json json) {
		json.writeValue(JsonKey.CHARGEMAX, chargeMax);
		json.writeValue(JsonKey.CHARGE, charge);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		chargeMax = jsonData.getInt(JsonKey.CHARGEMAX);
		charge = jsonData.getFloat(JsonKey.CHARGE);
	}
}
