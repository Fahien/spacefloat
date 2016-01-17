package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import me.fahien.spacefloat.utils.JsonKey;

/**
 * The Reactor {@link Component}
 *
 * @author Fahien
 */
public class ReactorComponent extends ParticleComponent {
	public static final String PARTICLES_DIR = "particles/";
	public static float DEFAULT_CONSUME = 32f;
	public static float DEFAULT_POWER = 128f;

	private float consume = DEFAULT_CONSUME;
	private float power = DEFAULT_POWER;

	public ReactorComponent() {}

	/**
	 * Returns the consume
	 */
	public float getConsume() {
		return consume;
	}

	/**
	 * Sets the consume
	 */
	public void setConsume(final float consume) {
		this.consume = consume;
	}

	/**
	 * Returns the power
	 */
	public float getPower() {
		return power;
	}

	/**
	 * Sets the power
	 */
	public void setPower(final float power) {
		this.power = power;
	}

	@Override
	public void write(final Json json) {
		super.write(json);
		json.writeValue(JsonKey.CONSUME, consume);
		json.writeValue(JsonKey.POWER, power);
	}

	@Override
	public void read(final Json json, final JsonValue jsonData) {
		super.read(json, jsonData);
		consume = jsonData.getFloat(JsonKey.CONSUME);
		power = jsonData.getFloat(JsonKey.POWER);
	}
}
