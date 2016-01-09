package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import me.fahien.spacefloat.entity.GameObject;

import static com.badlogic.ashley.core.Family.all;
import static me.fahien.spacefloat.game.GdxTestRunner.logger;

/**
 * The {@link CollisionComponent} Test Case
 *
 * @author Fahien
 */
public class CollisionComponentTest {

	public static final String TEST_NAME = "TestEnergyStation";
	private Engine engine;

	@Before
	public void before() {
		engine = new Engine();
	}

	@Test
	public void aGameObjectCouldHaveACollisionComponentAndARechargeComponent() {
		GameObject energyStation = new GameObject();
		energyStation.setName(TEST_NAME);
		energyStation.add(new CollisionComponent());
		energyStation.add(new RechargeComponent());
		engine.addEntity(energyStation);
		for (Entity entity : engine.getEntitiesFor(all(CollisionComponent.class).get())) {
			logger.info(((GameObject) entity).getName());
		}
	}

	@After
	public void after() {
		engine.removeAllEntities();
	}
}
