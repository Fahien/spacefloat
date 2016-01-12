package me.fahien.spacefloat.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.math.Vector3;

import me.fahien.spacefloat.component.CollisionComponent;
import me.fahien.spacefloat.component.DestinationComponent;
import me.fahien.spacefloat.component.GraphicComponent;
import me.fahien.spacefloat.component.RigidbodyComponent;
import me.fahien.spacefloat.component.TransformComponent;
import me.fahien.spacefloat.utils.ShapeRenderer;
import me.fahien.spacefloat.utils.SpaceFloatShapeRenderer;

import static me.fahien.spacefloat.component.ComponentMapperEnumerator.collisionMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.destinationMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.graphicMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.gravityMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.missionMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.playerMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.rechargeMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.rigidMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.transformMapper;
import static me.fahien.spacefloat.game.SpaceFloatGame.logger;

/**
 * The Rendering {@link EntitySystem}
 *
 * @author Fahien
 */
public class RenderSystem extends EntitySystem {
	private static final int RENDER_PRIORITY = 3;
	private static final boolean debugAll = false;

	private ImmutableArray<Entity> entities;

	private Camera camera;
	private ParticleSystem particleSystem;

	private Environment environment;
	private ModelBatch batch;
	private ShapeRenderer shapeRenderer;

	public RenderSystem() {
		super(RENDER_PRIORITY);
	}

	/**
	 * Sets the {@link Camera}
	 */
	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	/**
	 * Sets the {@link ParticleSystem}
	 */
	public void setParticleSystem(ParticleSystem particleSystem) {
		this.particleSystem = particleSystem;
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);

		// Get the Entities
		Family family = Family.all(GraphicComponent.class, TransformComponent.class).get();
		entities = engine.getEntitiesFor(family);
		setModelInstancesTransform(entities);

		// Initialize the camera
		camera.update();

		// Initialize the environment
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		// Initialize the model batch
		batch = new ModelBatch();

		// Initialize the shape renderer
		shapeRenderer = new SpaceFloatShapeRenderer();
	}

	/**
	 * Sets the {@link ModelInstance}s initial transform
	 */
	private void setModelInstancesTransform(ImmutableArray<Entity> entities) {
		logger.debug("Setting model instances initial transform");
		for (Entity entity : entities) {
			// Get the graphic component
			GraphicComponent graphicComponent = graphicMapper.get(entity);
			// Get the transform component
			TransformComponent transformComponent = transformMapper.get(entity);
			// Set the model instance initial rotation before position
			graphicComponent.setFromEulerAnglesRad(transformComponent.getEulerAngles());
			// Set the model instance initial position after rotation
			graphicComponent.setPosition(transformComponent.getPosition());
		}
	}

	// Temp variables
	protected GraphicComponent m_graphicComponent;
	protected ModelInstance m_modelInstance;
	protected CollisionComponent m_collisionComponent;
	protected RigidbodyComponent m_rigidbodyComponent;
	protected DestinationComponent m_destinationComponent;
	protected Vector3 m_position = new Vector3();

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

		// Begin the batch
		batch.begin(camera);
		shapeRenderer.setProjectionMatrix(camera.combined);

		// Render the model instances
		for (Entity entity : entities) {
			m_graphicComponent = graphicMapper.get(entity);
			// Render models
			m_modelInstance = m_graphicComponent.getInstance();
			if (m_modelInstance != null) batch.render(m_modelInstance, environment);

			m_collisionComponent = gravityMapper.get(entity);
			// Render gravity radius
			renderCollision(m_collisionComponent, Color.PURPLE);


			m_rigidbodyComponent = rigidMapper.get(entity);
			if (m_rigidbodyComponent != null) {
				m_rigidbodyComponent.getPosition(m_position);
			}

			if (playerMapper.get(entity) != null) {
			// Render destination line
				m_destinationComponent = destinationMapper.get(entity);
				if (m_destinationComponent.getName() != null) {
					renderVelocity(m_position, m_destinationComponent.getPosition(), Color.GRAY);
				}
				// Render velocity line
				renderVelocity(m_position, m_rigidbodyComponent.getLinearVelocity(), Color.BLUE);
			}

			if (debugAll) {
				// Render rigidbody radius
				renderRigidbody(m_rigidbodyComponent, Color.RED);

				m_collisionComponent = collisionMapper.get(entity);
				// Render collision radius
				renderCollision(m_collisionComponent, Color.MAGENTA);

				m_collisionComponent = missionMapper.get(entity);
				// Render mission radius
				renderCollision(m_collisionComponent, Color.GOLD);

				m_collisionComponent = rechargeMapper.get(entity);
				// Render recharge radius
				renderCollision(m_collisionComponent, Color.GREEN);
			}
		}

		// Render Particles
		particleSystem.update(); // technically not necessary for rendering
		particleSystem.begin();
		particleSystem.draw();
		particleSystem.end();
		batch.render(particleSystem);

		// End the batch
		batch.end();
	}

	/**
	 * Renders rigid body velocity line
	 */
	private void renderVelocity(Vector3 position, Vector3 velocity, Color color) {
		// Return if velocity is near zero
		if (velocity.len2() < 1.0f) return;
		// Begin shape renderer with line shape type
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		// Set shape renderer color
		shapeRenderer.setColor(color);
		// Draw a line
		shapeRenderer.line(position, velocity);
		// End shape renderer
		shapeRenderer.end();
	}

	protected Vector3 m_center = new Vector3();

	/**
	 * Render collision radius
	 */
	private <T extends CollisionComponent> void renderCollision(T collision, Color color) {
		// Return if collision is null
		if (collision == null) return;
		// Begin shape renderer with line shape type
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		// Set center point equals to collision position
		collision.getPosition(m_center);
		// Set shape renderer color RED
		shapeRenderer.setColor(color);
		// Draw a circle
		shapeRenderer.circle(m_center.x, m_center.z, collision.getRadius());
		// End shape renderer
		shapeRenderer.end();
	}

	private <T extends RigidbodyComponent> void renderRigidbody(T rigidbody, Color color) {
		// Return if collision is null
		if (rigidbody == null) return;
		// Begin shape renderer with line shape type
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		// Set center point equals to collision position
		rigidbody.getPosition(m_center);
		// Set shape renderer color RED
		shapeRenderer.setColor(color);
		// Draw a circle
		shapeRenderer.circle(m_center.x, m_center.z, rigidbody.getRadius());
		// End shape renderer
		shapeRenderer.end();
	}

	@Override
	public void removedFromEngine(Engine engine) {
		super.removedFromEngine(engine);
		// Dispose the model batch
		if (batch != null) batch.dispose();
	}
}
