package me.fahien.spacefloat.component;

/**
 * The Hurt {@link RigidbodyComponent}
 *
 * @author Fahien
 */
public class HurtComponent extends RigidbodyComponent {
	private static final float DEFAULT_MASS = 10f;
	private static final float DEFAULT_RADIUS = 75f;
	public final static short HURT_GROUP = 1 << 1; // 2
	private final static short DEFAULT_MASK = 7; // 1 | 2 | 4

	public HurtComponent() {
		this(DEFAULT_RADIUS);
	}

	public HurtComponent(float radius) {
		super(DEFAULT_MASS, radius, HURT_GROUP, DEFAULT_MASK);
	}
}
