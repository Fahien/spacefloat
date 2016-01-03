package me.fahien.spacefloat.factory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import org.junit.Before;
import org.junit.Test;

import me.fahien.spacefloat.component.AccelerationComponent;
import me.fahien.spacefloat.component.CollisionComponent;
import me.fahien.spacefloat.component.EnergyComponent;
import me.fahien.spacefloat.component.GraphicComponent;
import me.fahien.spacefloat.component.PlayerComponent;
import me.fahien.spacefloat.component.ReactorComponent;
import me.fahien.spacefloat.component.RefuelComponent;
import me.fahien.spacefloat.component.TransformComponent;
import me.fahien.spacefloat.component.VelocityComponent;
import me.fahien.spacefloat.entity.GameObject;
import me.fahien.spacefloat.component.GravityComponent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * The {@link GameObjectService} Test Case
 *
 * @author Fahien
 */
public class GameObjectServiceTest {
	private static final String SPACESHIP_NAME = "Spaceship";
	private static final String SPACESHIP_GRAPHIC = "cargo.g3db";
	private static final String SPACESHIP_REACTOR = "reactor.pfx";
	private static final float  SPACESHIP_RADIUS = 75f;
	private static final String EARTH_NAME = "Earth";
	private static final String ENERGYSTATION_NAME = "EnergyStation";
	private static final String ENERGYSTATION_GRAPHIC = "energy_station.g3db";
	private static final float  ENERGYSTATION_RADIUS = 100f;
	private static final float MASS_ZERO = 0f;

	private GameObjectService gameObjectService;

	@Before
	public void before() {
		gameObjectService = new GameObjectService();
	}

	@Test
	public void couldSaveTheSpaceship() {
		// Create the spaceship
		GameObject spaceship = new GameObject(SPACESHIP_NAME);
		// Create a graphic component
		GraphicComponent graphic = new GraphicComponent(SPACESHIP_GRAPHIC);
		spaceship.add(graphic);
		// Create a transform component
		TransformComponent position = new TransformComponent();
		spaceship.add(position);
		// Create a velocity component
		VelocityComponent velocity = new VelocityComponent();
		spaceship.add(velocity);
		// Create an acceleration component
		AccelerationComponent acceleration = new AccelerationComponent();
		spaceship.add(acceleration);
		// Create a player component
		PlayerComponent player = new PlayerComponent();
		spaceship.add(player);
		// Create a collision component
		CollisionComponent collision = new CollisionComponent(SPACESHIP_RADIUS);
		spaceship.add(collision);
		// Create a reactor component
		ReactorComponent reactorComponent = new ReactorComponent(SPACESHIP_REACTOR);
		spaceship.add(reactorComponent);
		// Create an energy component
		EnergyComponent energy = new EnergyComponent();
		spaceship.add(energy);
		// Save the spaceship
		gameObjectService.save(spaceship);
	}

	@Test
	public void couldSaveTheEnergyStation() {
		// Create the energy station
		GameObject energyStation = new GameObject(ENERGYSTATION_NAME);
		// Create a graphic component
		GraphicComponent graphic = new GraphicComponent(ENERGYSTATION_GRAPHIC);
		energyStation.add(graphic);
		// Create a transform component
		TransformComponent transform = new TransformComponent(new Vector3(1000f, 0f, 0f));
		energyStation.add(transform);
		// Create a velocity component
		VelocityComponent velocity = new VelocityComponent();
		energyStation.add(velocity);
		// Create a collision component
		CollisionComponent collision = new CollisionComponent(ENERGYSTATION_RADIUS);
		energyStation.add(collision);
		// Create a gravity component
		GravityComponent gravity = new GravityComponent(MASS_ZERO);
		energyStation.add(gravity);
		// Create e refuel component
		RefuelComponent refuel = new RefuelComponent();
		energyStation.add(refuel);
		// Save the energy station
		gameObjectService.save(energyStation);
	}

