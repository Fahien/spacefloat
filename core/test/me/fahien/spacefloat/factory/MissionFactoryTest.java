package me.fahien.spacefloat.factory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import org.junit.Before;
import org.junit.Test;

import me.fahien.spacefloat.mission.Mission;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * The {@link MissionFactory} Test Case
 */
public class MissionFactoryTest {

	private MissionFactory missionFactory;

	@Before
	public void before() {
		missionFactory = MissionFactory.INSTANCE;
		missionFactory.setJson(new Json());
		missionFactory.setEngine(new Engine());
	}

	@Test
	public void couldSaveAMission() {
		Mission mission = new Mission();
		mission.setName("FirstMission");
		mission.setDestination("Earth");
		mission.setPosition(new Vector3(1740.0f, 0.0f, 2310.0f));
		mission.setMessageInitial("Hello! This is Mission Control! The parcel you have picked up has to be delivered to the Earth!");
		mission.setMessageEnding("Well done, space courier!");
		mission.setReward(100);

		missionFactory.save(mission);
	}

	@Test
	public void couldLoadMission() {
		missionFactory.loadMissions();
		Array<Mission> mission = missionFactory.getMissions();
		assertNotNull("Missions array is null", mission);
		assertTrue("Mission array is empty", mission.size > 0);
		missionFactory.saveMissions();
	}
}
