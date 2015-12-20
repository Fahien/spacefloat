package me.fahien.spacefloat.screen;

/**
 * The {@link SpaceFloatScreen} Enumerator
 *
 * @author Fahien
 */
public enum ScreenEnumerator {
	SHOWCASE(new ShowcaseScreen()),
	INFO(new InfoScreen()),
	MAIN(new MainScreen()),
	LOADING(new LoadingScreen());

	private SpaceFloatScreen screen;

	ScreenEnumerator(SpaceFloatScreen screen) {
		this.screen = screen;
	}

	/**
	 * Returns the screen
	 */
	public SpaceFloatScreen getScreen() {
		return screen;
	}
}
