package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import static me.fahien.spacefloat.utils.JsonString.JSON_NAME;

/**
 * The Graphic {@link Component}
 *
 * @author Fahien
 */
public class GraphicComponent implements Component, Json.Serializable {
	public static final String MODELS_DIR = "models/";

	private String name;
	private ModelInstance instance;

	public GraphicComponent() {}

	public GraphicComponent(String name) {
		this.name = name;
	}

	/**
	 * Returns the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the {@link ModelInstance}
	 */
	public ModelInstance getInstance() {
		return instance;
	}

	/**
	 * Sets the {@link ModelInstance}
	 */
	public void setInstance(ModelInstance instance) {
		this.instance = instance;
	}

	/**
	 * Sets the instance position
	 */
	public void setPosition(Vector3 position) {
		instance.transform.setTranslation(position);
	}

	/**
	 * Sets the instance rotation
	 */
	public void setFromEulerAngles(Vector3 eulerAngles) {
		instance.transform.setFromEulerAnglesRad(eulerAngles.x, eulerAngles.y, eulerAngles.z);
	}

	@Override
	public void write(Json json) {
		json.writeValue(JSON_NAME, name);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		name = jsonData.getString(JSON_NAME);
	}
}