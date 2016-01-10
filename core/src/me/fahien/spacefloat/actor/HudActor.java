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

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(region, getX(), getY(), region.getRegionWidth(), region.getRegionHeight());
	}
}
