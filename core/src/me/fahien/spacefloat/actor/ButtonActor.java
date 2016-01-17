package me.fahien.spacefloat.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * The Button {@link HudActor}
 *
 * @author Fahien
 */
public class ButtonActor extends HudActor {
	private static final String BUTTON_OFF = "button-off";
	private static final String BUTTON_ON = "button-on";

	private static final float MESSAGE_X_OFFSET = 26f;
	private static final float MESSAGE_Y_OFFSET = -8f;

	public static final Color COLOR_ON = Color.WHITE;
	public static final Color COLOR_OFF = Color.GRAY;

	private BitmapFont font;
	private String text;
	private Color textColor;

	private TextureRegion offRegion;
	private TextureRegion onRegion;

	public ButtonActor(TextureAtlas menu, BitmapFont font, String text) {
		super(menu.findRegion(BUTTON_OFF));
		this.font = font;
		this.text = text;
		textColor = COLOR_OFF;
		offRegion = getRegion();
		onRegion = menu.findRegion(BUTTON_ON);
	}

	/**
	 * Returns the on {@link TextureRegion}
	 */
	public TextureRegion getOnRegion() {
		return onRegion;
	}

	/**
	 * Returns the off {@link TextureRegion}
	 */
	public TextureRegion getOffRegion() {
		return offRegion;
	}

	/**
	 * Sets the current text {@link Color}
	 */
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		// Draw text shadow
		font.setColor(Color.BLACK);
		font.draw(batch,
				text,
				getX() + MESSAGE_X_OFFSET + 1,
				getY() + getHeight() + MESSAGE_Y_OFFSET - 1,
				getWidth() - MESSAGE_X_OFFSET,
				FontActor.Halign.CENTER.getValue(),
				true);

		// Draw text
		font.setColor(textColor);
		font.draw(batch,
				text,
				getX() + MESSAGE_X_OFFSET,
				getY() + getHeight() + MESSAGE_Y_OFFSET,
				getWidth() - MESSAGE_X_OFFSET,
				FontActor.Halign.CENTER.getValue(),
				true);
	}
}
