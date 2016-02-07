package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import me.fahien.spacefloat.utils.JsonKey;

import static me.fahien.spacefloat.game.SpaceFloatGame.logger;

/**
 * The Particle {@link Component} Base Class
 *
 * @author Fahien
 */
public abstract class ParticleComponent implements Component, Json.Serializable{
	public static final String PARTICLES_DIR = "particles/";
	private String name;

	private ParticleEffect effect;

	private boolean emitting;

	public ParticleComponent() {}

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
	 * Returns the {@link ParticleEffect}
	 */
	public ParticleEffect getEffect() {
		return effect;
	}

	/**
	 * Sets the {@link ParticleEffect}
	 */
	public void setEffect(final ParticleEffect effect) {
		this.effect = effect.copy();
	}

	/**
	 * Tests whether is burning
	 */
	public boolean isEmitting() {
		return emitting;
	}

	public void setEmitting(final boolean emitting) {
		this.emitting = emitting;
	}

	/**
	 * Starts or updates the reactor {@link ParticleEffect}
	 */
	public void start(final ParticleSystem particleSystem, final Matrix4 transform) {
		if (isEmitting()) return;
		if (effect != null) {
			effect.setTransform(transform);
			effect.init();
			effect.start();  // optional: particle will begin immediately
			particleSystem.add(effect);
		} else {
			logger.error("Effect is null");
		}
		emitting = true;
	}

	/**
	 * Stops the reactor {@link ParticleEffect}
	 */
	public void stop(final ParticleSystem particleSystem) {
		if (!isEmitting()) return;
		if (effect != null) particleSystem.remove(effect);
		emitting = false;
	}

	/**
	 * Updates the reactor {@link ParticleEffect} transform
	 */
	public void setTransform(final Matrix4 transform) {
		if (effect != null) effect.setTransform(transform);
	}

	/**
	 * Disposes resources
	 */
	public void dispose() {
		if (effect != null) effect.dispose();
	}

	@Override
	public void write(final Json json) {
		json.writeValue(JsonKey.NAME, name);
	}

	@Override
	public void read(final Json json, final JsonValue jsonData) {
		name = jsonData.getString(JsonKey.NAME);
	}
}
