package me.fahien.spacefloat.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * The Control Message {@link HudActor}
 *
 * @author Fahien
 */
public class ControlMessageActor extends HudActor {
	private static final String CONTROL_FACE_HUD = "control-face";
	private static final String CONTROL_BACKGROUND_HUD = "control-background";
	private static final String CONTROL_BORDER_HUD = "control-border";
	private static final int BACKGROUND_REPEAT_COUNT = 32;
	private static final float MESSAGE_Y_OFFSET = -8f;

	private TextureRegion background;
	private TextureRegion border;
	private BitmapFont font;
	private String message;

	public ControlMessageActor(TextureAtlas hud, BitmapFont font, String message) {
		super(hud.findRegion(CONTROL_FACE_HUD));
		background = hud.findRegion(CONTROL_BACKGROUND_HUD);
		border = hud.findRegion(CONTROL_BORDER_HUD);
		this.font = font;
		this.message = message;
		setWidth(getWidth() + BACKGROUND_REPEAT_COUNT * background.getRegionWidth() + border.getRegionWidth());
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		for (int i = 0; i < BACKGROUND_REPEAT_COUNT; i++) {
			batch.draw(background,
					getX() + i * background.getRegionWidth() + getRegion().getRegionWidth(),
					getY(),
					background.getRegionWidth(),
					background.getRegionHeight());
		}
		batch.draw(border,
				getX() + BACKGROUND_REPEAT_COUNT * border.getRegionWidth() + getRegion().getRegionWidth(),
				getY(),
				border.getRegionWidth(),
				border.getRegionHeight());
		font.draw(batch,
				message,
				getX() + getRegion().getRegionWidth(),
				getY() + getHeight() + MESSAGE_Y_OFFSET,
				getWidth() + BACKGROUND_REPEAT_COUNT * background.getRegionWidth(),
				FontActor.Halign.LEFT.getValue(),
				false);
	}
}
