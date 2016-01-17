package me.fahien.spacefloat.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Hud {@link Actor}
 *
 * @author Fahien
 */
public class HudActor extends Actor {

	private TextureRegion region;

	public HudActor(TextureRegion region) {
		this.region = region;
		setWidth(region.getRegionWidth());
		setHeight(region.getRegionHeight());
	}

	/**
	 * Returns the {@link TextureRegion}
	 */
	public TextureRegion getRegion() {
		return region;
	}

	/**
	 * Sets the {link TextureRegion}
	 */
	public void setRegion(final TextureRegion region) {
		this.region = region;
		setWidth(region.getRegionWidth());
		setHeight(region.getRegionHeight());
	}

	@Override
	public void draw(final Batch batch, final float parentAlpha) {
		batch.draw(region, getX(), getY(), region.getRegionWidth(), region.getRegionHeight());
	}
}
