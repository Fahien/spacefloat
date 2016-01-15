package me.fahien.spacefloat.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;

import me.fahien.spacefloat.component.MissionComponent;
import me.fahien.spacefloat.component.MoneyComponent;
import me.fahien.spacefloat.mission.Mission;

import static com.badlogic.ashley.core.Family.all;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.missionMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.moneyMapper;
import static me.fahien.spacefloat.game.SpaceFloatGame.logger;

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
