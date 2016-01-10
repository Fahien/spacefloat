package me.fahien.spacefloat.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;

import me.fahien.spacefloat.component.EnergyComponent;
import me.fahien.spacefloat.screen.SpaceFloatScreen;

/**
 * The {@link Actor} Factory
 *
 * @author Fahien
 */
public class HudFactory {
	private static final String FPS_TXT = "FPS: ";
	private static final String VEL_TXT = "VEL: ";
	private static final float FPS_X = 4.0f;
	private static final float FPS_Y = 8.0f;
	private static final float VEL_X = SpaceFloatScreen.WIDTH - FPS_X;
	private static final float VEL_Y = FPS_Y;
	private static final float ENERGY_X = FPS_X;
	private static final float ENERGY_Y = SpaceFloatScreen.HEIGHT - 22.0f;
	private static final float MESSAGE_X = 96f;
	private static final float MESSAGE_Y = FPS_Y;

	private TextureAtlas hud;
	private BitmapFont font;

	private FontActor fpsActor;
	private FontActor velocityActor;
	private HudActor fuelActor;
	private ControlMessageActor messageActor;

	/**
	 * Sets the {@link TextureAtlas} HUD
	 */
	public void setHud(final TextureAtlas hud) {
		this.hud = hud;
	}

	/**
	 * Sets the {@link BitmapFont}
	 */
	public void setFont(final BitmapFont font) {
		this.font = font;
	}

	/**
	 * Returns the fps {@link FontActor}
	 */
	public FontActor getFpsActor() {
		if (fpsActor == null) {
			fpsActor = new FontActor(font, FPS_TXT + Gdx.graphics.getFramesPerSecond()) {
				@Override
				public void act(float delta) {
					setText(FPS_TXT + Gdx.graphics.getFramesPerSecond());
				}
			};
			fpsActor.setPosition(FPS_X, FPS_Y);
		}
		return fpsActor;
	}

	/**
	 * Returns the velocity {@link FontActor}
	 */
	public FontActor getVelocityActor(final Vector3 velocity) {
		if (velocityActor == null) {
			velocityActor = new FontActor(font, VEL_TXT + velocity) {
				@Override
				public void act(float delta) {
					setText(VEL_TXT + (int)(velocity.len()));
				}
			};
			velocityActor.setPosition(VEL_X, VEL_Y);
			velocityActor.setHalign(FontActor.Halign.RIGHT);
		}
		return velocityActor;
	}

	/**
	 * Returns the fuel {@link HudActor}
	 */
	public HudActor getFuelActor(final EnergyComponent energy) {
		if (fuelActor == null) {
			fuelActor = new EnergyHudActor(hud, energy);
			fuelActor.setPosition(ENERGY_X, ENERGY_Y);
		}
		return fuelActor;
	}

	/**
	 * Returns the {@link ControlMessageActor}
	 */
	public ControlMessageActor getMessageActor(final String text) {
		if (messageActor == null) {
			messageActor = new ControlMessageActor(hud, font);
			messageActor.setPosition(MESSAGE_X, MESSAGE_Y);
		}
		messageActor.setText(text);
		return messageActor;
	}
}
