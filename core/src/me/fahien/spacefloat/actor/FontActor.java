package me.fahien.spacefloat.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * The Font {@link Actor}
 *
 * @author Fahien
 */
public class FontActor extends Actor {
	public enum Halign {
		RIGHT(0),
		LEFT(-1),
		CENTER(1);

		private int value;

		Halign(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	private BitmapFont font;
	private StringBuilder text;

	private int halign;

	public FontActor(BitmapFont font, String text) {
		super();
		this.font = font;
		this.text = new StringBuilder(text);
		halign = Halign.LEFT.getValue();
	}

	/**
	 * Returns the text
	 */
	public StringBuilder getText() {
		return text;
	}

	/**
	 * Sets the text
	 */
	public void setText(String text) {
		this.text.replace(0, this.text.length(), text);
		this.text.delete(text.length(), this.text.length());
	}

	/**
	 * Sets horizontal alignment
	 */
	public void setHalign(Halign halign) {
		this.halign = halign.getValue();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		font.setColor(Color.BLACK);
		font.draw(batch, text, getX() + 1, getY() + getHeight() / 2 - 1, getWidth(), halign, false);
		font.setColor(Color.WHITE);
		font.draw(batch, text, getX(), getY() + getHeight() / 2, getWidth(), halign, false);
	}
}
