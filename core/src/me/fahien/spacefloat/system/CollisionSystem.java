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
import me.fahien.spacefloat.component.HurtComponent;
import me.fahien.spacefloat.component.RechargeComponent;
import me.fahien.spacefloat.component.VelocityComponent;
import me.fahien.spacefloat.entity.GameObject;

import static com.badlogic.ashley.core.Family.all;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.accelerationMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.energyMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.graphicMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.gravityMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.hurtMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.rechargeMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.velocityMapper;
import static me.fahien.spacefloat.game.SpaceFloatGame.logger;

/**
 * The Collision {@link EntitySystem}
 *
 * @author Fahien
 */
public class CollisionSystem extends EntitySystem {
	public static float RECHARGE_POWER = 0.125f;

	private ImmutableArray<Entity> gravityEntities;
	private ImmutableArray<Entity> rechargeEntities;
	private ImmutableArray<Entity> hurtEntities;

	private btDefaultCollisionConfiguration collisionConfig;
	private btCollisionDispatcher dispatcher;

	class SpaceFloatContactListener extends ContactListener {
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
	}

	private SpaceFloatContactListener contactListener;

	private btBroadphaseInterface broadphase;
	private btCollisionWorld collisionWorld;

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);

		gravityEntities = engine.getEntitiesFor(all(GraphicComponent.class, GravityComponent.class).get());
		createCollisionObjects(gravityEntities, GravityComponent.class);

		rechargeEntities = engine.getEntitiesFor(all(GraphicComponent.class, RechargeComponent.class).get());
		createCollisionObjects(rechargeEntities, RechargeComponent.class);

		hurtEntities = engine.getEntitiesFor(all(GraphicComponent.class, HurtComponent.class).get());
		createCollisionObjects(hurtEntities, HurtComponent.class);

		collisionConfig = new btDefaultCollisionConfiguration();
		dispatcher = new btCollisionDispatcher(collisionConfig);

		contactListener = new SpaceFloatContactListener();

		broadphase = new btDbvtBroadphase();
		collisionWorld = new btCollisionWorld(dispatcher, broadphase, collisionConfig);

		addToCollisionWorld(gravityEntities, GravityComponent.class);
		addToCollisionWorld(rechargeEntities, RechargeComponent.class);
		addToCollisionWorld(hurtEntities, HurtComponent.class);
	}

	/**
	 * Initialize every {@link CollisionComponent}
	 */
	protected void createCollisionObjects(ImmutableArray<Entity> entities, Class<? extends CollisionComponent> collisionType) {
		for (Entity entity : entities) {
			CollisionComponent collision = entity.getComponent(collisionType);
			GraphicComponent graphic = graphicMapper.get(entity);
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
	private void addToCollisionWorld(ImmutableArray<Entity> entities, Class<? extends CollisionComponent> collisionType) {
		for (Entity entity : entities) {
			CollisionComponent collision = entity.getComponent(collisionType);
			collisionWorld.addCollisionObject(collision);
		}
	}

	@Override
	public void update(float deltaTime) {
		for (Entity entity : gravityEntities) {
			updateCollisionPosition(gravityMapper.get(entity), graphicMapper.get(entity));
		}
		for (Entity entity : rechargeEntities) {
			updateCollisionPosition(rechargeMapper.get(entity), graphicMapper.get(entity));
		}
		for (Entity entity : hurtEntities) {
			updateCollisionPosition(hurtMapper.get(entity), graphicMapper.get(entity));
		}

		collisionWorld.performDiscreteCollisionDetection();
	}

	protected void updateCollisionPosition(CollisionComponent collision, GraphicComponent graphic) {
		collision.setTransform(graphic.getInstance().transform);
	}

	@Override
	public void removedFromEngine(Engine engine) {
		super.removedFromEngine(engine);

		if (collisionWorld != null) collisionWorld.dispose();
		broadphase.dispose();

		contactListener.dispose();

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
	}
}
