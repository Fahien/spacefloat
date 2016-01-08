package me.fahien.spacefloat.controller;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

import me.fahien.spacefloat.component.GraphicComponent;

import static com.badlogic.gdx.math.MathUtils.clamp;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.graphicMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.transformMapper;
import static me.fahien.spacefloat.game.SpaceFloatGame.logger;

/**
 * The Camera {@link PlayerController}
 *
 * @author Fahien
 */
public class CameraController extends PlayerController {
	private static final int CAMERA_PRIORITY = 1;
	private static final float CAMERA_OFFSET = 1024f;
	private static final float CAMERA_ZOOM_MIN = 1f;
	private static final float CAMERA_ZOOM_MAX = 8f;

	public static float CAMERA_ZOOM = 2f;
	public static String CAMERA_TYPE = "orthographic";

	private GraphicComponent playerGraphic;
	private Camera camera;
	private Vector3 cameraOffset;

	public CameraController() {
		super(CAMERA_PRIORITY);
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
	public void addedToEngine(Entity player, InputMultiplexer inputMultiplexer) {
		// Get the player position
		if (player != null) {
			playerGraphic = graphicMapper.get(player);
			playerGraphic.getPosition(camera.position);
			camera.position.add(cameraOffset);
			camera.lookAt(transformMapper.get(player).getPosition());
		}
		camera.update();
	}

	@Override
	public void update(float deltaTime) {
		// Follow the player
		playerGraphic.getPosition(camera.position);
		camera.position.add(cameraOffset);
		camera.update();
	}
}
