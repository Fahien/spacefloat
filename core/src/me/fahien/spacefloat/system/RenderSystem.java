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
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.math.Vector3;

import me.fahien.spacefloat.camera.MainCamera;
import me.fahien.spacefloat.component.AccelerationComponent;
import me.fahien.spacefloat.component.CollisionComponent;
import me.fahien.spacefloat.component.GraphicComponent;
import me.fahien.spacefloat.component.GravityComponent;
import me.fahien.spacefloat.component.ReactorComponent;
import me.fahien.spacefloat.component.RefuelComponent;
import me.fahien.spacefloat.component.TransformComponent;
import me.fahien.spacefloat.component.VelocityComponent;
import me.fahien.spacefloat.screen.SpaceFloatScreen;
import me.fahien.spacefloat.utils.ShapeRenderer;
import me.fahien.spacefloat.utils.SpaceFloatShapeRenderer;

/**
 * The Rendering {@link EntitySystem}
 *
 * @author Fahien
 */
public class RenderSystem extends EntitySystem {

	private ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);
	private ComponentMapper<VelocityComponent> velocityMapper = ComponentMapper.getFor(VelocityComponent.class);
	private ComponentMapper<AccelerationComponent> accelerationMapper = ComponentMapper.getFor(AccelerationComponent.class);
	private ComponentMapper<GraphicComponent> graphicMapper = ComponentMapper.getFor(GraphicComponent.class);
	private ComponentMapper<GravityComponent> gravityMapper = ComponentMapper.getFor(GravityComponent.class);
	private ComponentMapper<CollisionComponent> collisionMapper = ComponentMapper.getFor(CollisionComponent.class);
	private ComponentMapper<ReactorComponent> reactorMapper = ComponentMapper.getFor(ReactorComponent.class);
	private ComponentMapper<RefuelComponent> refuelMapper = ComponentMapper.getFor(RefuelComponent.class);
	private ImmutableArray<Entity> entities;

	private MainCamera camera;
	private ParticleSystem particleSystem;

	private Environment environment;
	private ModelBatch batch;
	private ShapeRenderer shapeRenderer;

	// Temp variables
	protected GraphicComponent m_graphicComponent;
	protected ModelInstance instance;

	/**
	 * Sets the {@link MainCamera}
	 */
	public void setCamera(MainCamera camera) {
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
		shapeRenderer = new SpaceFloatShapeRenderer();
	}

	protected TransformComponent m_transformComponent;
	protected VelocityComponent m_velocityComponent;
	protected AccelerationComponent m_accelerationComponent;
	protected CollisionComponent m_collisionComponent;
	protected ReactorComponent m_reactorComponent;
	protected RefuelComponent m_refuelComponent;

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

		// Begin the batch
		batch.begin(camera);
		shapeRenderer.setProjectionMatrix(camera.combined);

		// Render the model instances
		for (Entity entity : entities) {
			m_transformComponent = transformMapper.get(entity);
			m_velocityComponent = velocityMapper.get(entity);
			m_accelerationComponent = accelerationMapper.get(entity);
			m_reactorComponent = reactorMapper.get(entity);
			m_graphicComponent = graphicMapper.get(entity);

			// Render velocity line
			renderVelocity(m_transformComponent, m_velocityComponent);

			// Render gravity radius
			if (gravityMapper.get(entity) != null) {
				renderGravity(m_transformComponent);
			}

			// Render collision radius
			m_collisionComponent = collisionMapper.get(entity);
			renderCollision(m_collisionComponent);

			// Render refuel radius
			m_refuelComponent = refuelMapper.get(entity);
			renderRefuel(m_refuelComponent);

			// Render reactor particle effect
			renderReactor(m_accelerationComponent, m_graphicComponent, m_reactorComponent);

			// Render models
			instance = m_graphicComponent.getInstance();
			if (instance != null) {
				batch.render(instance, environment);
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

	protected Vector3 m_velocityCenter;
	protected Vector3 m_a = new Vector3();
	protected Vector3 m_b = new Vector3();
	protected Vector3 m_velocity;

	/**
	 * Renders velocity line
	 */
	private void renderVelocity(TransformComponent transformComponent, VelocityComponent velocityComponent) {
		if (velocityComponent == null) return;
		m_velocity = velocityComponent.getVelocity();
		if (m_velocity.equals(Vector3.Zero)) return;
		if (m_velocity.len2() < 1.0f) return;
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		m_velocityCenter = transformComponent.getPosition();

		m_a.x = m_velocityCenter.x - m_velocity.x * SpaceFloatScreen.WIDTH;
		m_a.y = m_velocityCenter.y - m_velocity.y * SpaceFloatScreen.WIDTH;
		m_a.z = m_velocityCenter.z - m_velocity.z * SpaceFloatScreen.WIDTH;
		m_b.x = m_velocityCenter.x + m_velocity.x * SpaceFloatScreen.WIDTH;
		m_b.y = m_velocityCenter.y + m_velocity.y * SpaceFloatScreen.WIDTH;
		m_b.z = m_velocityCenter.z + m_velocity.z * SpaceFloatScreen.WIDTH;
		// Set shape renderer color BLUE
		shapeRenderer.setColor(0, 0, 1, 1);
		// Draw a line
		shapeRenderer.line(m_a, m_b);
		shapeRenderer.end();
	}

	protected Vector3 m_gravityCenter;

	/**
	 * Renders gravity radius
	 */
	private void renderGravity(TransformComponent transform) {
		// Return if components are null
		if (transform == null) return;
		// Begin shape renderer with line shape type
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		// Set center point equals to collision position
		m_gravityCenter = transform.getPosition();
		// Set shape renderer color PURPLE
		shapeRenderer.setColor(1, 0, 1, 1);
		// Draw a circle
		shapeRenderer.circle(m_gravityCenter.x, m_gravityCenter.z, GravitySystem.MAX_DISTANCE);
		// End shape renderer
		shapeRenderer.end();
	}

	protected Vector3 m_collisionCenter = new Vector3();

	/**
	 * Render collision radius
	 */
	private void renderCollision(CollisionComponent collision) {
		// Return if collision is null
		if (collision == null) return;
		// Begin shape renderer with line shape type
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		// Set center point equals to collision position
		collision.getPosition(m_collisionCenter);
		// Set shape renderer color RED
		shapeRenderer.setColor(1, 0, 0, 1);
		// Draw a circle
		shapeRenderer.circle(m_collisionCenter.x, m_collisionCenter.z, collision.getRadius());
		// End shape renderer
		shapeRenderer.end();
	}

	/**
	 * Render refuel radius
	 */
	private void renderRefuel(RefuelComponent refuel) {
		// Return if refuel is null
		if (refuel == null) return;
		// Begin shape renderer with line shape type
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		// Set center point equals to collision position
		refuel.getPosition(m_collisionCenter);
		// Set shape renderer color GREEN
		shapeRenderer.setColor(0, 1, 0, 1);
		// Draw a circle
		shapeRenderer.circle(m_collisionCenter.x, m_collisionCenter.z, refuel.getRadius());
		// End shape renderer
		shapeRenderer.end();
	}

	/**
	 * Renders reactor particle effect
	 */
	private void renderReactor(AccelerationComponent acceleration, GraphicComponent graphic, ReactorComponent reactor) {
		if (acceleration == null) return;
		if (acceleration.getAcceleration().equals(Vector3.Zero)) {
			reactor.stop(particleSystem);
		} else {
			reactor.start(particleSystem, graphic.getInstance().transform);
		}
	}

	@Override
	public void removedFromEngine(Engine engine) {
		super.removedFromEngine(engine);
		// Dispose the model batch
		if (batch != null) batch.dispose();
	}
}
