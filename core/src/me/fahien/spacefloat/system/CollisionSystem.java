package me.fahien.spacefloat.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;

import me.fahien.spacefloat.component.CollisionComponent;
import me.fahien.spacefloat.component.GraphicComponent;
import me.fahien.spacefloat.component.GravityComponent;
import me.fahien.spacefloat.component.HurtComponent;
import me.fahien.spacefloat.component.PlanetComponent;
import me.fahien.spacefloat.component.RechargeComponent;
import me.fahien.spacefloat.component.RigidbodyComponent;
import me.fahien.spacefloat.entity.GameObject;

import static com.badlogic.ashley.core.Family.all;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.graphicMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.gravityMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.hurtMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.planetMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.rechargeMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.rigidMapper;
import static me.fahien.spacefloat.game.SpaceFloatGame.logger;

/**
 * The Collision {@link EntitySystem}
 *
 * @author Fahien
 */
public class CollisionSystem extends EntitySystem {
	private static final int COLLISION_PRIORITY = 1;
	public static float RECHARGE_POWER = 0.125f;

	private ImmutableArray<Entity> planetEntities;
	private ImmutableArray<Entity> gravityEntities;
	private ImmutableArray<Entity> rechargeEntities;
	private ImmutableArray<Entity> hurtEntities;
	private ImmutableArray<Entity> rigidEntities;

	private btDefaultCollisionConfiguration collisionConfig;
	private btCollisionDispatcher dispatcher;

	private btBroadphaseInterface broadphase;
	private btConstraintSolver constraintSolver;
	private btDynamicsWorld dynamicsWorld;

	private SpaceFloatContactListener contactListener;

	public CollisionSystem() {
		super(COLLISION_PRIORITY);
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);

		planetEntities = engine.getEntitiesFor(all(GraphicComponent.class, PlanetComponent.class).get());
		createCollisionObjects(planetEntities, PlanetComponent.class);

		gravityEntities = engine.getEntitiesFor(all(GraphicComponent.class, GravityComponent.class).get());
		createCollisionObjects(gravityEntities, GravityComponent.class);

		rechargeEntities = engine.getEntitiesFor(all(GraphicComponent.class, RechargeComponent.class).get());
		createCollisionObjects(rechargeEntities, RechargeComponent.class);

		hurtEntities = engine.getEntitiesFor(all(GraphicComponent.class, HurtComponent.class).get());
		createRigidbodies(hurtEntities, HurtComponent.class);

		rigidEntities = engine.getEntitiesFor(all(GraphicComponent.class, RigidbodyComponent.class).get());
		createRigidbodies(rigidEntities, RigidbodyComponent.class);

		collisionConfig = new btDefaultCollisionConfiguration();
		dispatcher = new btCollisionDispatcher(collisionConfig);

