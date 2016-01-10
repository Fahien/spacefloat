package me.fahien.spacefloat.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import org.junit.Before;
import org.junit.Test;

import me.fahien.spacefloat.component.AccelerationComponent;
import me.fahien.spacefloat.component.EnergyComponent;
import me.fahien.spacefloat.component.GraphicComponent;
import me.fahien.spacefloat.component.GravityComponent;
import me.fahien.spacefloat.component.MissionComponent;
import me.fahien.spacefloat.component.PlayerComponent;
import me.fahien.spacefloat.component.ReactorComponent;
import me.fahien.spacefloat.component.RechargeComponent;
import me.fahien.spacefloat.component.RigidbodyComponent;
import me.fahien.spacefloat.component.TransformComponent;
import me.fahien.spacefloat.component.VelocityComponent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * The {@link GameObjectFactory} Test Case
 *
 * @author Fahien
 */
public class GameObjectFactoryTest {
	private static final String SPACESHIP_NAME = "Spaceship";
	private static final String SPACESHIP_GRAPHIC = "cargo.g3db";
	private static final String SPACESHIP_REACTOR = "reactor.pfx";
	private static final float SPACESHIP_MASS = 1f;
	private static final float SPACESHIP_RADIUS = 80f;
	private static final short SPACESHIP_GROUP = 1;
	private static final Vector3 SPACESHIP_POSITION = new Vector3(1700,0,1700);

	private static final String EARTH_NAME = "Earth";

	private static final String ENERGY_STATION_NAME = "EnergyStation";
	private static final String ENERGY_STATION_GRAPHIC = "energy_station.g3db";
	public static final float ENERGY_STATION_MASS = 10f;
	public static final float ENERGY_STATION_RADIUS = 100f;

	private static final String PARCEL_ONE_NAME = "ParcelOne";
	private static final String PARCEL_ONE_GRAPHIC = "parcel.g3db";
	private static final String PARCEL_ONE_DESTINATION = "Earth";


	private GameObjectFactory gameObjectFactory;

	@Before
	public void before() {
		gameObjectFactory = new GameObjectFactory();
	}

	@Test
	public void couldSaveTheSpaceship() {
		// Create the spaceship
		GameObject spaceship = new GameObject(SPACESHIP_NAME);
		// Create a graphic component
		GraphicComponent graphic = new GraphicComponent(SPACESHIP_GRAPHIC);
		spaceship.add(graphic);
		// Create a transform component
		TransformComponent position = new TransformComponent(SPACESHIP_POSITION);
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
		// Create a rigid component
		RigidbodyComponent rigidbody = new RigidbodyComponent(
				SPACESHIP_MASS,
				SPACESHIP_RADIUS,
				SPACESHIP_GROUP,
				(short) (2|4|8)); // 2 - Object + 4 - Planet + 8 - Event
		spaceship.add(rigidbody);
		// Create a reactor component
		ReactorComponent reactorComponent = new ReactorComponent(SPACESHIP_REACTOR);
		spaceship.add(reactorComponent);
		// Create an energy component
		EnergyComponent energy = new EnergyComponent();
		spaceship.add(energy);
		// Save the spaceship
		gameObjectFactory.save(spaceship);
	}

	@Test
	public void couldSaveTheEnergyStation() {
		// Create the energy station
		GameObject energyStation = new GameObject(ENERGY_STATION_NAME);
		// Create a graphic component
		GraphicComponent graphic = new GraphicComponent(ENERGY_STATION_GRAPHIC);
		energyStation.add(graphic);
		// Create a transform component
		TransformComponent transform = new TransformComponent(new Vector3(-2000f, 0f, -3000f));
		energyStation.add(transform);
		// Create a velocity component
		VelocityComponent velocity = new VelocityComponent();
		energyStation.add(velocity);
		// Create a hurt component
		RigidbodyComponent rigidbody = new RigidbodyComponent(
				ENERGY_STATION_MASS,
				ENERGY_STATION_RADIUS,
				(short)2, // Object
				(short)(1|2|4)); // Player | Object | Planet
		energyStation.add(rigidbody);
		// Create e recharge component
		RechargeComponent recharge = new RechargeComponent();
		energyStation.add(recharge);
		// Save the energy station
		gameObjectFactory.save(energyStation);
	}

	@Test
	public void couldSaveTheParcelOne() {
		GameObject parcelOne = new GameObject(PARCEL_ONE_NAME);
		// Create a graphic component
		GraphicComponent graphic = new GraphicComponent(PARCEL_ONE_GRAPHIC);
		parcelOne.add(graphic);
		// Create a transform component
		TransformComponent transform = new TransformComponent(new Vector3(1740,0,2310));
		parcelOne.add(transform);
		// Create a mission component
		MissionComponent mission = new MissionComponent("Earth");
		parcelOne.add(mission);
		// Save the parce
		gameObjectFactory.save(parcelOne);
	}

