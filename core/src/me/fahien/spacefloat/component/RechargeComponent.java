package me.fahien.spacefloat.component;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

/**
 * The Refuel {@link CollisionComponent}
 *
 * @author Fahien
 */
public class RechargeComponent extends CollisionComponent {
	private static final float DEFAULT_RADIUS = 100.0f;

	/**
	 * Offset for the energy station model
	 */
	private static final Vector3 offset = new Vector3(-115f, 0f, 207f);

	public RechargeComponent() {
		super(DEFAULT_RADIUS);
	}

	@Override
	public void setTransform(Matrix4 transform) {
		setWorldTransform(transform);
		setWorldTransform(getWorldTransform().translate(offset));
	}
}
