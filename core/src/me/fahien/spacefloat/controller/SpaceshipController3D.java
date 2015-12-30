package me.fahien.spacefloat.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;

import me.fahien.spacefloat.component.PlayerComponent;

/**
 * The 3D {@link SpaceshipController}
 *
 * @author Fahien
 */
public class SpaceshipController3D extends SpaceshipController {

	@Override
	protected InputProcessor createInputProcessor(PlayerComponent player, final Vector3 acceleration,final Vector3 eulerAnglesAcceleration) {
		return new SpaceshipInputAdapter(player, acceleration, eulerAnglesAcceleration);
	}

	/**
	 * The Spaceship {@link InputAdapter}
	 *
	 * @author Fahien
	 */
	private class SpaceshipInputAdapter extends InputAdapter {
		private static final float VELOCITY = 64f;
		private static final float NEGATIVE = -1;
		private static final float POSITIVE = -NEGATIVE;

		private PlayerComponent player;
		private Vector3 acceleration;
		private Vector3 eulerAnglesAcceleration;

		public SpaceshipInputAdapter(PlayerComponent player, Vector3 acceleration, Vector3 eulerAnglesAcceleration) {
			this.player = player;
			this.acceleration = acceleration;
			this.eulerAnglesAcceleration = eulerAnglesAcceleration;
		}

		@Override
		public boolean keyDown(int keycode) {
			switch(keycode) {
				case Input.Keys.SPACE:
					if (player.getFuel() <= 0.0f) return false;
					acceleration.z = NEGATIVE * VELOCITY;
					return true;
				case Input.Keys.A:
					eulerAnglesAcceleration.z = POSITIVE;
					return true;
				case Input.Keys.D:
					eulerAnglesAcceleration.z = NEGATIVE;
					return true;
				case Input.Keys.W:
					eulerAnglesAcceleration.y = NEGATIVE;
					return true;
				case Input.Keys.S:
					eulerAnglesAcceleration.y = POSITIVE;
					return true;
				default:
					return false;
			}
		}

		@Override
		public boolean keyUp(int keycode) {
			switch (keycode) {
				case Input.Keys.SPACE:
					acceleration.z = 0;
					return true;
				case Input.Keys.A:
					eulerAnglesAcceleration.z = 0;
					return true;
				case Input.Keys.D:
					eulerAnglesAcceleration.z = 0;
					return true;
				case Input.Keys.W:
					eulerAnglesAcceleration.y = 0;
					return true;
				case Input.Keys.S:
					eulerAnglesAcceleration.y = 0;
					return true;
				default:
					return false;
			}
		}
	}
}
