package me.fahien.spacefloat.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import me.fahien.spacefloat.component.PlayerComponent;

/**
 * A linear {@link HudActor}
 *
 * @author Fahien
 */
public class FuelHudActor extends HudActor {
	private static final String LINEAR_HUD = "fuel";
	private static final String POINT_HUD = "point";
	private static final int POINT_MAX = 19;
	private static final int POINT_INITIAL_OFFSET = 17;
	private static final int POINT_X_OFFSET = 4;
	private static final int POINT_Y_OFFSET = 5;

	private TextureRegion point;
	private int pointWidth;
	private int pointHeight;

	private PlayerComponent player;

	public FuelHudActor(TextureAtlas hud, PlayerComponent player) {
		super(hud.findRegion(LINEAR_HUD));
		point = hud.findRegion(POINT_HUD);
		pointWidth = point.getRegionWidth();
		pointHeight = point.getRegionHeight();
		this.player = player;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		int pointCount = (int)(player.getFuel() / player.getFuelMax() * POINT_MAX);
		for (int i = 0; i < pointCount; i++) {
			batch.draw(point,
					getX() + POINT_INITIAL_OFFSET + i * POINT_X_OFFSET,
					getY() + POINT_Y_OFFSET,
					pointWidth,
					pointHeight);
		}
	}
}


