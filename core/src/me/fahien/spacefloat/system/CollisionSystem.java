package me.fahien.spacefloat.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btManifoldPoint;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;

import me.fahien.spacefloat.component.AccelerationComponent;
import me.fahien.spacefloat.component.CollisionComponent;
import me.fahien.spacefloat.component.EnergyComponent;
import me.fahien.spacefloat.component.GraphicComponent;
import me.fahien.spacefloat.component.GravityComponent;
import me.fahien.spacefloat.component.RechargeComponent;
import me.fahien.spacefloat.component.VelocityComponent;

import static com.badlogic.ashley.core.ComponentMapper.getFor;
import static com.badlogic.ashley.core.Family.all;
import static me.fahien.spacefloat.game.SpaceFloatGame.logger;

/**
 * The Collision {@link IteratingSystem}
 *
 * @author Fahien
 */
public class CollisionSystem extends IteratingSystem {
	public static float RECHARGE_POWER = 0.125f;

	private ComponentMapper<GraphicComponent> gm = getFor(GraphicComponent.class);
	private ComponentMapper<VelocityComponent> vm = getFor(VelocityComponent.class);
	private ComponentMapper<AccelerationComponent> am = getFor(AccelerationComponent.class);
	private ComponentMapper<CollisionComponent> cm = getFor(CollisionComponent.class);
	private ComponentMapper<RechargeComponent> rm = getFor(RechargeComponent.class);
	private ComponentMapper<GravityComponent> grm = getFor(GravityComponent.class);
	private ComponentMapper<EnergyComponent> em = getFor(EnergyComponent.class);


	private btDefaultCollisionConfiguration collisionConfig;
	private btCollisionDispatcher dispatcher;

	class SpaceFloatContactListener extends ContactListener {
		private Vector3 normal;
		private Entity entity0;
		private Entity entity1;

		public SpaceFloatContactListener() {
			normal = new Vector3();
		}

		protected btCollisionObject collisionObject0;
		protected btCollisionObject collisionObject1;

		@Override
		public boolean onContactAdded(btManifoldPoint cp,
									  btCollisionObjectWrapper colObj0Wrap, int partId0, int index0,
									  btCollisionObjectWrapper colObj1Wrap, int partId1, int index1) {
			try {
				collisionObject0 = colObj0Wrap.getCollisionObject();
				collisionObject1 = colObj1Wrap.getCollisionObject();
				entity0 = (Entity) collisionObject0.userData;
				entity1 = (Entity) collisionObject1.userData;
				cp.getNormalWorldOnB(normal);
				if (collisionObject0 instanceof RechargeComponent) {
					logger.debug("Computing refuel between object0 and entity1");
					computeRecharge(em.get(entity1), vm.get(entity1));
					return true;
				}
				if (collisionObject1 instanceof RechargeComponent) {
					logger.debug("Computing refuel between entity1 and entity0");
					computeRecharge(em.get(entity0), vm.get(entity0));
					return true;
				}
				logger.debug("Computing collision between entity0 and entity1");
				computeCollision(grm.get(entity0), entity1, vm.get(entity1), am.get(entity1), em.get(entity1), normal);
				logger.debug("Computing collision between entity1 and entity0");
				computeCollision(grm.get(entity1), entity0, vm.get(entity0), am.get(entity0), em.get(entity0), normal);
				return true;
			} catch (Exception e) {
				logger.error("Error on contact added");
				e.printStackTrace();
				return false;
			}
		}

		/**
		 * Apply recharge logic
		 */
		private void computeRecharge(EnergyComponent energy, VelocityComponent velocity) {
			if (energy == null) return;
			energy.addCharge(RECHARGE_POWER * Gdx.graphics.getDeltaTime());
			if (velocity == null) return;
			velocity.getVelocity().scl(1f - Gdx.graphics.getDeltaTime() / 8f);
		}

		/**
		 * Apply Collision Logic
		 */
		private void computeCollision(GravityComponent gravity,
									  Entity entity,
									  VelocityComponent velocity,
									  AccelerationComponent acceleration,
									  EnergyComponent energyComponent,
									  Vector3 normal) {
			if (gravity != null) {
				if (!gravity.collideWith(entity)) {
					gravity.addCollision(entity);
					if (energyComponent != null) {
						energyComponent.hurt(velocity.getVelocity(), normal);
					}
					if (velocity != null) {
						velocity.hurt(normal);
					}
				} else {
					if (velocity != null) {
						velocity.collide(normal);
					}
				}
				if (acceleration != null) {
					acceleration.collide(normal);
				}
			}
		}

