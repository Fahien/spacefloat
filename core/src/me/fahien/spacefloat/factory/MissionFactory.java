package me.fahien.spacefloat.factory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import me.fahien.spacefloat.component.GraphicComponent;
import me.fahien.spacefloat.component.MissionComponent;
import me.fahien.spacefloat.component.TransformComponent;
import me.fahien.spacefloat.entity.GameObject;
import me.fahien.spacefloat.game.SpaceFloat;
import me.fahien.spacefloat.mission.Mission;
import me.fahien.spacefloat.utils.JsonKey;

import static me.fahien.spacefloat.game.SpaceFloatGame.logger;

/**
 * The {@link Mission} Factory
 *
 * @author Fahien
 */
public enum MissionFactory {
	INSTANCE;

	public static final String PARCEL_NAME = "parcel";
	public static final String PARCEL_GRAPHIC = "parcel.g3db";

	private static final String MISSIONS_DIR = "missions/";
	private static final String MISSION_LIST = MISSIONS_DIR + "missions.txt";

	private Json json;
	private Engine engine;

	private GameObject parcel;
	private TransformComponent parcelTransform;
	private GraphicComponent parcelGraphic;

	private MissionComponent missionComponent;

	private Array<Mission> missions;

	MissionFactory() {
		parcel = new GameObject(PARCEL_NAME);
		parcelTransform = new TransformComponent();
		parcel.add(parcelTransform);
		parcelGraphic = new GraphicComponent(PARCEL_GRAPHIC);
		parcel.add(parcelGraphic);
	}

	/**
	 * Loads the next {@link Mission}
	 */
	public void loadNextMission() {
		// If there is no missions, do nothing
		if (missions.size == 0) {
			logger.debug("No more missions");
			return;
		}
		if (missionComponent == null) {
			missionComponent = new MissionComponent();
			missionComponent.createCollisionObject();
		}
		GameObject player = SpaceFloat.GAME.getGame().getPlayer();
		for (int i = 0; i < missions.size; i++) {
			// Get first mission
			Mission mission = missions.get(i);
			// If mission is delivered, load next mission
			if (!mission.isDelivered()) {
				missionComponent.setMission(mission);
				// If mission is not collected
				if (!mission.isCollected()) {
					// Set the position
					Vector3 position = mission.getPosition();
					missionComponent.setPosition(position);
					// Update mission component user data
					missionComponent.getCollisionObject().userData = parcel;
					// Updating parcel transform
					parcelTransform.setPosition(position);
					// Update parcel graphic
					parcelGraphic.setPosition(position);
					// Add the mission component to the parcel
					parcel.add(missionComponent);
					// Add parcel game object to the engine
					engine.addEntity(parcel);
				} else {
					// Update mission component user data
					missionComponent.getCollisionObject().userData = player;
					// Add the mission component to the player
					player.add(missionComponent);
				}
				return;
			}
		}
		player.remove(MissionComponent.class);
	}

	/**
	 * Sets {@link Json}
	 */
	public void setJson(final Json json) {
		this.json = json;
	}

	/**
	 * Sets {@link Engine}
	 */
	public void setEngine(final Engine engine) {
		this.engine = engine;
	}

	/**
	 * Creates the mission list
	 */
	private void createLocalMissionList() {
		String missionList = "";
		FileHandle[] files = Gdx.files.local(MISSIONS_DIR).list();
		for (FileHandle file : files) {
			if (file.path().endsWith(JsonKey.JSON_EXT)) {
				missionList += file.nameWithoutExtension() + "\n";
			}
		}
		FileHandle fileList = Gdx.files.local(MISSION_LIST);
		fileList.writeString(missionList, false);
	}

	/**
	 * Saves a {@link Mission}
	 */
	public void save(Mission mission) {
		json.toJson(mission, Gdx.files.local(MISSIONS_DIR + mission.getName() + JsonKey.JSON_EXT));
	}

	/**
	 * Saves all {@link Mission}s
	 */
	public void saveMissions() {
		for (Mission mission : missions) {
			save(mission);
		}
		createLocalMissionList();
	}

	/**
	 * Loads a local {@link Mission}
	 */
	public Mission load(final String missionName) {
		return json.fromJson(Mission.class, Gdx.files.local(MISSIONS_DIR + missionName + JsonKey.JSON_EXT));
	}

	/**
	 * Loads an internal {@link Mission}
	 */
	public Mission loadInternal(String missionName) {
		return json.fromJson(Mission.class, Gdx.files.internal(MISSIONS_DIR + missionName + JsonKey.JSON_EXT));
	}

	/**
	 * Loads all {@link Mission} in the internal MISSIONS_DIR
	 */
	public void loadInternalMissions() {
		if (missions != null) {
			missions.clear();
		}
		/*
		FileHandle file = Gdx.files.local(MISSIONS_DIR);
		if (file.isDirectory()) {
			file.deleteDirectory();
		}
		*/
		if (missionComponent != null && missionComponent.getCollisionObject().isDisposed()) {
			missionComponent = null;
		}
		FileHandle file = Gdx.files.internal(MISSION_LIST);
		String listString = file.readString();
		String[] missionName = listString.split("\n");
		if (missionName.length > 0) {
			missions = new Array<>(missionName.length);
			for (String objectName : missionName) {
				missions.add(loadInternal(objectName));
			}
			logger.info(missions.size + " missions found in the internal directory: " + MISSIONS_DIR);
		}
	}


	/**
	 * Loads all {@link Mission} in the local MISSIONS_DIR
	 */
	public void loadLocalMissions() {
		FileHandle missionsDir = Gdx.files.local(MISSIONS_DIR);
		if (missionsDir.isDirectory()) {
			FileHandle[] files = missionsDir.list();
			if (files.length > 0) {
				missions = new Array<>(files.length);
				for (FileHandle file : files) {
					if (file.name().endsWith(JsonKey.JSON_EXT)) {
						missions.add(load(file.nameWithoutExtension()));
					}
				}
				logger.info(missions.size + " missions found in the local directory: " + MISSIONS_DIR);
			}
		}
	}

	/**
	 * Loads the {@link Mission} list
	 */
	public void loadMissions(final boolean force) {
		if (!force) {
			if (missions != null && missions.size > 0) {
				logger.debug("Missions already loaded");
			} else {
				logger.debug("Loading local objects");
				loadLocalMissions();
			}
		}
		else {
			logger.debug("Reloading local missions");
			loadLocalMissions();
		}
		if (missions == null) {
			logger.error("No missions found in the local directory: " + MISSIONS_DIR);
			loadInternalMissions();
		}
	}

	/**
	 * Returns the {@link Mission}s
	 */
	protected Array<Mission> getMissions() {
		return missions;
	}

	/**
	 * Returns the parcel {@link GraphicComponent}
	 */
	public GraphicComponent getParcelGraphic() {
		return parcelGraphic;
	}

	/**
	 * Disposes resources
	 */
	public void dispose() {
		if (missionComponent != null) {
			missionComponent.dispose();
			missionComponent = null;
		}
	}

	public MissionComponent getMissionComponent() {
		return missionComponent;
	}
}
