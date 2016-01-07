package me.fahien.spacefloat.component;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * The {@link RigidbodyComponent} Test Case
 *
 * @author Fahien
 */
public class RigidbodyComponentTest {

	private RigidbodyComponent rigidbody;

	@Before
	public void before() {
		rigidbody = new RigidbodyComponent();
		Bullet.init();
	}

	@Test
	public void couldCreateAShape() {
		rigidbody.createShape();
		assertNotNull("The rigidbody has no shape", rigidbody.getShape());
	}

	@Test
	public void couldCalculateLocalInertia() {
		rigidbody.calculateLocalInertia();
		Assert.assertEquals("Local inertia is not ZERO", Vector3.Zero, rigidbody.getLocalInertia());
		rigidbody.setMass(1f);
		rigidbody.calculateLocalInertia();
		Assert.assertEquals("Local inertia is not ZERO", Vector3.Zero, rigidbody.getLocalInertia());
		btBoxShape shape = new btBoxShape(new Vector3(1f, 1f, 1f));
		rigidbody.setShape(shape);
		rigidbody.calculateLocalInertia();
		Assert.assertNotEquals("Local inertia is ZERO", Vector3.Zero, rigidbody.getLocalInertia());
	}

	@Test
	public void couldCreateConstructionInfo() {
		rigidbody.setMass(1f);
		btBoxShape shape = new btBoxShape(new Vector3(1f, 1f, 1f));
		rigidbody.setShape(shape);
		rigidbody.createConstructionInfo();
		assertNotNull("Construction Info is null", rigidbody.getConstructionInfo());
	}

	@Test
	public void couldCreateRigidbody() {
		rigidbody.setMass(1f);
		btBoxShape shape = new btBoxShape(new Vector3(1f, 1f, 1f));
		rigidbody.setShape(shape);
		rigidbody.createRigidbody();
		assertNotNull("The rigidbody is null", rigidbody.getRigidbody());
	}

	@Test
	public void couldSetTransform() {
		rigidbody.createRigidbody();
		rigidbody.setMotionStateTransform(new Matrix4());
		assertNotNull("The rigidbody is null", rigidbody.getRigidbody());
		assertNotNull("The motion state is null", rigidbody.getRigidbody().getMotionState());
	}

	@Test
	public void couldApplyAForce() {
		rigidbody.createRigidbody(new Matrix4());
		rigidbody.getRigidbody().applyCentralForce(new Vector3(0f, 0f, -1f));
	}

	@After
	public void after() {
		rigidbody.dispose();
		btCollisionShape shape = rigidbody.getShape();
		if (shape != null) {
			assertTrue("The shape is not disposed", shape.isDisposed());
		}
		btRigidBody.btRigidBodyConstructionInfo constructionInfo = rigidbody.getConstructionInfo();
		if (constructionInfo != null) {
			assertTrue("The construction info is not disposed", constructionInfo.isDisposed());
		}
		btRigidBody rigidBody = rigidbody.getRigidbody();
		if (rigidBody != null) {
			assertTrue("The rigidbody is not disposed", rigidBody.isDisposed());
		}
		RigidbodyComponent.SpaceFloatMotionState motionState = rigidbody.getMotionState();
		if (motionState != null) {
			assertTrue("The motion state is not disposed", motionState.isDisposed());
		}
	}
}