	@Test
	public void couldLoadTheSpaceship() {
		GameObject spaceship = gameObjectService.load(SPACESHIP_NAME);
		assertEquals("The name is not equals to " + SPACESHIP_NAME, SPACESHIP_NAME, spaceship.getName());
		GraphicComponent graphic = spaceship.getComponent(GraphicComponent.class);
		assertNotNull("The spaceship has no graphic component", graphic);
		assertEquals("The graphic name is not equals to " + SPACESHIP_GRAPHIC, SPACESHIP_GRAPHIC, graphic.getName());
		TransformComponent transform = spaceship.getComponent(TransformComponent.class);
		assertNotNull("The spaceship has no transform component", transform);
		VelocityComponent velocity = spaceship.getComponent(VelocityComponent.class);
		assertNotNull("The spaceship has no velocity component", velocity);
		PlayerComponent player = spaceship.getComponent(PlayerComponent.class);
		assertNotNull("The spaceship has no player component", player);
		AccelerationComponent acceleration = spaceship.getComponent(AccelerationComponent.class);
		assertNotNull("The spaceship has no acceleration component", acceleration);
		CollisionComponent collision = spaceship.getComponent(CollisionComponent.class);
		assertNotNull("The spaceship has no collision component", collision);
		ReactorComponent reactor = spaceship.getComponent(ReactorComponent.class);
		assertNotNull("The spaceship has no reactor component", reactor);
		EnergyComponent energy = spaceship.getComponent(EnergyComponent.class);
		assertNotNull("The spaceship has no energy component", energy);
	}

	@Test
	public void couldLoadTheEnergyStation() {
		GameObject energyStation = gameObjectService.load(ENERGYSTATION_NAME);
		assertEquals("The energy station name is not equals to " + ENERGYSTATION_NAME, ENERGYSTATION_NAME, energyStation.getName());
		GraphicComponent graphic = energyStation.getComponent(GraphicComponent.class);
		assertNotNull("The energy station has no graphic component", graphic);
		assertEquals("The energy station graphic name is not equals to " + ENERGYSTATION_GRAPHIC, ENERGYSTATION_GRAPHIC, graphic.getName());
		TransformComponent transform = energyStation.getComponent(TransformComponent.class);
		assertNotNull("The energy station has no transform component", transform);
		CollisionComponent collision = energyStation.getComponent(CollisionComponent.class);
		assertNotNull("The energy station has no collision component", collision);
		GravityComponent gravity = energyStation.getComponent(GravityComponent.class);
		assertNotNull("The energy station has no gravity component", gravity);
		RefuelComponent refuel = energyStation.getComponent(RefuelComponent.class);
		assertNotNull("The energy station has no refuel component", refuel);
	}

	@Test
	public void couldLoadTheEarth() {
		GameObject earth = gameObjectService.load(EARTH_NAME);
		assertEquals("The name is not equals to " + EARTH_NAME, EARTH_NAME, earth.getName());
		GravityComponent gravity = earth.getComponent(GravityComponent.class);
		assertNotNull("The spaceship has no gravity component", gravity);
	}

	/**
	 * Creates the {@link GameObject}s list
	 */
	private void createObjectList() {
		String objectList = "";
		FileHandle[] files = Gdx.files.local(GameObjectService.OBJECTS_DIR).list();
		for (FileHandle file : files) {
			if (file.path().endsWith(GameObjectService.JSON_EXT)) {
				objectList += file.nameWithoutExtension() + "\n";
			}
		}
		FileHandle fileList = Gdx.files.local(GameObjectService.OBJECT_LIST);
		fileList.writeString(objectList, false);
	}

	@Test
	public void couldLoadAllObjects() {
		createObjectList();
		Array<GameObject> objects = gameObjectService.loadObjects();
		assertNotNull("Objects array is null", objects);
		assertTrue("The array does not contain any object", objects.size > 0);
	}

	@Test
	public void couldSaveAllObjects() {
		createObjectList();
		Array<GameObject> objects = gameObjectService.loadObjects();
		gameObjectService.saveObjects(objects);
		objects = gameObjectService.loadObjects();
		assertNotNull("Objects array is null", objects);
		assertTrue("The array does not contain any object", objects.size > 0);
	}
}
