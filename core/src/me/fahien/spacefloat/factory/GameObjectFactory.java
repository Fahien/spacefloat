package me.fahien.spacefloat.factory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import me.fahien.spacefloat.entity.GameObject;
import me.fahien.spacefloat.game.SpaceFloatGame;

import static me.fahien.spacefloat.game.SpaceFloatGame.logger;

/**
 * The {@link GameObject} Factory
 */
public class GameObjectFactory {
	protected static final String OBJECTS_DIR = "objects/";
	protected static final String OBJECT_LIST = OBJECTS_DIR + "objects.txt";
	protected static final String JSON_EXT = ".json";

	private Json json;

	public GameObjectFactory() {
		json = new Json();
	}

	/**
	 * Save a {@link GameObject}
	 */
	public void save(GameObject object) {
		json.toJson(object, Gdx.files.local(OBJECTS_DIR + object.getName() + JSON_EXT));
	}

	/**
	 * Load a {@link GameObject}
	 */
	public GameObject load(String testName) {
		return json.fromJson(GameObject.class, Gdx.files.local(OBJECTS_DIR + testName + JSON_EXT));
	}

	/**
	 * Loads all {@link GameObject} in OBJECTS_DIR
	 */
	public Array<GameObject> loadOldObjects() {
		FileHandle[] objects = Gdx.files.local(OBJECTS_DIR).list();
		if (objects.length == 0) {
			objects = Gdx.files.internal(OBJECTS_DIR).list();
		}
		logger.info("Objects in " + OBJECTS_DIR + ": " + objects.length);
		Array<GameObject> array = new Array<>((int)objects.length);
		for (FileHandle object : objects) {
			array.add(load(object.nameWithoutExtension()));
		}
		logger.info("Loaded " + array.size + " objects");
		return array;
	}

	/**
	 * Loads the {@link GameObject} list
	 */
	public Array<GameObject> loadObjects() {
		Array<GameObject> objects = loadLocalModels();
		if (objects == null) {
			logger.error("No objects found in the local directory: " + OBJECTS_DIR);
			objects = loadInternalObjects();
		}
		return objects;
	}

	/**
	 * Loads all {@link GameObject} in the local OBJECTS_DIR
	 */
	public Array<GameObject> loadLocalModels() {
		FileHandle modelDir = Gdx.files.local(OBJECTS_DIR);
		if (modelDir.isDirectory()) {
			FileHandle[] files = modelDir.list();
			Array<GameObject> objects;
			if (files.length > 0) {
				objects = new Array<>(files.length);
				for (FileHandle file : files) {
					if (file.name().endsWith(JSON_EXT)) {
						objects.add(load(file.nameWithoutExtension()));
					}
				}
				logger.info(objects.size + " object found in the local directory: " + OBJECTS_DIR);
				return objects;
			}
		}
		return null;
	}

	/**
	 * Loads all {@link GameObject} in the internal OBJECTS_DIR
	 */
	public Array<GameObject> loadInternalObjects() {
		FileHandle file = Gdx.files.internal(OBJECT_LIST);
		String listString = file.readString();
		String[] modelNames = listString.split("\n");
		Array<GameObject> objects;
		if (modelNames.length > 0) {
			objects = new Array<>(modelNames.length);
			for (String modelName : modelNames) {
				objects.add(load(modelName));
			}
			logger.info(objects.size + " object found in the internal directory: " + OBJECTS_DIR);
			return objects;
		}
		return null;
	}
}
