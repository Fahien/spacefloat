package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import me.fahien.spacefloat.utils.JsonKey;

/**
 * The Energy {@link Component}
 *
 * @author Fahien
 */
public class EnergyComponent extends ParticleComponent {
	protected static final float CHARGE_MAX_DEFAULT = 128.0f;
	protected static final float CHARGE_MAX_LOWER_LIMIT = 1.0f;
	protected static final float CHARGE_MIN = 0.0f;
	public static float ENERGY_RECHARGE = 1024.0f;
	public static float SHIELD_CONSUME = 32768.0f;

	private float charge;
	private float chargeMax;

	public EnergyComponent() {
		this(CHARGE_MAX_DEFAULT);
	}

	public EnergyComponent(final float chargeMax) {
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
	public void setCharge(final float charge) {
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
	public void hurt(final float impulse) {
		float charge = -impulse / SHIELD_CONSUME;
		if (charge < -2.0f) addCharge(charge);
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
	public void setChargeMax(final float chargeMax) {
		this.chargeMax = (chargeMax < CHARGE_MAX_LOWER_LIMIT) ? CHARGE_MAX_LOWER_LIMIT : chargeMax;
	}

	/**
	 * Recharges energy
	 */
	public void recharge(final float delta) {
		addCharge(delta * ENERGY_RECHARGE);
	}

	@Override
	public void write(final Json json) {
		super.write(json);
		json.writeValue(JsonKey.CHARGEMAX, chargeMax);
		json.writeValue(JsonKey.CHARGE, charge);
	}

	@Override
	public void read(final Json json, final JsonValue jsonData) {
		super.read(json, jsonData);
		chargeMax = jsonData.getInt(JsonKey.CHARGEMAX);
		charge = jsonData.getFloat(JsonKey.CHARGE);
	}
}
