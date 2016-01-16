package me.fahien.spacefloat.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;

import me.fahien.spacefloat.component.MissionComponent;
import me.fahien.spacefloat.component.MoneyComponent;
import me.fahien.spacefloat.component.TransformComponent;
import me.fahien.spacefloat.entity.GameObject;
import me.fahien.spacefloat.mission.Mission;

import static com.badlogic.ashley.core.Family.all;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.missionMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.moneyMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.transformMapper;

/**
 * The Mission {@link IteratingSystem}
 *
 * @author Fahien
 */
public class MissionSystem extends IteratingSystem {
	private static final int MISSION_PRIORITY = 3;

	private float money_delta;

	public MissionSystem() {
		super(all(MissionComponent.class).get(), MISSION_PRIORITY);
	}

	protected MissionComponent m_missionComponent;
	protected MoneyComponent m_moneyComponent;

	@Override
	public void addedToEngine(final Engine engine) {
		super.addedToEngine(engine);

		updateTargetPosition(engine, getEntities());
	}

	/**
	 * Updates the mission component target position if the parcel is collected but not delivered
	 */
	private void updateTargetPosition(final Engine engine, final ImmutableArray<Entity> entities) {
		// If there are entities
		if (entities.size() > 0) {
			// Get the first game object
			GameObject gameObject = (GameObject) entities.first();
			// If it is the player
			if (gameObject.isPlayer()) {
				// Get the mission component
				m_missionComponent = missionMapper.get(gameObject);
				// Get the mission
				Mission mission = m_missionComponent.getMission();
				// Get the family of transform
				Family familyTransform = all(TransformComponent.class).get();
				// Get entities with a transform
				ImmutableArray<Entity> entitiesTransform = engine.getEntitiesFor(familyTransform);
				// For every entity
				for (Entity entity : entitiesTransform) {
					// If it is the destination
					if (mission.getDestination().equals(((GameObject) entity).getName())) {
						// Get the transform
						TransformComponent transform = transformMapper.get(entity);
						// Update the mission component destination position
						m_missionComponent.setDestinationPosition(transform.getPosition());
						// Done
						break;
					}
				}
			}
		}
	}

	@Override
	protected void processEntity(final Entity entity, final float delta) {
		m_missionComponent = missionMapper.get(entity);

		// Update hangling time
		updateHandlingTime(m_missionComponent, delta);

		// Update money reward
		m_moneyComponent = moneyMapper.get(entity);
		if (m_moneyComponent != null) {
			if (money_delta >= 1.0f) money_delta = 0.0f;
			money_delta += delta * 1024.0f;
			if (money_delta >= 1.0f) {
				updateReward(m_missionComponent.getMission(), m_moneyComponent, (int) money_delta);
			}
		}
	}

	private void updateHandlingTime(final MissionComponent mission, final float delta) {
		// If is collecting or delivering
		if (mission.isCollecting() || mission.isDelivering()) {
			// Decrease handling time
			mission.addHandlingTime(-delta);
		}
	}

	private void updateReward(final Mission mission, final MoneyComponent money, final int reward) {
		if (mission.isCollected() && !mission.isDelivered()) {
			mission.addReward(-reward);
			money.setReward(mission.getReward());
		}
	}
}
