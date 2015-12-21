package me.fahien.spacefloat.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;

import me.fahien.spacefloat.screen.SpaceFloatScreen;

/**
 * The {@link Actor} Factory
 */
public class ActorFactory {
	private static final String FPS_TXT = "FPS: ";
	private static final String VEL_TXT = "VEL: ";
	private static final String POS_TXT = "POS: ";
	private static final float FPS_X = 4.0f;
	private static final float FPS_Y = 8.0f;
	private static final float VEL_X = SpaceFloatScreen.WIDTH - FPS_X;
	private static final float VEL_Y = FPS_Y;
	private static final float POS_X = VEL_X;
	private static final float POS_Y = FPS_Y * 2;

	private FontActor fpsActor;
	private FontActor velocityActor;
	private FontActor positionActor;

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
			velocityActor.setHalign(FontActor.Halign.LEFT);
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
			positionActor.setHalign(FontActor.Halign.LEFT);
		}
		return positionActor;
	}
}