		@Override
		public void onContactEnded(btCollisionObject colObj0, btCollisionObject colObj1) {
			try {
				entity0 = (Entity) colObj0.userData;
				entity1 = (Entity) colObj1.userData;
				endCollision(grm.get(entity0), entity1);
				endCollision(grm.get(entity1), entity0);
			} catch (Exception e) {
				logger.error("Error on contact ended");
				e.printStackTrace();
			}
		}

		private void endCollision(GravityComponent gravity, Entity entity) {
			if (gravity != null) {
				gravity.removeCollision(entity);
			}
		}
	}

	private SpaceFloatContactListener contactListener;

	private btBroadphaseInterface broadphase;
	private btCollisionWorld collisionWorld;

	public CollisionSystem() {
		super(all(GraphicComponent.class, CollisionComponent.class).get());
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);

		createCollisionObjects(getEntities());
		ImmutableArray<Entity> refuelEntities = engine.getEntitiesFor(all(GraphicComponent.class, RechargeComponent.class).get());
		createRefuelCollisionObjects(refuelEntities);

		collisionConfig = new btDefaultCollisionConfiguration();
		dispatcher = new btCollisionDispatcher(collisionConfig);

		contactListener = new SpaceFloatContactListener();

		broadphase = new btDbvtBroadphase();
		collisionWorld = new btCollisionWorld(dispatcher, broadphase, collisionConfig);

		addToCollisionWorld(getEntities());
		addRefuelToCollisionWorld(refuelEntities);
	}

	/**
	 * Initialize every {@link CollisionComponent}
	 */
	protected void createCollisionObjects(ImmutableArray<Entity> entities) {
		for (Entity entity : entities) {
			CollisionComponent collision = cm.get(entity);
			GraphicComponent graphic = gm.get(entity);
			btCollisionObject collisionObject = new btCollisionObject();
			btCollisionShape shape = new btSphereShape(collision.getRadius());
			collision.setCollisionShape(shape);
			collision.setWorldTransform(graphic.getInstance().transform);
			collision.setCollisionFlags(collisionObject.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
			collision.userData = entity;
		}
	}

	/**
	 * Initialize every {@link CollisionComponent}
	 */
	protected void createRefuelCollisionObjects(ImmutableArray<Entity> entities) {
		for (Entity entity : entities) {
			RechargeComponent collision = rm.get(entity);
			GraphicComponent graphic = gm.get(entity);
			btCollisionObject collisionObject = new btCollisionObject();
			btCollisionShape shape = new btSphereShape(collision.getRadius());
			collision.setCollisionShape(shape);
			collision.setWorldTransform(graphic.getInstance().transform);
			collision.setCollisionFlags(collisionObject.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
			collision.userData = entity;
		}
	}

	/**
	 * Add collision objects to the {@link btCollisionWorld}
	 */
	private void addToCollisionWorld(ImmutableArray<Entity> entities) {
		for (Entity entity : entities) {
			CollisionComponent collision = cm.get(entity);
			collisionWorld.addCollisionObject(collision);
		}
	}

	/**
	 * Add refuel objects to the {@link btCollisionWorld}
	 */
	private void addRefuelToCollisionWorld(ImmutableArray<Entity> entities) {
		for (Entity entity : entities) {
			RechargeComponent collision = rm.get(entity);
			collisionWorld.addCollisionObject(collision);
		}
	}

	protected GraphicComponent m_graphic;
	protected CollisionComponent m_collision;
	protected RechargeComponent m_refuel;

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		// Get components
		m_graphic = gm.get(entity);
		m_collision = cm.get(entity);
		m_refuel = rm.get(entity);
		// Update collision and refuel position
		m_collision.setTransform(m_graphic.getInstance().transform);
		if (m_refuel != null) m_refuel.setTransform(m_graphic.getInstance().transform);

		collisionWorld.performDiscreteCollisionDetection();
	}

	@Override
	public void removedFromEngine(Engine engine) {
		super.removedFromEngine(engine);

		if (collisionWorld != null) collisionWorld.dispose();
		broadphase.dispose();

		contactListener.dispose();

		dispatcher.dispose();
		collisionConfig.dispose();

		for (Entity entity : engine.getEntitiesFor(all(CollisionComponent.class).get())) {
			cm.get(entity).dispose();
		}
	}
}
