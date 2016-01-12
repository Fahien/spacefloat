package me.fahien.spacefloat.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
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

import me.fahien.spacefloat.component.CollisionComponent;
import me.fahien.spacefloat.component.GraphicComponent;
import me.fahien.spacefloat.component.GravityComponent;
import me.fahien.spacefloat.component.MissionComponent;
import me.fahien.spacefloat.component.RechargeComponent;
import me.fahien.spacefloat.component.RigidbodyComponent;
import me.fahien.spacefloat.entity.GameObject;

import static com.badlogic.ashley.core.Family.all;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.collisionMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.graphicMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.gravityMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.missionMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.rechargeMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.rigidMapper;
import static me.fahien.spacefloat.game.SpaceFloatGame.logger;

/**
 * The Collision {@link EntitySystem}
 *
 * @author Fahien
 */
public class BulletSystem extends EntitySystem {
	private static final int COLLISION_PRIORITY = 1;
	public static float RECHARGE_POWER = 0.125f;

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

		collisionConfig = new btDefaultCollisionConfiguration();
		dispatcher = new btCollisionDispatcher(collisionConfig);

		broadphase = new btDbvtBroadphase();
		constraintSolver = new btSequentialImpulseConstraintSolver();
		dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, constraintSolver, collisionConfig);

		contactListener = new SpaceFloatContactListener();

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
		dynamicsWorld.stepSimulation(delta, 5, 0.000001f);
		for (Entity entity : rigidEntities) {
			// Update rigid body after world simulation
			rigidMapper.get(entity).update();
		}
	}

	/**
	 * Updates {@link btCollisionObject}s transform
	 */
	protected void updateCollisionObjects(CollisionComponent collision, GraphicComponent graphic) {
		collision.setTransform(graphic.getTransform());
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

		for (Entity entity: collisionEntities) {
			collisionMapper.get(entity).dispose();
		}
		for (Entity entity : gravityEntities) {
			gravityMapper.get(entity).dispose();
		}
		for (Entity entity : rechargeEntities) {
			rechargeMapper.get(entity).dispose();
		}
		for (Entity entity : missionEntities) {
			missionMapper.get(entity).dispose();
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
		public boolean onContactAdded(btManifoldPoint collisionPoint, btCollisionObject collision0, int partId0, int index0, btCollisionObject collision1, int partId1, int index1) {
			if (collision0 instanceof CollisionComponent) {
				handleCollision(collisionPoint, (CollisionComponent) collision0, (GameObject) collision0.userData, (GameObject) collision1.userData);
			}
			if (collision1 instanceof CollisionComponent) {
				handleCollision(collisionPoint, (CollisionComponent) collision1, (GameObject) collision1.userData, (GameObject) collision0.userData);
			}
			return true;
		}

		private void handleCollision(btManifoldPoint collisionPoint, CollisionComponent collision, GameObject source, GameObject target) {
			collision.collideWith(m_delta, collisionPoint, source, target);
		}
	}
}
