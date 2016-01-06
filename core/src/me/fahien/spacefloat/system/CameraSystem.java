package me.fahien.spacefloat.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

import static com.badlogic.gdx.math.MathUtils.clamp;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.transformMapper;
import static me.fahien.spacefloat.game.SpaceFloatGame.logger;

/**
 * The Camera {@link PlayerSystem}
 *
 * @author Fahien
 */
public class CameraSystem extends PlayerSystem {
	private static final float CAMERA_OFFSET = 1024f;
	private static final float CAMERA_ZOOM_MIN = 1f;
	private static final float CAMERA_ZOOM_MAX = 8f;

	public static float CAMERA_ZOOM = 2f;
	public static String CAMERA_TYPE = "orthographic";

	private Vector3 playerPosition;
	private Camera camera;
	private Vector3 cameraOffset;

	public CameraSystem() {
		cameraOffset = new Vector3(0f, CAMERA_OFFSET * CAMERA_ZOOM, 0f);
	}

	/**
	 * Sets the camera zoom
	 */
	public static void setCameraZoom(float cameraZoom) {
		if (cameraZoom < CAMERA_ZOOM_MIN || cameraZoom > CAMERA_ZOOM_MAX) {
			logger.error("Camera zoom must be comprised between " + CAMERA_ZOOM_MIN + " and " + CAMERA_ZOOM_MAX);
		}
		CAMERA_ZOOM = clamp(cameraZoom, CAMERA_ZOOM_MIN, CAMERA_ZOOM_MAX);
	}

	/**
	 * Sets the {@link Camera}
	 */
	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	@Override
	public void addedToEngine(Engine engine, Entity player, InputMultiplexer inputMultiplexer) {
		// Get the player position
		if (player != null) {
			playerPosition = transformMapper.get(player).getPosition();
			camera.position.set(playerPosition);
			camera.position.add(cameraOffset);
			camera.lookAt(playerPosition);
		}
		camera.update();
	}

	@Override
	public void update(float deltaTime) {
		// Follow the player
		camera.position.set(playerPosition);
		camera.position.add(cameraOffset);
		camera.update();
	}
}
