package me.fahien.spacefloat.screen;

/**
 * The {@link SpaceFloatScreen} Enumerator
 *
 * @author Fahien
 */
public enum ScreenEnumerator {
	SHOWCASE(new ShowcaseScreen()),
	INFO(new InfoScreen()),
	GAME(new GameScreen()),
	LOADING(new LoadingScreen()),
	MAIN(new MainScreen());

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
