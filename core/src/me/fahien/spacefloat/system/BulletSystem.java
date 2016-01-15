package me.fahien.spacefloat.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btManifoldPoint;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.utils.ObjectSet;

import me.fahien.spacefloat.component.CollisionComponent;
import me.fahien.spacefloat.component.DestinationComponent;
import me.fahien.spacefloat.component.EnergyComponent;
import me.fahien.spacefloat.component.GraphicComponent;
import me.fahien.spacefloat.component.GravityComponent;
import me.fahien.spacefloat.component.MissionComponent;
import me.fahien.spacefloat.component.MoneyComponent;
import me.fahien.spacefloat.component.RechargeComponent;
import me.fahien.spacefloat.component.RigidbodyComponent;
import me.fahien.spacefloat.component.TransformComponent;
import me.fahien.spacefloat.component.VelocityComponent;
import me.fahien.spacefloat.entity.GameObject;
import me.fahien.spacefloat.factory.MissionFactory;
import me.fahien.spacefloat.game.SpaceFloat;
import me.fahien.spacefloat.game.SpaceFloatGame;
import me.fahien.spacefloat.mission.Mission;

import static com.badlogic.ashley.core.Family.all;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.collisionMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.destinationMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.energyMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.graphicMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.gravityMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.missionMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.moneyMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.rechargeMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.rigidMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.transformMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.velocityMapper;
import static me.fahien.spacefloat.game.SpaceFloatGame.logger;

/**
 * The Collision {@link EntitySystem}
 *
 * @author Fahien
 */
public class BulletSystem extends EntitySystem {
	private static final int COLLISION_PRIORITY = 1;

	private ObjectSet<CollisionComponent> collisionComponents;
	private ObjectSet<RigidbodyComponent> rigidbodyComponents;

	private ImmutableArray<Entity> collisionEntities;
	private ImmutableArray<Entity> gravityEntities;
	private ImmutableArray<Entity> rechargeEntities;
	private ImmutableArray<Entity> missionEntities;
	private ImmutableArray<Entity> rigidEntities;

	private btDefaultCollisionConfiguration collisionConfig;
	private btCollisionDispatcher dispatcher;

	private btBroadphaseInterface broadphase;
	private btConstraintSolver constraintSolver;
	private btDynamicsWorld dynamicsWorld;

	private SpaceFloatContactListener contactListener;

	public BulletSystem() {
		super(COLLISION_PRIORITY);
		collisionComponents = new ObjectSet<>();
		rigidbodyComponents = new ObjectSet<>();
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);

		collisionEntities = engine.getEntitiesFor(all(GraphicComponent.class, CollisionComponent.class).get());
		createCollisionObjects(collisionEntities, CollisionComponent.class);

		gravityEntities = engine.getEntitiesFor(all(GraphicComponent.class, GravityComponent.class).get());
		createCollisionObjects(gravityEntities, GravityComponent.class);

		rechargeEntities = engine.getEntitiesFor(all(GraphicComponent.class, RechargeComponent.class).get());
		createCollisionObjects(rechargeEntities, RechargeComponent.class);

		missionEntities = engine.getEntitiesFor(all(GraphicComponent.class, MissionComponent.class).get());
		createCollisionObjects(missionEntities, MissionComponent.class);

		rigidEntities = engine.getEntitiesFor(all(GraphicComponent.class, RigidbodyComponent.class).get());
		createRigidbodies(rigidEntities, RigidbodyComponent.class);

		if (collisionConfig == null) collisionConfig = new btDefaultCollisionConfiguration();
		if (dispatcher == null) dispatcher = new btCollisionDispatcher(collisionConfig);