		broadphase = new btDbvtBroadphase();
		constraintSolver = new btSequentialImpulseConstraintSolver();
		dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, constraintSolver, collisionConfig);

		contactListener = new SpaceFloatContactListener();

		addCollisionObjectsToDynamicsWorld(planetEntities, PlanetComponent.class);
		addCollisionObjectsToDynamicsWorld(gravityEntities, GravityComponent.class);
		addCollisionObjectsToDynamicsWorld(rechargeEntities, RechargeComponent.class);

		addRigidbodiesToDynamicsWorld(hurtEntities, HurtComponent.class);
		addRigidbodiesToDynamicsWorld(rigidEntities, RigidbodyComponent.class);
	}

	/**
	 * Initialize every {@link CollisionComponent}
	 */
	protected void createCollisionObjects(ImmutableArray<Entity> entities, Class<? extends CollisionComponent> collisionType) {
		logger.debug("Creating collision objects for " + collisionType.getSimpleName());
		for (Entity entity : entities) {
			// Get the collision component
			CollisionComponent collisionComponent = entity.getComponent(collisionType);
			// Create the collision object
			collisionComponent.createCollisionObject();
			// Get the graphic component
			GraphicComponent graphic = graphicMapper.get(entity);
			// Set transform
			collisionComponent.setWorldTransform(graphic.getTransform());
			// Set user data
			collisionComponent.userData = entity;
		}
	}

	/**
	 * Initializes every {@link RigidbodyComponent}
	 */
	protected void createRigidbodies(ImmutableArray<Entity> entities, Class<? extends RigidbodyComponent> rigidbodyType) {
		logger.debug("Creating rigidbodies for " + rigidbodyType.getSimpleName());
		for (Entity entity : entities) {
			// Get the graphic component
			GraphicComponent graphic = graphicMapper.get(entity);
			// Get the rigidbody component
			RigidbodyComponent rigidbodyComponent = entity.getComponent(rigidbodyType);
			// Create the rigidbody
			rigidbodyComponent.createRigidbody();
			// Set transform
			rigidbodyComponent.setTransform(graphic.getTransform());
			// Set user data
			rigidbodyComponent.getRigidbody().userData = entity;
		}
	}

	/**
	 * Adds {@link btCollisionObject}s to the {@link btDynamicsWorld}
	 */
	private void addCollisionObjectsToDynamicsWorld(ImmutableArray<Entity> entities, Class<? extends CollisionComponent> collisionType) {
		for (Entity entity : entities) {
			CollisionComponent collisionComponent = entity.getComponent(collisionType);
			dynamicsWorld.addCollisionObject(collisionComponent, collisionComponent.getGroup(), collisionComponent.getMask());
		}
	}

	/**
	 * Adds {@link btRigidBody}s to the {@link btDynamicsWorld}
	 */
	private void addRigidbodiesToDynamicsWorld(ImmutableArray<Entity> entities, Class<? extends RigidbodyComponent> rigidbodyType) {
		for (Entity entity : entities) {
			RigidbodyComponent rigidbodyComponent = entity.getComponent(rigidbodyType);
			dynamicsWorld.addRigidBody(rigidbodyComponent.getRigidbody(), rigidbodyComponent.getGroup(), rigidbodyComponent.getMask());
		}
	}

	@Override
	public void update(float delta) {
		for (Entity entity : planetEntities) {
			updateCollisionObjects(planetMapper.get(entity), graphicMapper.get(entity));
		}
		for (Entity entity : gravityEntities) {
			updateCollisionObjects(gravityMapper.get(entity), graphicMapper.get(entity));
		}
		for (Entity entity : rechargeEntities) {
			updateCollisionObjects(rechargeMapper.get(entity), graphicMapper.get(entity));
		}
		dynamicsWorld.stepSimulation(delta, 5, 0.000001f);
		for (Entity entity : hurtEntities) {
			updateModelInstances(hurtMapper.get(entity), graphicMapper.get(entity));
		}
		for (Entity entity : rigidEntities) {
			updateModelInstances(rigidMapper.get(entity), graphicMapper.get(entity));
		}

	}

	/**
	 * Updates {@link btCollisionObject}s transform
	 */
	protected void updateCollisionObjects(CollisionComponent collision, GraphicComponent graphic) {
		collision.setTransform(graphic.getTransform());
	}

	protected Matrix4 m_transform;
	protected Vector3 m_position = new Vector3();

	/**
	 * Updates {@link ModelInstance}s transform
	 */
	protected void updateModelInstances(RigidbodyComponent rigidbody, GraphicComponent graphic) {
		m_transform = graphic.getTransform();
		rigidbody.getTransform(m_transform);
		m_transform.getTranslation(m_position);
		m_position.y = 0f;
		m_transform.setTranslation(m_position);
		rigidbody.setTransform(m_transform);
	}

	@Override
	public void removedFromEngine(Engine engine) {
		super.removedFromEngine(engine);

		if (dynamicsWorld != null) dynamicsWorld.dispose();
		constraintSolver.dispose();
		broadphase.dispose();

		if (contactListener != null) contactListener.dispose();

		dispatcher.dispose();
		collisionConfig.dispose();

		for (Entity entity : gravityEntities) {
			gravityMapper.get(entity).dispose();
		}
		for (Entity entity : rechargeEntities) {
			rechargeMapper.get(entity).dispose();
		}
		for (Entity entity : hurtEntities) {
			hurtMapper.get(entity).dispose();
		}
		for (Entity entity : rigidEntities) {
			rigidMapper.get(entity).dispose();
		}
	}

	/**
	 * The Space Float {@link ContactListener}
	 *
	 * @author Fahien
	 */
	class SpaceFloatContactListener extends ContactListener {

		@Override
		public boolean onContactAdded(btCollisionObject collision0, int partId0, int index0, btCollisionObject collision1, int partId1, int index1) {
			if (collision0 instanceof CollisionComponent) {
				handleCollision((CollisionComponent) collision0, collision1);
			}
			if (collision1 instanceof CollisionComponent) {
				handleCollision((CollisionComponent) collision1, collision0);
			}
			return true;
		}

		private void handleCollision(CollisionComponent component, btCollisionObject collision) {
			component.collideWith((GameObject) collision.userData);
		}

		/*
				private Vector3 normal;
				private GameObject object0;
				private GameObject object1;

				public SpaceFloatContactListener() {
					normal = new Vector3();
				}

				protected CollisionComponent collision0;
				protected CollisionComponent collision1;

				@Override
				public boolean onContactAdded(btManifoldPoint cp,
											  btCollisionObjectWrapper colObj0Wrap, int partId0, int index0,
											  btCollisionObjectWrapper colObj1Wrap, int partId1, int index1) {
					try {
						collision0 = (CollisionComponent) colObj0Wrap.getCollisionObject();
						collision1 = (CollisionComponent) colObj1Wrap.getCollisionObject();
						object0 = (GameObject) collision0.userData;
						object1 = (GameObject) collision1.userData;
						cp.getNormalWorldOnB(normal);
						if (collision0 instanceof RechargeComponent) {
							logger.debug("Computing refuel between " + object0.getName() + " and " + object1.getName());
							computeRecharge(energyMapper.get(object1), velocityMapper.get(object1));
							return true;
						}
						if (collision1 instanceof RechargeComponent) {
							logger.debug("Computing refuel between " + object1.getName() + " and " + object0.getName());
							computeRecharge(energyMapper.get(object0), velocityMapper.get(object0));
							return true;
						}

						if (collision0 instanceof GravityComponent) {
							logger.debug("Computing collision between " + object0.getName() + " and " + object1.getName());
							computeCollision(gravityMapper.get(object0), object1, velocityMapper.get(object1), accelerationMapper.get(object1), energyMapper.get(object1), normal);
							return true;
						}
						if (collision1 instanceof GravityComponent) {
							logger.debug("Computing collision between " + object1.getName() + " and " + object0.getName());
							computeCollision(gravityMapper.get(object1), object0, velocityMapper.get(object0), accelerationMapper.get(object0), energyMapper.get(object0), normal);
							return true;
						}

						logger.debug("Computing hurt between " + object0.getName() + " and " + object1.getName());
						computeCollision(collision0, object1, velocityMapper.get(object1), accelerationMapper.get(object1), energyMapper.get(object1), normal);
						logger.debug("Computing hurt between " + object1.getName() + " and " + object0.getName());
						computeCollision(collision1, object0, velocityMapper.get(object0), accelerationMapper.get(object0), energyMapper.get(object0), normal);
						return true;
					} catch (Exception e) {
						logger.error("Error on contact between " + object0.getName() + " and " + object1.getName() + ":" + e.getMessage());
						return false;
					}
				}


				private void computeRecharge(EnergyComponent energy, VelocityComponent velocity) {
					if (energy == null) return;
					energy.addCharge(RECHARGE_POWER * Gdx.graphics.getDeltaTime());
					if (velocity == null) return;
					velocity.getVelocity().scl(1f - Gdx.graphics.getDeltaTime() / 8f);
				}

				private void computeCollision(CollisionComponent collision,
											  GameObject object,
											  VelocityComponent velocity,
											  AccelerationComponent acceleration,
											  EnergyComponent energyComponent,
											  Vector3 normal) {
					// If the collision is starting
					if (!collision.collideWith(object)) {
						// Add entity to collision array
						logger.debug("Adding object to collisions");
						collision.addCollision(object);
						// If the entity has an energy component
						if (energyComponent != null) {
							// Activate shield
							logger.debug("Activating shield");
							energyComponent.hurt(velocity.getVelocity(), normal);
						}
						// If has a velocity
						if (velocity != null) {
							// Hurt velocity
							logger.debug("Hurting velocity");
							velocity.hurt(normal);
						}
					} else {
						if (velocity != null) {
							logger.debug("Velocity collide");
							velocity.collide(normal);
						}
					}
					if (acceleration != null) {
						acceleration.collide(normal);
					}
				}

				@Override
				public void onContactEnded(btCollisionObject collision0, btCollisionObject collision1) {
					try {
						object0 = (GameObject) collision0.userData;
						object1 = (GameObject) collision1.userData;
						endCollision(gravityMapper.get(object0), object1);
						endCollision(gravityMapper.get(object1), object0);
					} catch (Exception e) {
						logger.error("Error on contact ended");
						e.printStackTrace();
					}
				}

				private void endCollision(GravityComponent gravity, GameObject object) {
					if (gravity != null) {
						gravity.removeCollision(object);
					}
				}
		*/
	}
}
