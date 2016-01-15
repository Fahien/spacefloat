package me.fahien.spacefloat.component;

import com.badlogic.gdx.math.Vector3;

/**
 * The Planet {@link RigidbodyComponent}
 *
 * @author Fahien
 */
public class PlanetComponent extends RigidbodyComponent {
	public final static short PLANET_GROUP = 1 << 2; // 4
	private final static short PLANET_MASK = 7; // The spaceship

	@Override
	public void createRigidbody() {
		super.createRigidbody();
		getRigidbody().setLinearFactor(Vector3.Zero);
	}
}
