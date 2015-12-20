package me.fahien.spacefloat.screen;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * A Two Dimension {@link SpaceFloatScreen}
 *
 * @author Fahien
 */
public abstract class TwoDimensionsScreen extends SpaceFloatScreen {

	private Viewport viewport;
	private Stage stage;

	/**
	 * Populates the {@link Stage}
	 */
	public abstract void populate(Stage stage);

	/**
	 * Updates the logic
	 */
	public abstract void update(float delta);

	@Override
	public void show() {
		super.show();
		viewport = new FitViewport(SpaceFloatScreen.WIDTH, SpaceFloatScreen.HEIGHT);
		stage = new Stage();
		populate(stage);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		update(delta);
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		viewport.update(width, height);
	}

	@Override
	public void dispose() {
		super.dispose();
		if (stage != null) stage.dispose();
	}
}
