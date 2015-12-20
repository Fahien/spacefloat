package me.fahien.spacefloat.factory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import me.fahien.spacefloat.entity.GameObject;

/**
 * The {@link GameObject} Factory
 */
public class GameObjectFactory {
	private static final String OBJECTS_DIR = "objects/";
	private static final String JSON_EXT = ".json";

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
	public Array<GameObject> loadObjects() {
		FileHandle objects = Gdx.files.local(OBJECTS_DIR);
		Array<GameObject> array = new Array<>((int)objects.length());
		for (FileHandle object : objects.list()) {
			array.add(load(object.nameWithoutExtension()));
		}
		return array;
	}
}
