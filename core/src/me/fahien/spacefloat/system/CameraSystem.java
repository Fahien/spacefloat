package me.fahien.spacefloat.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector3;

import me.fahien.spacefloat.camera.MainCamera;
import me.fahien.spacefloat.component.PlayerComponent;
import me.fahien.spacefloat.component.PositionComponent;

import static com.badlogic.ashley.core.ComponentMapper.getFor;

/**
 * The Camera {@link EntitySystem}
 *
 * @author Fahien
 */
public class CameraSystem extends EntitySystem {
	private static final float CAMERA_Y_OFFSET = 100.0f;
	private static final float CAMERA_Z_OFFSET = 150.0f;
	private ComponentMapper<PositionComponent> pm = getFor(PositionComponent.class);

	Vector3 playerPosition;
	private MainCamera camera;

	public CameraSystem(MainCamera camera) {
		this.camera = camera;
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);

		// Get the player position
		Family family = Family.all(PlayerComponent.class).get();
		ImmutableArray<Entity> entities = engine.getEntitiesFor(family);
		if (entities.size() > 0) {
			Entity player = entities.get(0);
			playerPosition = pm.get(player).getPosition();
			camera.position.x = playerPosition.x;
			camera.position.y = playerPosition.y + CAMERA_Y_OFFSET;
			camera.position.z = playerPosition.z + CAMERA_Z_OFFSET;
			camera.lookAt(playerPosition);
		}
		camera.update();
	}

	@Override
	public void update(float deltaTime) {
		// Follow the player
		camera.position.x = playerPosition.x;
		camera.position.y = playerPosition.y + CAMERA_Y_OFFSET;
		camera.position.z = playerPosition.z + CAMERA_Z_OFFSET;
		camera.update();
	}
}
