package me.fahien.spacefloat.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;

/**
 * The 2D {@link SpaceshipController}
 *
 * @author Fahien
 */
public class SpaceshipController2D extends SpaceshipController {

	@Override
	protected InputProcessor createInputProcessor(final Vector3 acceleration, final Vector3 eulerAnglesAcceleration) {
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
		public boolean touchDown(int screenX, int screenY, int pointer, int button) {
			acceleration.x = screenX - Gdx.graphics.getWidth() / 2;
			acceleration.z = screenY - Gdx.graphics.getHeight() / 2;
			acceleration.nor();
			acceleration.z *= VELOCITY;
			acceleration.x *= VELOCITY;
			return true;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			acceleration.x = screenX - Gdx.graphics.getWidth() / 2;
			acceleration.z = screenY - Gdx.graphics.getHeight() / 2;
			acceleration.nor();
			acceleration.z *= VELOCITY;
			acceleration.x *= VELOCITY;
			return true;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			acceleration.x = 0;
			acceleration.z = 0;
			return true;
		}

		@Override
		public boolean keyDown(int keycode) {
			switch(keycode) {
				case Input.Keys.W:
					acceleration.z = NEGATIVE * VELOCITY;
					return true;
				case Input.Keys.A:
					eulerAnglesAcceleration.x = POSITIVE;
					return true;
				case Input.Keys.D:
					eulerAnglesAcceleration.x = NEGATIVE;
					return true;
				default:
					return false;
			}
		}

		@Override
		public boolean keyUp(int keycode) {
			switch (keycode) {
				case Input.Keys.W:
					acceleration.z = 0;
					return true;
				case Input.Keys.A:
					eulerAnglesAcceleration.x = 0;
					return true;
				case Input.Keys.D:
					eulerAnglesAcceleration.x = 0;
					return true;
				default:
					return false;
			}
		}
	}
}
