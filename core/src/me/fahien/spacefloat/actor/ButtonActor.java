package me.fahien.spacefloat.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

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

	private static final Color COLOR_ON = Color.WHITE;
	private static final Color COLOR_OFF = new Color(0x002030ff);

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
		addListener(new ButtonClickListener());
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

	/**
	 * The Button {@link ClickListener}
	 *
	 * @author Fahien
	 */
	public class ButtonClickListener extends ClickListener {
		@Override
		public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor)  {
			super.enter(event, x, y, pointer, fromActor);
			setRegion(onRegion);
			textColor = COLOR_ON;
		}

		@Override
		public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
			super.exit(event, x, y, pointer, toActor);
			setRegion(offRegion);
			textColor = COLOR_OFF;
		}
	}
}
