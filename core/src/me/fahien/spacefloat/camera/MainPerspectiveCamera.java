package me.fahien.spacefloat.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;

import me.fahien.spacefloat.controller.CameraController;

/**
 * Main {@link PerspectiveCamera}
 *
 * @author Fahien
 */
public class MainPerspectiveCamera extends PerspectiveCamera {
	private static final int MAIN_FOV = 67;
	private static final float NEAR = 1f;
	private static final float FAR = 1024f + CameraController.CAMERA_ZOOM * 1024f;

	public MainPerspectiveCamera() {
		super(MAIN_FOV, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		near = NEAR;
		far = FAR;
	}
}
