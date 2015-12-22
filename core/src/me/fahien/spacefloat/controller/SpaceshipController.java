package me.fahien.spacefloat.controller;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;

import me.fahien.spacefloat.component.AccelerationComponent;
import me.fahien.spacefloat.system.PlayerSystem;

import static com.badlogic.ashley.core.ComponentMapper.getFor;
import static me.fahien.spacefloat.game.SpaceFloatGame.logger;

/**
 * The Spaceship Controller Base Class
 *
 * @author Fahien
 */
public abstract class SpaceshipController extends PlayerSystem {

	private ComponentMapper<AccelerationComponent> am = getFor(AccelerationComponent.class);

	@Override
	public void addedToEngine(Engine engine, Entity player, InputMultiplexer inputMultiplexer) {
		if (player != null) {
			AccelerationComponent acceleration = am.get(player);
			if (acceleration != null) {
				Vector3 acceleration1 = acceleration.getAcceleration();
				Vector3 eulerAnglesAcceleration = acceleration.getEulerAnglesAcceleration();
				InputProcessor inputProcessor = createInputProcessor(acceleration1, eulerAnglesAcceleration);
				inputMultiplexer.addProcessor(inputProcessor);
			} else {
				logger.error("Error adding " + SpaceshipController.class.getSimpleName() +
						" to the engine: The player has no acceleration component");
			}
		}
	}

	/**
	 * Creates and returns the {@link InputProcessor}
	 */
	protected abstract InputProcessor createInputProcessor(final Vector3 acceleration, final Vector3 eulerAnglesAcceleration);
}
