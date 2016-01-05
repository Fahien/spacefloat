package me.fahien.spacefloat.component;

/**
 * The Hurt {@link CollisionComponent}
 *
 * @author Fahien
 */
public class HurtComponent extends CollisionComponent {
	private static final float DEFAULT_RADIUS = 75f;

	public HurtComponent() {
		super(DEFAULT_RADIUS);
	}

	public HurtComponent(float radius) {
		super(radius);
	}
}
