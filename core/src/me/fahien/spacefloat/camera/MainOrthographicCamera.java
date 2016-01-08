package me.fahien.spacefloat.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

import me.fahien.spacefloat.controller.CameraController;

/**
 * Main {@link OrthographicCamera}
 *
 * @author Fahien
 */
public class MainOrthographicCamera extends OrthographicCamera {
	private static final float NEAR = 1f;
	private static final float FAR = 2048f * CameraController.CAMERA_ZOOM;

	public MainOrthographicCamera() {
		super(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		near = NEAR;
		far = FAR;
		zoom = CameraController.CAMERA_ZOOM;
	}
}
