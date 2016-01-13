package me.fahien.spacefloat.factory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import me.fahien.spacefloat.entity.GameObject;
import me.fahien.spacefloat.utils.JsonKey;

import static me.fahien.spacefloat.game.SpaceFloatGame.logger;

/**
 * The {@link GameObject} Factory
 *
 * @author Fahien
 */
public enum GameObjectFactory {
	INSTANCE;

	protected static final String OBJECTS_DIR = "objects/";
	protected static final String OBJECT_LIST = OBJECTS_DIR + "objects.txt";

	private Json json;

	/**
	 * Sets {@link Json}
	 */
	public void setJson(Json json) {
		this.json = json;
	}

	/**
	 * Creates the {@link GameObject}s list
	 */
	private void createLocalObjectList() {
		String objectList = "";
		FileHandle[] files = Gdx.files.local(GameObjectFactory.OBJECTS_DIR).list();
		for (FileHandle file : files) {
			if (file.path().endsWith(JsonKey.JSON_EXT)) {
				objectList += file.nameWithoutExtension() + "\n";
			}
		}
		FileHandle fileList = Gdx.files.local(GameObjectFactory.OBJECT_LIST);
		fileList.writeString(objectList, false);
	}

	/**
	 * Saves a {@link GameObject}
	 */
	public void save(GameObject object) {
		json.toJson(object, Gdx.files.local(OBJECTS_DIR + object.getName() + JsonKey.JSON_EXT));
	}

	/**
	 * Saves all {@link GameObject}s
	 */
	public void saveObjects(Array<GameObject> objects) {
		for (GameObject object : objects) {
			save(object);
		}
		createLocalObjectList();
	}

	/**
	 * Loads a local {@link GameObject}
	 */
	public GameObject load(String testName) {
		return json.fromJson(GameObject.class, Gdx.files.local(OBJECTS_DIR + testName + JsonKey.JSON_EXT));
	}

	/**
	 * Loads an internal {@link GameObject}
	 */
	public GameObject loadInternal(String testName) {
		return json.fromJson(GameObject.class, Gdx.files.internal(OBJECTS_DIR + testName + JsonKey.JSON_EXT));
	}

	/**
	 * Loads all {@link GameObject} in the internal OBJECTS_DIR
	 */
	public Array<GameObject> loadInternalObjects() {
		FileHandle file = Gdx.files.internal(OBJECT_LIST);
		String listString = file.readString();
		String[] objectNames = listString.split("\n");
		Array<GameObject> objects;
		if (objectNames.length > 0) {
			objects = new Array<>(objectNames.length);
			for (String objectName : objectNames) {
				objects.add(loadInternal(objectName));
			}
			logger.info(objects.size + " object found in the internal directory: " + OBJECTS_DIR);
			return objects;
		}
		return null;
	}


	/**
	 * Loads all {@link GameObject} in the local OBJECTS_DIR
	 */
	public Array<GameObject> loadLocalObjects() {
		FileHandle objectsDir = Gdx.files.local(OBJECTS_DIR);
		if (objectsDir.isDirectory()) {
			FileHandle[] files = objectsDir.list();
			Array<GameObject> objects;
			if (files.length > 0) {
				objects = new Array<>(files.length);
				for (FileHandle file : files) {
					if (file.name().endsWith(JsonKey.JSON_EXT)) {
						objects.add(load(file.nameWithoutExtension()));
					}
				}
				logger.info(objects.size + " objects found in the local directory: " + OBJECTS_DIR);
				return objects;
			}
		}
		return null;
	}

	/**
	 * Loads the {@link GameObject} list
	 */
	public Array<GameObject> loadObjects() {
		Array<GameObject> objects = loadLocalObjects();
		if (objects == null) {
			logger.error("No objects found in the local directory: " + OBJECTS_DIR);
			objects = loadInternalObjects();
		}
		return objects;
	}
}
