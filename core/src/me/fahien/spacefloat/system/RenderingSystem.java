package me.fahien.spacefloat.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;

import me.fahien.spacefloat.camera.MainCamera;
import me.fahien.spacefloat.component.GraphicComponent;
import me.fahien.spacefloat.component.TransformComponent;
import me.fahien.spacefloat.component.VelocityComponent;
import me.fahien.spacefloat.screen.SpaceFloatScreen;

/**
 * The Rendering {@link EntitySystem}
 */
public class RenderingSystem extends EntitySystem {

	private ComponentMapper<VelocityComponent> velocityMapper = ComponentMapper.getFor(VelocityComponent.class);
	private ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);
	private ComponentMapper<GraphicComponent> graphicMapper = ComponentMapper.getFor(GraphicComponent.class);
	private ImmutableArray<Entity> entities;

	private MainCamera camera;
	private Environment environment;
	private ModelBatch batch;
	private ShapeRenderer shapeRenderer;

	// Temp variables
	protected GraphicComponent graphic;
	protected ModelInstance instance;

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
		// Initialize the shape renderer
		shapeRenderer = new ShapeRenderer();
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

		// Begin the batch
		batch.begin(camera);
		shapeRenderer.setProjectionMatrix(camera.combined);

		// Render the model instances
		for (Entity entity : entities) {
			graphic = graphicMapper.get(entity);
			// Render velocity lines
			renderVelocity(transformMapper.get(entity), velocityMapper.get(entity));

			// Render models
			instance = graphic.getInstance();
			if (instance != null) {
				batch.render(instance, environment);
			}
		}

		// End the batch
		batch.end();
	}

	protected Vector3 center;
	protected Vector3 a = new Vector3();
	protected Vector3 b = new Vector3();
	protected Vector3 velocity;
	private void renderVelocity(TransformComponent transformComponent, VelocityComponent velocityComponent) {
		if (velocityComponent == null) return;
		velocity = velocityComponent.getVelocity();
		if (velocity.equals(Vector3.Zero)) return;
		if (velocity.len2() < 1.0f) return;
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		center = transformComponent.getPosition();

		a.x = center.x - velocity.x * SpaceFloatScreen.WIDTH;
		a.y = center.y - velocity.y * SpaceFloatScreen.WIDTH;
		a.z = center.z - velocity.z * SpaceFloatScreen.WIDTH;
		b.x = center.x + velocity.x * SpaceFloatScreen.WIDTH;
		b.y = center.y + velocity.y * SpaceFloatScreen.WIDTH;
		b.z = center.z + velocity.z * SpaceFloatScreen.WIDTH;
		shapeRenderer.setColor(1, 0, 0, 1);
		shapeRenderer.line(a, b);
		shapeRenderer.end();
	}


	/**
	 * Disposes the resources
	 */
	public void dispose() {
		// Dispose the model batch
		if (batch != null) batch.dispose();
	}
}
