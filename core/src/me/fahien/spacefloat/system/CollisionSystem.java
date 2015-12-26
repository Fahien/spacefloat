package me.fahien.spacefloat.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
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

import me.fahien.spacefloat.component.CollisionComponent;
import me.fahien.spacefloat.component.GraphicComponent;
import me.fahien.spacefloat.component.VelocityComponent;
import me.fahien.spacefloat.entity.GameObject;

import static com.badlogic.ashley.core.ComponentMapper.getFor;

/**
 * The Collision {@link IteratingSystem}
 *
 * @author Fahien
 */
public class CollisionSystem extends IteratingSystem {

	private ComponentMapper<GraphicComponent> gm = getFor(GraphicComponent.class);
	private ComponentMapper<VelocityComponent> vm = getFor(VelocityComponent.class);
	private ComponentMapper<CollisionComponent> cm = getFor(CollisionComponent.class);

	private btDefaultCollisionConfiguration collisionConfig;
	private btCollisionDispatcher dispatcher;

	class SpaceFloatContactListener extends ContactListener {
		private VelocityComponent velocity;

		@Override
		public boolean onContactAdded(btManifoldPoint cp,
				btCollisionObjectWrapper colObj0Wrap, int partId0, int index0,
				btCollisionObjectWrapper colObj1Wrap, int partId1, int index1) {
			GameObject entity0 = (GameObject) colObj0Wrap.getCollisionObject().userData;
			GameObject entity1 = (GameObject) colObj1Wrap.getCollisionObject().userData;
			velocity = vm.get(entity0);
			if (velocity != null) {
				velocity.collision();
			}
			velocity = vm.get(entity1);
			if (velocity != null) {
				velocity.collision();
			}

			return true;
		}
	}

	private SpaceFloatContactListener contactListener;

	private btBroadphaseInterface broadphase;
	private btCollisionWorld collisionWorld;

	public CollisionSystem() {
		super(Family.all(GraphicComponent.class, CollisionComponent.class).get());
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);

		createCollisionObjects(getEntities());

		collisionConfig = new btDefaultCollisionConfiguration();
		dispatcher = new btCollisionDispatcher(collisionConfig);

		contactListener = new SpaceFloatContactListener();

		broadphase = new btDbvtBroadphase();
		collisionWorld = new btCollisionWorld(dispatcher, broadphase, collisionConfig);

		addToCollisionWorld(getEntities());
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
	 * Add collision objects to the {@link btCollisionWorld}
	 */
	private void addToCollisionWorld(ImmutableArray<Entity> entities) {
		for (Entity entity : entities) {
			CollisionComponent collision = cm.get(entity);
			collisionWorld.addCollisionObject(collision);
		}
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		GraphicComponent graphic = gm.get(entity);
		CollisionComponent collision = cm.get(entity);
		collision.setTransform(graphic.getInstance().transform);

		collisionWorld.performDiscreteCollisionDetection();
	}

	@Override
	public void removedFromEngine(Engine engine) {
		super.removedFromEngine(engine);

		collisionWorld.dispose();
		broadphase.dispose();

		contactListener.dispose();

		dispatcher.dispose();
		collisionConfig.dispose();

		for (Entity entity : engine.getEntitiesFor(Family.all(CollisionComponent.class).get())) {
			cm.get(entity).dispose();
		}
	}
}