	@Test
	public void couldLoadTheSpaceship() {
		GameObject spaceship = gameObjectFactory.load(SPACESHIP_NAME);
		assertEquals("The name is not equal to " + SPACESHIP_NAME, SPACESHIP_NAME, spaceship.getName());
		GraphicComponent graphic = spaceship.getComponent(GraphicComponent.class);
		assertNotNull("The spaceship has no graphic component", graphic);
		assertEquals("The graphic name is not equal to " + SPACESHIP_GRAPHIC, SPACESHIP_GRAPHIC, graphic.getName());
		TransformComponent transform = spaceship.getComponent(TransformComponent.class);
		assertNotNull("The spaceship has no transform component", transform);
		VelocityComponent velocity = spaceship.getComponent(VelocityComponent.class);
		assertNotNull("The spaceship has no velocity component", velocity);
		PlayerComponent player = spaceship.getComponent(PlayerComponent.class);
		assertNotNull("The spaceship has no player component", player);
		AccelerationComponent acceleration = spaceship.getComponent(AccelerationComponent.class);
		assertNotNull("The spaceship has no acceleration component", acceleration);
		RigidbodyComponent rigidbody = spaceship.getComponent(RigidbodyComponent.class);
		assertNotNull("The spaceship has no rigid component", rigidbody);
		ReactorComponent reactor = spaceship.getComponent(ReactorComponent.class);
		assertNotNull("The spaceship has no reactor component", reactor);
		EnergyComponent energy = spaceship.getComponent(EnergyComponent.class);
		assertNotNull("The spaceship has no energy component", energy);
	}

	@Test
	public void couldLoadTheEnergyStation() {
		GameObject energyStation = gameObjectFactory.load(ENERGY_STATION_NAME);
		assertEquals("The energy station name is not equal to " + ENERGY_STATION_NAME, ENERGY_STATION_NAME, energyStation.getName());
		GraphicComponent graphic = energyStation.getComponent(GraphicComponent.class);
		assertNotNull("The energy station has no graphic component", graphic);
		assertEquals("The energy station graphic name is not equal to " + ENERGY_STATION_GRAPHIC, ENERGY_STATION_GRAPHIC, graphic.getName());
		TransformComponent transform = energyStation.getComponent(TransformComponent.class);
		assertNotNull("The energy station has no transform component", transform);
		VelocityComponent velocity = energyStation.getComponent(VelocityComponent.class);
		assertNotNull("The energy station has no velocity component", velocity);
		RigidbodyComponent rigidbody = energyStation.getComponent(RigidbodyComponent.class);
		assertNotNull("The energy station has no rigidbody component", rigidbody);
		RechargeComponent refuel = energyStation.getComponent(RechargeComponent.class);
		assertNotNull("The energy station has no refuel component", refuel);
	}

	@Test
	public void couldLoadTheParcelOne() {
		GameObject parcelOne = gameObjectFactory.load(PARCEL_ONE_NAME);
		assertEquals("The parcel name is not equal to " + PARCEL_ONE_NAME, PARCEL_ONE_NAME, parcelOne.getName());
		GraphicComponent graphic = parcelOne.getComponent(GraphicComponent.class);
		assertNotNull("The parcel has no graphic component", graphic);
		assertEquals("The parcel graphic name is not equal to " + PARCEL_ONE_GRAPHIC, PARCEL_ONE_GRAPHIC, graphic.getName());
		TransformComponent transform = parcelOne.getComponent(TransformComponent.class);
		assertNotNull("The parcel has no transform component", transform);
		MissionComponent mission = parcelOne.getComponent(MissionComponent.class);
		assertNotNull("The parcel has no mission", mission);
		assertEquals("The parcel mission destination is not equal to " + PARCEL_ONE_DESTINATION, PARCEL_ONE_DESTINATION, mission.getDestination());
	}

	@Test
	public void couldLoadTheEarth() {
		GameObject earth = gameObjectFactory.load(EARTH_NAME);
		assertEquals("The name is not equal to " + EARTH_NAME, EARTH_NAME, earth.getName());
		GravityComponent gravity = earth.getComponent(GravityComponent.class);
		assertNotNull("The spaceship has no gravity component", gravity);
	}

	/**
	 * Creates the {@link GameObject}s list
	 */
	private void createObjectList() {
		String objectList = "";
		FileHandle[] files = Gdx.files.local(GameObjectFactory.OBJECTS_DIR).list();
		for (FileHandle file : files) {
			if (file.path().endsWith(GameObjectFactory.JSON_EXT)) {
				objectList += file.nameWithoutExtension() + "\n";
			}
		}
		FileHandle fileList = Gdx.files.local(GameObjectFactory.OBJECT_LIST);
		fileList.writeString(objectList, false);
	}

	@Test
	public void couldLoadAllObjects() {
		createObjectList();
		Array<GameObject> objects = gameObjectFactory.loadObjects();
		assertNotNull("Objects array is null", objects);
		assertTrue("The array does not contain any object", objects.size > 0);
	}

	@Test
	public void couldSaveAllObjects() {
		createObjectList();
		Array<GameObject> objects = gameObjectFactory.loadObjects();
		gameObjectFactory.saveObjects(objects);
		objects = gameObjectFactory.loadObjects();
		assertNotNull("Objects array is null", objects);
		assertTrue("The array does not contain any object", objects.size > 0);
	}
}
