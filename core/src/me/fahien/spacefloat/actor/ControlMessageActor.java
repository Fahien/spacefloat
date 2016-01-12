package me.fahien.spacefloat.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.removeActor;

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
	private static final float MESSAGE_X_OFFSET = 4f;
	private static final float MESSAGE_Y_OFFSET = -8f;

	private static final InputListener ControlMessageInputListener = new InputListener() {
		@Override
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
			event.getTarget().addAction(removeActor());
			return true;
		}
	};

	private TextureRegion background;
	private TextureRegion border;
	private BitmapFont font;
	private String text;

	public ControlMessageActor(TextureAtlas hud, BitmapFont font) {
		super(hud.findRegion(CONTROL_FACE_HUD));
		background = hud.findRegion(CONTROL_BACKGROUND_HUD);
		border = hud.findRegion(CONTROL_BORDER_HUD);
		this.font = font;
		setWidth(getWidth() + BACKGROUND_REPEAT_COUNT * background.getRegionWidth() + border.getRegionWidth());
		addListener(ControlMessageInputListener);
	}

	/**
	 * Sets the text
	 */
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		// Draw control face
		super.draw(batch, parentAlpha);

		// Draw background
		for (int i = 0; i < BACKGROUND_REPEAT_COUNT; i++) {
			batch.draw(background,
					getX() + i * background.getRegionWidth() + getRegion().getRegionWidth(),
					getY(),
					background.getRegionWidth(),
					background.getRegionHeight());
		}

		// Draw right border
		batch.draw(border,
				getX() + BACKGROUND_REPEAT_COUNT * border.getRegionWidth() + getRegion().getRegionWidth(),
				getY(),
				border.getRegionWidth(),
				border.getRegionHeight());

		// Draw text shadow
		font.setColor(Color.BLACK);
		font.draw(batch,
				text,
				getX() + getRegion().getRegionWidth() + MESSAGE_X_OFFSET + 1,
				getY() + getHeight() + MESSAGE_Y_OFFSET - 1,
				getWidth() - getRegion().getRegionWidth() - MESSAGE_X_OFFSET,
				FontActor.Halign.LEFT.getValue(),
				true);

		// Draw text
		font.setColor(Color.WHITE);
		font.draw(batch,
				text,
				getX() + getRegion().getRegionWidth() + MESSAGE_X_OFFSET,
				getY() + getHeight() + MESSAGE_Y_OFFSET,
				getWidth() - getRegion().getRegionWidth() - MESSAGE_X_OFFSET,
				FontActor.Halign.LEFT.getValue(),
				true);
	}
}
