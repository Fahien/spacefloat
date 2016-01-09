package me.fahien.spacefloat.component;

/**
 * Planet {@link CollisionComponent}
 *
 * @author Fahien
 */
public class PlanetComponent extends CollisionComponent {
	private static final float DEFAULT_RADIUS = 500f;
	private final static short DEFAULT_GROUP = 1 << 2; // 4 - Planet
	private final static short DEFAULT_MASK = 1 | 2; // 1 - Player + 2 - Object

	public PlanetComponent() {
		super(DEFAULT_RADIUS, DEFAULT_GROUP, DEFAULT_MASK);
	}
}
