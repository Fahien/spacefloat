package me.fahien.spacefloat.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector3;

import me.fahien.spacefloat.component.DestinationComponent;
import me.fahien.spacefloat.component.PlayerComponent;
import me.fahien.spacefloat.component.TransformComponent;
import me.fahien.spacefloat.screen.SpaceFloatScreen;

import static com.badlogic.ashley.core.Family.all;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.destinationMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.transformMapper;

/**
 * The Destination {@link IteratingSystem}
 *
 * @author Fahien
 */
public class DestinationSystem extends IteratingSystem {
	private static final int DESTINATION_PRIORITY = 2;

	public DestinationSystem() {
		super(all(PlayerComponent.class, DestinationComponent.class, TransformComponent.class).get(), DESTINATION_PRIORITY);
	}

	@Override
	protected void processEntity(final Entity entity, final float delta) {
		updateDestination(destinationMapper.get(entity), transformMapper.get(entity));
	}

	private void updateDestination(final DestinationComponent destination, final TransformComponent transform) {
		updateIndicator(destination.getIndicator(), destination.getPosition(), transform.getPosition());
	}

	private void updateIndicator(final Vector3 indicator, final Vector3 position, final Vector3 center) {
		indicator.set(position);
		indicator.sub(center);
		indicator.nor().scl(SpaceFloatScreen.HEIGHT);
		indicator.add(center);
	}
}
