package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import static me.fahien.spacefloat.utils.JsonKey.NAME;

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
	public void setName(final String name) {
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
	public void setInstance(final ModelInstance instance) {
		this.instance = instance;
	}

	/**
	 * Returns the {@link ModelInstance} transform
	 */
	public Matrix4 getTransform() {
		if (instance != null) {
			return instance.transform;
		}
		return null;
	}

	/**
	 * Sets the {@link ModelInstance} transform
	 */
	public void setTransform(final Matrix4 transform) {
		instance.transform = transform;
	}

	/**
	 * Returns the {@link ModelInstance} position
	 */
	public void getPosition(final Vector3 position) {
		instance.transform.getTranslation(position);
	}

	/**
	 * Sets the {@link ModelInstance} position
	 */
	public void setPosition(final Vector3 position) {
		instance.transform.setTranslation(position);
	}

	/**
	 * Sets the {@link ModelInstance} rotation
	 */
	public void setFromEulerAngles(final Vector3 eulerAngles) {
		instance.transform.setFromEulerAngles(eulerAngles.x, eulerAngles.y, eulerAngles.z);
	}

	@Override
	public void write(final Json json) {
		json.writeValue(NAME, name);
	}

	@Override
	public void read(final Json json, JsonValue jsonData) {
		name = jsonData.getString(NAME);
	}
}