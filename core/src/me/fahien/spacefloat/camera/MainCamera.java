package me.fahien.spacefloat.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;

/**
 * Main {@link PerspectiveCamera}
 *
 * @author Fahien
 */
public class MainCamera extends PerspectiveCamera {
	private static final int MAIN_FOV = 67;
	private static final float POSITION_X = 50f;
	private static final float POSITION_Y = 50f;
	private static final float POSITION_Z = 50f;
	private static final float NEAR = 1f;
	private static final float FAR = 300f;

	public MainCamera() {
		super(MAIN_FOV, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		position.set(POSITION_X, POSITION_Y, POSITION_Z);
		lookAt(0, 0, 0);
		near = NEAR;
		far = FAR;
	}
}
