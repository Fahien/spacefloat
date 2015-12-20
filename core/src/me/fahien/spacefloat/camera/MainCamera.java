package me.fahien.spacefloat.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;

/**
 * Main {@link PerspectiveCamera}
 *
 * @author Fahien
 */
public class MainCamera extends PerspectiveCamera {
	private static final int MAIN_FOV = 67;
	private static final float NEAR = 1f;
	private static final float FAR = 1000f;

	public MainCamera() {
		super(MAIN_FOV, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		near = NEAR;
		far = FAR;
	}
}
