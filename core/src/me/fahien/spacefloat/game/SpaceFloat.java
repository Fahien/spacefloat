package me.fahien.spacefloat.game;

import me.fahien.spacefloat.screen.ScreenEnumerator;

/**
 * SpaceFloat Enumerator
 *
 * @author Fahien
 */
public enum SpaceFloat {
	GAME(new SpaceFloatGame());

	private SpaceFloatGame game;

	SpaceFloat(SpaceFloatGame game) {
		this.game = game;
	}
	/**
	 * Returns the {@link SpaceFloatGame}
	 */
	public SpaceFloatGame getGame() {
		return game;
	}

	/**
	 * Sets the current screen
	 */
	public void setScreen(ScreenEnumerator screenEnumerator) {
		game.setScreen(screenEnumerator);
	}
}
