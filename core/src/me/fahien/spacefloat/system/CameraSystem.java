package me.fahien.spacefloat.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.math.Vector3;

import me.fahien.spacefloat.camera.MainCamera;
import me.fahien.spacefloat.component.TransformComponent;

import static com.badlogic.ashley.core.ComponentMapper.getFor;

/**
 * The Camera {@link PlayerSystem}
 *
 * @author Fahien
 */
public class CameraSystem extends PlayerSystem {
	private static final float CAMERA_Y_OFFSET = 1024.f;
	private ComponentMapper<TransformComponent> tm = getFor(TransformComponent.class);

	private Vector3 playerPosition;
	private MainCamera camera;
	private Vector3 cameraOffset;

	public CameraSystem() {
		cameraOffset = new Vector3(0, CAMERA_Y_OFFSET, 0);
	}

	/**
	 * Sets the {@link MainCamera}
	 */
	public void setCamera(MainCamera camera) {
		this.camera = camera;
	}

	@Override
	public void addedToEngine(Engine engine, Entity player, InputMultiplexer inputMultiplexer) {
		// Get the player position
		if (player != null) {
			playerPosition = tm.get(player).getPosition();
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
