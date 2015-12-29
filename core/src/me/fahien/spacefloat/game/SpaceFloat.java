package me.fahien.spacefloat.game;

import me.fahien.spacefloat.screen.ScreenEnumerator;

/**
 * SpaceFloat
 *
 * @author Fahien
 */
public enum SpaceFloat {
	GAME(new SpaceFloatGame());

	private SpaceFloatGame game;

	SpaceFloat(SpaceFloatGame game) {
		this.game = game;
	}

	public void setScreen(ScreenEnumerator screenEnumerator) {
		game.setScreen(screenEnumerator);
	}

	public SpaceFloatGame getGame() {
		return game;
	}
}
