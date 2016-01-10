package me.fahien.spacefloat.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

import me.fahien.spacefloat.system.CameraSystem;

/**
 * Main {@link OrthographicCamera}
 *
 * @author Fahien
 */
public class MainOrthographicCamera extends OrthographicCamera {
	private static final float NEAR = 1f;
	private static final float FAR = 2048f * CameraSystem.CAMERA_ZOOM;

	public MainOrthographicCamera() {
		super(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		near = NEAR;
		far = FAR;
		zoom = CameraSystem.CAMERA_ZOOM;
	}
}
