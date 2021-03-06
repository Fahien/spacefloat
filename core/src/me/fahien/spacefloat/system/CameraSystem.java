package me.fahien.spacefloat.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

import me.fahien.spacefloat.component.GraphicComponent;
import me.fahien.spacefloat.controller.PlayerController;

import static com.badlogic.gdx.math.MathUtils.clamp;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.graphicMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.transformMapper;
import static me.fahien.spacefloat.game.SpaceFloatGame.logger;

/**
 * The Camera {@link PlayerController}
 *
 * @author Fahien
 */
public class CameraSystem extends PlayerController {
	private static final int CAMERA_PRIORITY = 2;
	private static final float CAMERA_ZOOM_MIN = 1f;
	private static final float CAMERA_ZOOM_MAX = 8f;

	public static float CAMERA_ZOOM = 2f;
	public static final Vector3 POSITION_OFFSET = new Vector3(0f, 1024f, 0f);
	public static final Vector3 LOOKAT_OFFSET = new Vector3(0f, 0f, 0f);

	public static String CAMERA_TYPE = "orthographic";

	private GraphicComponent playerGraphic;
	private Camera camera;

	private Vector3 offset;
	private Vector3 lookAt;

	public CameraSystem() {
		this(POSITION_OFFSET, LOOKAT_OFFSET);
	}

	public CameraSystem(final Vector3 offset, final Vector3 lookAt) {
		super(CAMERA_PRIORITY);
		this.offset = offset;
		this.lookAt = lookAt;
	}

	/**
	 * Sets the camera zoom
	 */
	public static void setCameraZoom(final float cameraZoom) {
		if (cameraZoom < CAMERA_ZOOM_MIN || cameraZoom > CAMERA_ZOOM_MAX) {
			logger.error("Camera zoom must be comprised between " + CAMERA_ZOOM_MIN + " and " + CAMERA_ZOOM_MAX);
		}
		CAMERA_ZOOM = clamp(cameraZoom, CAMERA_ZOOM_MIN, CAMERA_ZOOM_MAX);
	}

	/**
	 * Sets the {@link Camera}
	 */
	public void setCamera(final Camera camera) {
		this.camera = camera;
	}

	/**
	 * Sets the camera offset
	 */
	public void setOffset(final Vector3 offset) {
		this.offset = offset;
	}

	/**
	 * Set lookAt
	 */
	public void setLookAt(final Vector3 lookAt) {
		this.lookAt = lookAt;
	}

	@Override
	public void addedToEngine(final Engine engine) {
		super.addedToEngine(engine);

		Entity player = getPlayer();
		// Get the player position
		if (player != null) {
			playerGraphic = graphicMapper.get(player);
			playerGraphic.getPosition(camera.position);
			camera.position.add(offset);
			Vector3 lookAt = new Vector3(this.lookAt).add(transformMapper.get(player).getPosition());
			camera.lookAt(lookAt);
		}
		camera.update();
	}

	@Override
	public void update(final float deltaTime) {
		// Follow the player
		playerGraphic.getPosition(camera.position);
		camera.position.add(offset);
		camera.update();
	}
}
