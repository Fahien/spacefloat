package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import static me.fahien.spacefloat.utils.JsonString.JSON_NAME;

/**
 * The Reactor {@link Component}
 *
 * @author Fahien
 */
public class ReactorComponent implements Component, Json.Serializable {
	public static final String PARTICLES_DIR = "particles/";

	private String name;
	private ParticleEffect reactor;
	private ParticleEffect effect;

	private boolean playing;

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
	 * Starts or updates the reactor
	 */
	public void start(ParticleSystem particleSystem, Matrix4 transform) {
		if (playing) {
			effect.setTransform(transform);
			return;
		}

		effect = reactor.copy();
		effect.setTransform(transform);
		effect.init();
		effect.start();  // optional: particle will begin playing immediately
		particleSystem.add(effect);
		playing = true;
	}

	/**
	 * Stops the reactor
	 */
	public void stop(ParticleSystem particleSystem) {
		if (!playing) return;

		particleSystem.remove(effect);
		playing = false;
	}

	@Override
	public void write(Json json) {
		json.writeValue(JSON_NAME, name);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		name = jsonData.getString(JSON_NAME);
	}
}
