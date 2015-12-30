package me.fahien.spacefloat.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;

import me.fahien.spacefloat.component.PlayerComponent;
import me.fahien.spacefloat.screen.SpaceFloatScreen;

/**
 * The {@link Actor} Factory
 *
 * @author Fahien
 */
public class HudFactory {
	private static final String FPS_TXT = "FPS: ";
	private static final String VEL_TXT = "VEL: ";
	private static final String ROT_TXT = "ACC: ";
	private static final String POS_TXT = "POS: ";
	private static final float FPS_X = 4.0f;
	private static final float FPS_Y = 8.0f;
	private static final float VEL_X = SpaceFloatScreen.WIDTH - FPS_X;
	private static final float VEL_Y = FPS_Y;
	private static final float ROT_X = VEL_X;
	private static final float ROT_Y = FPS_Y * 3;
	private static final float POS_X = VEL_X;
	private static final float POS_Y = FPS_Y * 6;
	private static final float FUEL_X = FPS_X;
	private static final float FUEL_Y = SpaceFloatScreen.HEIGHT - 22.0f;

	private FontActor fpsActor;
	private FontActor velocityActor;
	private FontActor positionActor;
	private FontActor accelerationActor;
	private HudActor fuelActor;

	/**
	 * Returns the fps {@link FontActor}
	 */
	public FontActor getFpsActor(BitmapFont font) {
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
	public FontActor getVelocityActor(BitmapFont font, final Vector3 velocity) {
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
	 * Returns the position {@link FontActor}
	 */
	public FontActor getPositionActor(BitmapFont font, final Vector3 position) {
		if (positionActor == null) {
			positionActor = new FontActor(font, VEL_TXT + position) {
				@Override
				public void act(float delta) {
					setText(POS_TXT +
							"(" + (int)(position.x) + ", " +
							(int)(position.y) + ", " +
							(int)(position.z) + ")");
				}
			};
			positionActor.setPosition(POS_X, POS_Y);
			positionActor.setHalign(FontActor.Halign.RIGHT);
		}
		return positionActor;
	}

	/**
	 * Returns the rotation velocity {@link FontActor}
	 */
	public FontActor getAccelerationActor(BitmapFont font, final Vector3 acceleration) {
		if (accelerationActor == null) {
			accelerationActor = new FontActor(font, ROT_TXT + acceleration) {
				@Override
				public void act(float delta) {
					setText(ROT_TXT + (int) acceleration.len());
				}
			};
			accelerationActor.setPosition(ROT_X, ROT_Y);
			accelerationActor.setHalign(FontActor.Halign.RIGHT);
		}
		return accelerationActor;
	}

	/**
	 * Returns the fuel {@link HudActor}
	 */
	public HudActor getFuelActor(TextureAtlas hud, PlayerComponent player) {
		if (fuelActor == null) {
			fuelActor = new FuelHudActor(hud, player);
			fuelActor.setPosition(FUEL_X, FUEL_Y);
		}
		return fuelActor;
	}
}
