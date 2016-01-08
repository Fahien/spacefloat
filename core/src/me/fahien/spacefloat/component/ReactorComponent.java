package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import me.fahien.spacefloat.utils.JsonKey;

/**
 * The Reactor {@link Component}
 *
 * @author Fahien
 */
public class ReactorComponent implements Component, Json.Serializable {
	public static final String PARTICLES_DIR = "particles/";
	public static float DEFAULT_CONSUME = 64f;
	public static float DEFAULT_POWER = 64f;

	private String name;
	private ParticleEffect reactor;
	private ParticleEffect effect;

	private boolean burning;
	private float consume;
	private float power;

	public ReactorComponent() {
		this.consume = DEFAULT_CONSUME;
		this.power = DEFAULT_POWER;
	}

	public ReactorComponent(String name) {
		this();
		this.name = name;
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
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the reactor {@link ParticleEffect}
	 */
	public ParticleEffect getReactor() {
		return reactor;
	}

	/**
	 * Sets the reactor {@link ParticleEffect}
	 */
	public void setReactor(ParticleEffect reactor) {
		this.reactor = reactor;
	}

	/**
	 * Returns the consume
	 */
	public float getConsume() {
		return consume;
	}

	/**
	 * Returns the power
	 */
	public float getPower() {
		return power;
	}

	/**
	 * Tests whether is burning
	 */
	public boolean isBurning() {
		return burning;
	}

	/**
	 * Starts or updates the reactor {@link ParticleEffect}
	 */
	public void start(ParticleSystem particleSystem, Matrix4 transform) {
		if (isBurning()) return;
		effect = reactor.copy();
		effect.setTransform(transform);
		effect.init();
		effect.start();  // optional: particle will begin burning immediately
		particleSystem.add(effect);
		burning = true;
	}

	/**
	 * Stops the reactor {@link ParticleEffect}
	 */
	public void stop(ParticleSystem particleSystem) {
		if (!isBurning()) return;
		particleSystem.remove(effect);
		burning = false;
	}

	/**
	 * Updates the reactor {@link ParticleEffect} transform
	 */
	public void setTransform(Matrix4 transform) {
		effect.setTransform(transform);
	}

	@Override
	public void write(Json json) {
		json.writeValue(JsonKey.NAME, name);
		json.writeValue(JsonKey.CONSUME, consume);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		name = jsonData.getString(JsonKey.NAME);
		consume = jsonData.getFloat(JsonKey.CONSUME);
	}
}
