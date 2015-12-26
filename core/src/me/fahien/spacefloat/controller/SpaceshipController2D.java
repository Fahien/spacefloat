package me.fahien.spacefloat.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * The 2D {@link SpaceshipController}
 *
 * @author Fahien
 */
public class SpaceshipController2D extends SpaceshipController {

	@Override
	protected InputProcessor createInputProcessor(final Vector3 acceleration, final Vector3 eulerAngles) {
		return new SpaceshipInputAdapter(acceleration, eulerAngles);
	}

	/**
	 * The Spaceship {@link InputAdapter}
	 *
	 * @author Fahien
	 */
	private class SpaceshipInputAdapter extends InputAdapter {
		private static final float SEMIPI = MathUtils.PI / 2;
		private static final float VELOCITY = 32f;
		private static final float NEGATIVE = -1;
		private static final float POSITIVE = -NEGATIVE;

		private Vector2 mouse;
		private Vector3 acceleration;
		private Vector3 eulerAngles;

		public SpaceshipInputAdapter(Vector3 acceleration, Vector3 eulerAngles) {
			this.acceleration = acceleration;
			this.eulerAngles = eulerAngles;
			mouse = new Vector2();
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
			mouseMoved(screenX, screenY);
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
		public boolean mouseMoved(int screenX, int screenY) {
			mouse.x = screenX - Gdx.graphics.getWidth() / 2;
			mouse.y = screenY - Gdx.graphics.getHeight() / 2;
			eulerAngles.x = - SEMIPI - mouse.angleRad();
			return true;
		}

		@Override
		public boolean keyDown(int keycode) {
			switch(keycode) {
				case Input.Keys.W:
					acceleration.z = NEGATIVE * VELOCITY;
					return true;
				case Input.Keys.S:
					acceleration.z = POSITIVE * VELOCITY;
					return true;
				case Input.Keys.A:
					acceleration.x = NEGATIVE * VELOCITY;
					return true;
				case Input.Keys.D:
					acceleration.x = POSITIVE * VELOCITY;
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
				case Input.Keys.S:
					acceleration.z = 0;
					return true;
				case Input.Keys.A:
					acceleration.x = 0;
					return true;
				case Input.Keys.D:
					acceleration.x = 0;
					return true;
				default:
					return false;
			}
		}
	}
}
