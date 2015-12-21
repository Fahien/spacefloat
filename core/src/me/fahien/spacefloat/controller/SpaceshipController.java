package me.fahien.spacefloat.controller;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.math.Vector3;

import me.fahien.spacefloat.component.AccelerationComponent;
import me.fahien.spacefloat.system.PlayerSystem;

import static com.badlogic.ashley.core.ComponentMapper.getFor;
import static me.fahien.spacefloat.game.SpaceFloatGame.logger;

/**
 * The Spaceship Controller
 *
 * @author Fahien
 */
public class SpaceshipController extends PlayerSystem {

	private ComponentMapper<AccelerationComponent> am = getFor(AccelerationComponent.class);

	private Vector3 playerAcceleration;
	private Vector3 playerEulerAnglesAcceleration;
	private InputAdapter inputAdapter;

	public SpaceshipController() {
		super();
		inputAdapter = new SpaceshipInputAdapter();
	}

	@Override
	public void addedToEngine(Engine engine, Entity player, InputMultiplexer inputMultiplexer) {
		if (player != null) {
			AccelerationComponent acceleration = am.get(player);
			if (acceleration != null) {
				playerAcceleration = acceleration.getAcceleration();
				playerEulerAnglesAcceleration = acceleration.getEulerAnglesAcceleration();
				inputMultiplexer.addProcessor(inputAdapter);
			} else {
				logger.error("Error adding " + SpaceshipController.class.getSimpleName() +
						" to the engine: The player has no acceleration component");
			}
		}
	}

	/**
	 * The Spaceship {@link InputAdapter}
	 *
	 * @author Fahien
	 */
	private class SpaceshipInputAdapter extends InputAdapter {
		private static final float VELOCITY = 32f;
		private static final float NEGATIVE = -1;
		private static final float POSITIVE = -NEGATIVE;
		@Override
		public boolean keyDown(int keycode) {
			switch(keycode) {
				case Input.Keys.SPACE:
					playerAcceleration.z = NEGATIVE * VELOCITY;
					return true;
				case Input.Keys.A:
					playerEulerAnglesAcceleration.z = POSITIVE;
					return true;
				case Input.Keys.D:
					playerEulerAnglesAcceleration.z = NEGATIVE;
					return true;
				case Input.Keys.W:
					playerEulerAnglesAcceleration.y = NEGATIVE;
					return true;
				case Input.Keys.S:
					playerEulerAnglesAcceleration.y = POSITIVE;
					return true;
				default:
					return false;
			}
		}

		@Override
		public boolean keyUp(int keycode) {
			switch (keycode) {
				case Input.Keys.SPACE:
					playerAcceleration.z = 0;
					return true;
				case Input.Keys.A:
					playerEulerAnglesAcceleration.z = 0;
					return true;
				case Input.Keys.D:
					playerEulerAnglesAcceleration.z = 0;
					return true;
				case Input.Keys.W:
					playerEulerAnglesAcceleration.y = 0;
					return true;
				case Input.Keys.S:
					playerEulerAnglesAcceleration.y = 0;
					return true;
				default:
					return false;
			}
		}
	}
}
