package me.fahien.spacefloat.component;

import com.badlogic.gdx.math.Vector3;

/**
 * The Planet {@link RigidbodyComponent}
 *
 * @author Fahien
 */
public class PlanetComponent extends RigidbodyComponent {
	@Override
	public void createRigidbody() {
		super.createRigidbody();
		getRigidbody().setLinearFactor(Vector3.Zero);
	}
}
