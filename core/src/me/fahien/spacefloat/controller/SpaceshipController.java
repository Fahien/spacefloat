package me.fahien.spacefloat.controller;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;

import me.fahien.spacefloat.component.AccelerationComponent;
import me.fahien.spacefloat.component.EnergyComponent;
import me.fahien.spacefloat.component.PlayerComponent;
import me.fahien.spacefloat.component.TransformComponent;
import me.fahien.spacefloat.system.PlayerSystem;

import static com.badlogic.ashley.core.ComponentMapper.getFor;
import static me.fahien.spacefloat.game.SpaceFloatGame.logger;

/**
 * The Spaceship Controller Base Class
 *
 * @author Fahien
 */
public abstract class SpaceshipController extends PlayerSystem {
	public static float REACTOR_CONSUMES = 0.5f;

	private ComponentMapper<AccelerationComponent> am = getFor(AccelerationComponent.class);
	private ComponentMapper<TransformComponent> tm = getFor(TransformComponent.class);
	private ComponentMapper<PlayerComponent> pm = getFor(PlayerComponent.class);
	private ComponentMapper<EnergyComponent> em = getFor(EnergyComponent.class);

	private EnergyComponent energyComponent;
	private Vector3 acceleration;

	/**
	 * Creates and returns the {@link InputProcessor}
	 */
	protected abstract InputProcessor createInputProcessor(PlayerComponent player, final Vector3 acceleration, final Vector3 eulerAnglesAcceleration);

	@Override
	public void addedToEngine(Engine engine, Entity player, InputMultiplexer inputMultiplexer) {
		if (player != null) {
			energyComponent = em.get(player);
			AccelerationComponent accelerationComponent = am.get(player);
			TransformComponent transform = tm.get(player);
			if (accelerationComponent != null && transform != null) {
				acceleration = accelerationComponent.getAcceleration();
				Vector3 eulerAngles = transform.getRotation();
				InputProcessor inputProcessor = createInputProcessor(pm.get(player), acceleration, eulerAngles);
				inputMultiplexer.addProcessor(inputProcessor);
			} else {
				logger.error("Error adding " + SpaceshipController.class.getSimpleName() +
						" to the engine: The player has no acceleration component");
			}
		}
	}

	@Override
	public void update(float deltaTime) {
		if (energyComponent.getCharge() <= 0.0f) {
			acceleration.set(Vector3.Zero);
			return;
		}
		if (!acceleration.equals(Vector3.Zero)) {
			energyComponent.addCharge(-REACTOR_CONSUMES * deltaTime);
		}
	}
}
