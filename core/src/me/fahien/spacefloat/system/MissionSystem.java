package me.fahien.spacefloat.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;

import me.fahien.spacefloat.component.ComponentMapperEnumerator;
import me.fahien.spacefloat.component.MissionComponent;
import me.fahien.spacefloat.component.MoneyComponent;
import me.fahien.spacefloat.game.SpaceFloatGame;
import me.fahien.spacefloat.mission.Mission;

import static com.badlogic.ashley.core.Family.all;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.missionMapper;

/**
 * The Mission {@link IteratingSystem}
 *
 * @author Fahien
 */
public class MissionSystem extends IteratingSystem {
	private static final int MISSION_PRIORITY = 3;

	private float money_delta;

	public MissionSystem() {
		super(all(MissionComponent.class, MoneyComponent.class).get(), MISSION_PRIORITY);
	}

	@Override
	protected void processEntity(final Entity entity, final float delta) {
		if (money_delta >= 1.0f) money_delta = 0.0f;
		money_delta += delta * 1024.0f;
		if (money_delta >= 1.0f) {
			updateMission(missionMapper.get(entity).getMission(), ComponentMapperEnumerator.moneyMapper.get(entity), (int) money_delta);
		}
	}

	private void updateMission(Mission mission, MoneyComponent money, int reward) {
		if (mission.isCollected() && !mission.isDelivered()) {
			mission.addReward(-reward);
			money.setReward(mission.getReward());
			SpaceFloatGame.logger.debug("Reward: " + mission.getReward());
		}
	}
}