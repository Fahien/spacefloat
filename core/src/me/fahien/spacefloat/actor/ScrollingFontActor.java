package me.fahien.spacefloat.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * The Scrolling {@link FontActor}
 *
 * @author Fahien
 */
public class ScrollingFontActor extends Actor {
	public static final float INITIAL_Y = -8f;

	private BitmapFont font;
	private String text;
	private float position;

	public ScrollingFontActor(BitmapFont font, String text) {
		this.font = font;
		this.text = text;
		position = INITIAL_Y;
	}

	public void setPosition(float position) {
		this.position = position;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		position += delta * 4096 * 8;
		if (position > getHeight() * 2.4f) {
			position = 0f;
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		font.setColor(Color.BLACK);
		font.draw(batch, text, getX() + 1, getY() - 1 + position, getWidth(), 1, false);
		font.setColor(Color.WHITE);
		font.draw(batch, text, getX(), getY() + position, getWidth(), 1, false);
	}
}
