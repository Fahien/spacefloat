package me.fahien.spacefloat.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

import me.fahien.spacefloat.component.DestinationComponent;
import me.fahien.spacefloat.component.EnergyComponent;
import me.fahien.spacefloat.component.MissionComponent;
import me.fahien.spacefloat.component.PlayerComponent;
import me.fahien.spacefloat.component.RechargeComponent;
import me.fahien.spacefloat.component.TransformComponent;
import me.fahien.spacefloat.entity.GameObject;
import me.fahien.spacefloat.screen.SpaceFloatScreen;

import static com.badlogic.ashley.core.Family.all;
import static com.badlogic.ashley.core.Family.one;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.destinationMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.energyMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.missionMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.transformMapper;
import static me.fahien.spacefloat.game.SpaceFloatGame.logger;

/**
 * The Destination {@link IteratingSystem}
 *
 * @author Fahien
 */
public class DestinationSystem extends IteratingSystem {
	private static final int DESTINATION_PRIORITY = 2;

	public DestinationSystem() {
		super(one(EnergyComponent.class, MissionComponent.class).get(), DESTINATION_PRIORITY);
	}

	private Vector3 energyStationPosition;
	private DestinationComponent destinationComponent;

	private boolean chargeLow;

	@Override
	public void addedToEngine(final Engine engine) {
		super.addedToEngine(engine);
		ImmutableArray<Entity> entities = engine.getEntitiesFor(all(RechargeComponent.class, TransformComponent.class).get());
		energyStationPosition = retrieveEnergyStationPosition(entities);

		entities = engine.getEntitiesFor(all(PlayerComponent.class).get());
		destinationComponent = retrieveDestinationComponent(entities);
	}

	private Vector3 retrieveEnergyStationPosition(final ImmutableArray<Entity> entities) {
		if (entities.size() > 0) {
			Entity energyStation = entities.first();
			TransformComponent transform = transformMapper.get(energyStation);
			if (transform != null) {
				return transform.getPosition();
			}
		}
		logger.error("Could not retrieve energy station position");
		Gdx.app.exit();
		return null;
	}

	private DestinationComponent retrieveDestinationComponent(final ImmutableArray<Entity> entities) {
		if (entities.size() > 0) {
			Entity player = entities.first();
			DestinationComponent destination = destinationMapper.get(player);
			if (destination != null) {
				return destination;
			}
		}
		logger.error("Could not retrieve the player destination component");
		Gdx.app.exit();
		return null;
	}

	@Override
	protected void processEntity(final Entity entity, final float delta) {
		if (((GameObject) entity).isPlayer()) {
			EnergyComponent energy = energyMapper.get(entity);
			if (energy != null) {
				chargeLow = energy.getCharge() / energy.getChargeMax() < 1/3f;
				if (chargeLow) {
					destinationComponent.setPosition(energyStationPosition);
				} else {
					MissionComponent mission = missionMapper.get(entity);
					if (mission != null) {
						Vector3 targetPosition = mission.getDestinationPosition();
						if (targetPosition != null) {
							destinationComponent.setPosition(targetPosition);
						} else {
							logger.error("Target position is null");
							Gdx.app.exit();
						}
					} else {
						destinationComponent.setPosition(null);
					}
				}
				TransformComponent transform = transformMapper.get(entity);
				updateDestination(destinationComponent, transform);
			} else {
				logger.error("The player has no energy component");
				Gdx.app.exit();
			}
		} else if (!chargeLow){
			// It is the parcel
			TransformComponent transform = transformMapper.get(entity);
			if (transform != null) {
				destinationComponent.setPosition(transform.getPosition());
			} else {
				logger.error("The parcel has no transform component");
				Gdx.app.exit();
			}
		}
	}

	private void updateDestination(final DestinationComponent destination, final TransformComponent transform) {
		updateIndicator(destination.getIndicator(), destination.getPosition(), transform.getPosition());
	}

	private void updateIndicator(final Vector3 indicator, final Vector3 position, final Vector3 center) {
		if (position != null) {
			indicator.set(position);
			indicator.sub(center);
			indicator.nor().scl(SpaceFloatScreen.HEIGHT);
			indicator.add(center);
		}
	}
}
