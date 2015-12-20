package me.fahien.spacefloat.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;

import me.fahien.spacefloat.camera.MainCamera;
import me.fahien.spacefloat.component.GraphicComponent;

/**
 * The Rendering {@link EntitySystem}
 */
public class RenderingSystem extends EntitySystem {

	private ComponentMapper<GraphicComponent> graphicMapper = ComponentMapper.getFor(GraphicComponent.class);
	private ImmutableArray<Entity> entities;

	private MainCamera camera;
	private Environment environment;
	private ModelBatch batch;

	public RenderingSystem(MainCamera camera) {
		this.camera = camera;
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);

		// Get the Entities
		Family family = Family.all(GraphicComponent.class).get();
		entities = engine.getEntitiesFor(family);

		// Initialize the camera
		camera.update();

		// Initialize the environment
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		// Initialize the model batch
		batch = new ModelBatch();
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

		// Begin the batch
		batch.begin(camera);

		// Render the model instances
		for (Entity entity : entities) {
			GraphicComponent graphic = graphicMapper.get(entity);
			ModelInstance instance = graphic.getInstance();
			if (instance != null) {
				batch.render(instance, environment);
			}
		}

		// End the batch
		batch.end();
	}

	/**
	 * Disposes the resources
	 */
	public void dispose() {
		// Dispose the model batch
		if (batch != null) batch.dispose();
	}
}