		if (broadphase == null) broadphase = new btDbvtBroadphase();
		if (constraintSolver == null) constraintSolver = new btSequentialImpulseConstraintSolver();
		if (dynamicsWorld == null) dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, constraintSolver, collisionConfig);

		if (contactListener == null) contactListener = new SpaceFloatContactListener();

		addCollisionObjectsToDynamicsWorld(collisionEntities, CollisionComponent.class);
		addCollisionObjectsToDynamicsWorld(gravityEntities, GravityComponent.class);
		addCollisionObjectsToDynamicsWorld(rechargeEntities, RechargeComponent.class);
		addCollisionObjectsToDynamicsWorld(missionEntities, MissionComponent.class);

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
			collisionComponent.setTransform(graphic.getTransform());
			// Set user data
			collisionComponent.getCollisionObject().userData = entity;
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
			VelocityComponent velocity = velocityMapper.get(entity);
			// Set linear velocity
			rigidbodyComponent.getRigidbody().setLinearVelocity(velocity.getVelocity());
			// Set angular velocity
			rigidbodyComponent.getRigidbody().setAngularVelocity(velocity.getAngularVelocity());
			// Set user data
			rigidbodyComponent.getRigidbody().userData = entity;
			// Update rigidbody
			rigidbodyComponent.update();
			// Link graphic transform to rigid body transform
			graphic.setTransform(rigidbodyComponent.getTransform());
		}
	}

	/**
	 * Adds {@link btCollisionObject}s to the {@link btDynamicsWorld}
	 */
	private void addCollisionObjectsToDynamicsWorld(ImmutableArray<Entity> entities, Class<? extends CollisionComponent> collisionType) {
		for (Entity entity : entities) {
			CollisionComponent collisionComponent = entity.getComponent(collisionType);
			collisionComponents.add(collisionComponent);
			dynamicsWorld.addCollisionObject(collisionComponent.getCollisionObject(), collisionComponent.getGroup(), collisionComponent.getMask());
		}
	}

	/**
	 * Adds {@link btRigidBody}s to the {@link btDynamicsWorld}
	 */
	private void addRigidbodiesToDynamicsWorld(ImmutableArray<Entity> entities, Class<? extends RigidbodyComponent> rigidbodyType) {
		for (Entity entity : entities) {
			RigidbodyComponent rigidbodyComponent = entity.getComponent(rigidbodyType);
			rigidbodyComponents.add(rigidbodyComponent);
			dynamicsWorld.addRigidBody(rigidbodyComponent.getRigidbody(), rigidbodyComponent.getGroup(), rigidbodyComponent.getMask());
		}
	}

	protected float m_delta;

	@Override
	public void update(float delta) {
		m_delta = delta;

		for (Entity entity : collisionEntities) {
			updateCollisionObjects(collisionMapper.get(entity), graphicMapper.get(entity));
		}
		for (Entity entity : gravityEntities) {
			updateCollisionObjects(gravityMapper.get(entity), graphicMapper.get(entity));
		}
		for (Entity entity : rechargeEntities) {
			updateCollisionObjects(rechargeMapper.get(entity), graphicMapper.get(entity));
		}
		for (Entity entity : missionEntities) {
			updateCollisionObjects(missionMapper.get(entity), graphicMapper.get(entity));
		}
		try {
			dynamicsWorld.stepSimulation(delta, 5, 0.000001f);
		} catch (Exception e) {
			logger.error("Error while simulating dynamics world: " + e.getMessage());
			Gdx.app.exit();
		}
		for (Entity entity : rigidEntities) {
			// Update rigid body after world simulation
			updateRigidbody(rigidMapper.get(entity), transformMapper.get(entity));
		}
	}

	private void updateRigidbody(RigidbodyComponent rigidbodyComponent, TransformComponent transformComponent) {
		rigidbodyComponent.update();
		rigidbodyComponent.getPosition(transformComponent.getPosition());
	}

	/**
	 * Updates {@link btCollisionObject}s transform
	 */
	protected void updateCollisionObjects(CollisionComponent collision, GraphicComponent graphic) {
		collision.setTransform(graphic.getTransform());
	}

	@Override
	public void removedFromEngine(Engine engine) {
		if (dynamicsWorld != null) {
			for (CollisionComponent collision : collisionComponents) {
				logger.debug("Removing collision: " + ((GameObject) collision.getCollisionObject().userData).getName());
				dynamicsWorld.removeCollisionObject(collision.getCollisionObject());
			}
			for (RigidbodyComponent rigidbody : rigidbodyComponents) {
				logger.debug("Removing rigidbody: " + ((GameObject) rigidbody.getRigidbody().userData).getName());
				dynamicsWorld.removeRigidBody(rigidbody.getRigidbody());
			}
		}
	}

	/**
	 * Disposes collision objects and rigidbodies
	 */
	public void disposeCollisionsAndRigidbodies() {
		for (CollisionComponent collision : collisionComponents) {
			if (collision != null && collision.getCollisionObject() != null && !collision.getCollisionObject().isDisposed()) {
				dynamicsWorld.removeCollisionObject(collision.getCollisionObject());
				logger.debug("Disposing collision: " + ((GameObject) collision.getCollisionObject().userData).getName());
				collision.dispose();
				collisionComponents.remove(collision);
			}
		}
		for (RigidbodyComponent rigidbody : rigidbodyComponents) {
			if (rigidbody != null && rigidbody.getRigidbody() != null && !rigidbody.getRigidbody().isDisposed()) {
				dynamicsWorld.removeRigidBody(rigidbody.getRigidbody());
				logger.debug("Disposing rigidbody: " + ((GameObject) rigidbody.getRigidbody().userData).getName());
				rigidbody.dispose();
				rigidbodyComponents.remove(rigidbody);
			}
		}
	}

	/**
	 * Disposes resources
	 */
	public void dispose() {
		disposeCollisionsAndRigidbodies();
		if (dynamicsWorld != null && !dynamicsWorld.isDisposed()) {
			dynamicsWorld.dispose();
		}
		if (constraintSolver != null && !constraintSolver.isDisposed()) {
			constraintSolver.dispose();
		}
		if (broadphase != null && !broadphase.isDisposed()) {
			broadphase.dispose();
		}
		if (contactListener != null && !contactListener.isDisposed()) {
			contactListener.dispose();
		}
		if (dispatcher != null && !dispatcher.isDisposed()) {
			dispatcher.dispose();
		}
		if (collisionConfig != null && !collisionConfig.isDisposed()) {
			collisionConfig.dispose();
		}
	}

	/**
	 * The Space Float {@link ContactListener}
	 *
	 * @author Fahien
	 */
	class SpaceFloatContactListener extends ContactListener {

		@Override
		public boolean onContactAdded(btManifoldPoint collisionPoint, btCollisionObject collision0, int partId0, int index0, btCollisionObject collision1, int partId1, int index1) {
			try {
				GameObject source = (GameObject) collision0.userData;
				GameObject target = (GameObject) collision1.userData;

				// Energy shield
				CollisionComponent collision = collisionMapper.get(source);
				EnergyComponent energy = energyMapper.get(target);
				if (collision != null && collision.getCollisionObject() == collision0) {
					triggerShield(collisionPoint, collision, energy);
				}

				// Energy recharge
				RechargeComponent recharge = rechargeMapper.get(source);
				if (recharge != null && recharge.getCollisionObject() == collision0) {
					triggerRecharge(recharge, energy);
				}

				// Gravity attraction
				GravityComponent gravity = gravityMapper.get(source);
				RigidbodyComponent rigidbody = rigidMapper.get(target);
				if (gravity != null && gravity.getCollisionObject() == collision0) {
					triggerGravity(gravity, rigidbody);
				}

				// Mission logic
				MissionComponent mission = missionMapper.get(source);
				if (mission != null && mission.getCollisionObject() == collision0) {
					triggerMission(mission, source, target);
				}
				mission = missionMapper.get(target);
				if (mission != null && mission.getCollisionObject() == collision1) {
					triggerMission(mission, target, source);
				}
			} catch (Exception e) {
				logger.error("Error during collision callback: " + e.getMessage());
				Gdx.app.exit();
			}
			return true;
		}

		private void triggerMission(MissionComponent missionComponent, GameObject source, GameObject target) {
			Mission mission = missionComponent.getMission();
			// If is not collected and collide with Player
			if (!mission.isCollected() && target.isPlayer()) {
				// Decrease hangling time
				missionComponent.addHandlingTime(-m_delta);

				if (missionComponent.getHandlingTime() <= 0f) {
					// Reset handling time
					missionComponent.resetHandlingTime();
					// Set collected
					mission.setCollected(true);
					// Remove this component from the parcel source
					source.remove(MissionComponent.class);

					SpaceFloatGame game = SpaceFloat.GAME.getGame();
					Engine engine = game.getEngine();
					// Remove the parcel from the engine
					engine.removeEntity(source);
					// Update player destination component
					for (Entity entity : engine.getEntities()) {
						if (((GameObject) entity).getName().equals(mission.getDestination())) {
							Vector3 position = new Vector3();
							collisionMapper.get(entity).getPosition(position);
							// Set the destination
							DestinationComponent destinationComponent = destinationMapper.get(target);
							destinationComponent.setPosition(position);
							destinationComponent.setName(mission.getDestination());
						}
					}
					// Update user data
					missionComponent.getCollisionObject().userData = target;
					// Add this component to the player
					target.add(missionComponent);
					// Enqueue first message
					game.enqueueMessage(mission.getMessageInitial());
				}
			} else if (!mission.isDelivered() && mission.getDestination().equals(target.getName())) {
				// Update handling time
				missionComponent.addHandlingTime(-m_delta);

				if (missionComponent.getHandlingTime() <= 0f) {
					// Reset handling time
					missionComponent.resetHandlingTime();
					// Set delivered
					mission.setDelivered(true);
					// Remove this component from the player
					SpaceFloatGame game = SpaceFloat.GAME.getGame();
					GameObject player = game.getPlayer();
					player.remove(MissionComponent.class);
					// Add reward
					MoneyComponent moneyComponent = moneyMapper.get(player);
					moneyComponent.addMoney(mission.getReward());
					// Reset the destination
					DestinationComponent destinationComponent = destinationMapper.get(player);
					destinationComponent.setName(null);
					// Enqueue last message
					game.enqueueMessage(mission.getMessageEnding());
					// Load next mission
					MissionFactory.INSTANCE.loadNextMission();
				}
			}
		}

		protected Vector3 m_gravityPosition = new Vector3();
		protected Vector3 m_rigidbodyPosition = new Vector3();

		private void triggerGravity(GravityComponent gravity, RigidbodyComponent rigidbody) {
			if (rigidbody != null) {
				// Set gravity vector equal to this collision object position
				gravity.getPosition(m_gravityPosition);
				// Get the rigid body position
				rigidbody.getPosition(m_rigidbodyPosition);
				// Compute the attractive vector
				m_gravityPosition.sub(m_rigidbodyPosition).nor().scl(GravityComponent.G * gravity.getMass());
				// Apply attraction
				rigidbody.applyCentralForce(m_gravityPosition);
			}
		}

		private void triggerRecharge(RechargeComponent recharge, EnergyComponent energy) {
			if (recharge != null && energy != null) {
				energy.recharge();
			}
		}

		private void triggerShield(btManifoldPoint point, CollisionComponent collision, EnergyComponent energy) {
			if (collision != null && energy != null) {
				energy.hurt(point.getAppliedImpulse());
			}
		}
	}
}
