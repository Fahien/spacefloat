package me.fahien.spacefloat.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;

/**
 * The 3D {@link SpaceshipController}
 *
 * @author Fahien
 */
public class SpaceshipController3D extends SpaceshipController {

	@Override
	protected InputProcessor createInputProcessor(final Vector3 acceleration,final Vector3 eulerAnglesAcceleration) {
		return new SpaceshipInputAdapter(acceleration, eulerAnglesAcceleration);
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

		private Vector3 acceleration;
		private Vector3 eulerAnglesAcceleration;

		public SpaceshipInputAdapter(Vector3 acceleration, Vector3 eulerAnglesAcceleration) {
			this.acceleration = acceleration;
			this.eulerAnglesAcceleration = eulerAnglesAcceleration;
		}

		@Override
		public boolean keyDown(int keycode) {
			switch(keycode) {
				case Input.Keys.SPACE:
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