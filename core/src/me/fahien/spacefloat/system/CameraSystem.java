package me.fahien.spacefloat.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

import me.fahien.spacefloat.camera.MainCamera;
import me.fahien.spacefloat.component.GraphicComponent;
import me.fahien.spacefloat.component.TransformComponent;

import static com.badlogic.ashley.core.ComponentMapper.getFor;

/**
 * The Camera {@link PlayerSystem}
 *
 * @author Fahien
 */
public class CameraSystem extends PlayerSystem {
	private static final float CAMERA_Y_OFFSET = 100.0f;
	private static final float CAMERA_Z_OFFSET = 150.0f;
	private ComponentMapper<TransformComponent> tm = getFor(TransformComponent.class);
	private ComponentMapper<GraphicComponent> gm = getFor(GraphicComponent.class);

	private Vector3 playerPosition;
	private ModelInstance instance;
	private Vector3 instancePosition;
	private MainCamera camera;
	private Vector3 cameraOffset;

	private CameraController cameraController;

	public CameraSystem(MainCamera camera) {
		this.camera = camera;
		instancePosition = new Vector3();
		cameraOffset = new Vector3(0, CAMERA_Y_OFFSET, CAMERA_Z_OFFSET);
		cameraController = new CameraController();
	}

	@Override
	public void addedToEngine(Engine engine, Entity player, InputMultiplexer inputMultiplexer) {
		// Get the player position
		if (player != null) {
			playerPosition = tm.get(player).getPosition();
			GraphicComponent graphic = gm.get(player);
			instance = graphic.getInstance();
			camera.transform(instance.transform);
			camera.translate(0, CAMERA_Y_OFFSET, CAMERA_Z_OFFSET);
			camera.lookAt(instancePosition);
		}
		camera.update();
		inputMultiplexer.addProcessor(cameraController);
	}

	@Override
	public void update(float deltaTime) {
		// Follow the player
		instance.transform.getTranslation(instancePosition);
		camera.position.set(instancePosition);
		camera.position.add(cameraOffset);

		camera.update();
	}

	private class CameraController extends InputAdapter {

		private final Vector3 tmpV1 = new Vector3();
		/** The angle to rotate when moved the full width or height of the screen. */
		public float rotateAngle = 360f;
		private float startX;
		private float startY;
		
		protected boolean process(float deltaX, float deltaY) {
			tmpV1.set(camera.direction).crs(camera.up).y = 0f;
			camera.rotateAround(playerPosition, tmpV1.nor(), deltaY * rotateAngle);
			camera.rotateAround(playerPosition, Vector3.Y, deltaX * -rotateAngle);
			return true;
		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer, int button) {
			startX = screenX;
			startY = screenY;
			return true;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			final float deltaX = (screenX - startX) / Gdx.graphics.getWidth();
			final float deltaY = (startY - screenY) / Gdx.graphics.getHeight();
			startX = screenX;
			startY = screenY;
			return process(deltaX, deltaY);
		}
	}
}
