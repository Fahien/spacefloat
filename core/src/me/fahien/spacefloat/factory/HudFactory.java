package me.fahien.spacefloat.factory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;

import me.fahien.spacefloat.actor.ButtonActor;
import me.fahien.spacefloat.actor.ControlMessageActor;
import me.fahien.spacefloat.actor.EnergyHudActor;
import me.fahien.spacefloat.actor.FontActor;
import me.fahien.spacefloat.actor.HudActor;
import me.fahien.spacefloat.actor.MoneyActor;
import me.fahien.spacefloat.actor.ParcelActor;
import me.fahien.spacefloat.actor.ScrollingFontActor;
import me.fahien.spacefloat.component.EnergyComponent;
import me.fahien.spacefloat.component.MissionComponent;
import me.fahien.spacefloat.component.MoneyComponent;
import me.fahien.spacefloat.game.SpaceFloatGame;
import me.fahien.spacefloat.screen.SpaceFloatScreen;

/**
 * The {@link Actor} Factory
 *
 * @author Fahien
 */
public enum HudFactory {
	INSTANCE;

	private static final String FPS_TXT = "FPS: ";
	private static final String VEL_TXT = "VEL: ";

	private static final float FPS_X = 4.0f;
	private static final float FPS_Y = 8.0f;

	private static final float VEL_X = SpaceFloatScreen.WIDTH - FPS_X;
	private static final float VEL_Y = FPS_Y;

	private static final float ENERGY_X = FPS_X;
	private static final float ENERGY_Y = SpaceFloatScreen.HEIGHT - 22.0f;

	private static final float MESSAGE_X = 96.0f;
	private static final float MESSAGE_Y = FPS_Y;

	private static final float MONEY_X = SpaceFloatScreen.WIDTH - FPS_X - 72.0f;
	private static final float MONEY_Y = ENERGY_Y;

	private static final float PARCEL_X = SpaceFloatScreen.WIDTH - FPS_X - 16.0f;
	private static final float PARCEL_Y = ENERGY_Y - 110f;

	private static final String CREDITS =
			"SPACE FLOAT\n" +
			"version " + SpaceFloatGame.VERSION + "\n\n" +
			"Design\n"+
			"Antonio Caggiano\n" +
			"Alberto Pastorato\n\n" +
			"Programming\n" +
			"Antonio Caggiano\n\n" +
			"Artwork\n" +
			"Alberto Pastorato";

	private TextureAtlas hud;
	private TextureAtlas menu;

	private BitmapFont font;

	private FontActor fpsActor;
	private FontActor velocityActor;
	private EnergyHudActor fuelActor;
	private ControlMessageActor messageActor;
	private MoneyActor moneyActor;
	private ParcelActor parcelActor;

	private HudActor backgroundMenu;
	private HudActor continueActor;
	private HudActor newGameActor;
	private HudActor videoActor;
	private HudActor audioActor;
	private ScrollingFontActor creditsActor;
	private HudActor textBackgroundMenu;

	/**
	 * Sets the {@link TextureAtlas} HUD
	 */
	public void setHud(final TextureAtlas hud) {
		this.hud = hud;
	}

	/**
	 * Sets the {@link TextureAtlas} Menu
	 */
	public void setMenu(final TextureAtlas menu) {
		this.menu = menu;
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
			fuelActor = new EnergyHudActor(hud);
			fuelActor.setPosition(ENERGY_X, ENERGY_Y);
		}
		fuelActor.setEnergy(energy);
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

	/**
	 * Returns the {@link MoneyActor}
	 */
	public MoneyActor getMoneyActor(final MoneyComponent money) {
		if (moneyActor == null) {
			moneyActor = new MoneyActor(hud, font, money);
			moneyActor.setPosition(MONEY_X, MONEY_Y);
		}
		return moneyActor;
	}

	/**
	 * Returns the {@link ParcelActor}
	 */
	public ParcelActor getParcelActor(final MissionComponent mission) {
		if (parcelActor == null) {
			parcelActor = new ParcelActor(hud, mission);
			parcelActor.setPosition(PARCEL_X, PARCEL_Y);
		}
		return parcelActor;
	}

	/**
	 * Returns the background menu {@link HudActor}
	 */
	public HudActor getBackgroundMenu() {
		if (backgroundMenu == null) {
			TextureRegion background = menu.findRegion("background");
			backgroundMenu = new HudActor(background);
		}
		return backgroundMenu;
	}

	/**
	 * Returns the credits {@link ScrollingFontActor}
	 */
	public ScrollingFontActor getCreditsActor() {
		if (creditsActor == null) {
			creditsActor = new ScrollingFontActor(font, CREDITS);
		}
		creditsActor.setPosition(ScrollingFontActor.INITIAL_Y);
		return creditsActor;
	}

	/**
	 * Returns the text background
	 */
	public HudActor getTextBackgroundMenu() {
		if (textBackgroundMenu == null) {
			TextureRegion background = menu.findRegion("text-background");
			textBackgroundMenu = new HudActor(background);
		}
		return textBackgroundMenu;
	}

	/**
	 * Returns the continue {@link ButtonActor}
	 */
	public HudActor getContinueActor() {
		if (continueActor == null) {
			continueActor = new ButtonActor(menu, font, "Continue");
		}
		return continueActor;
	}

	/**
	 * Returns the new game {@link ButtonActor}
	 */
	public HudActor getNewGameActor() {
		if (newGameActor == null) {
			newGameActor = new ButtonActor(menu, font, "New Game");
		}
		return newGameActor;
	}

	public HudActor getVideoActor() {
		if (videoActor == null) {
			videoActor = new ButtonActor(menu, font, "Video");
		}
		return videoActor;
	}

	public HudActor getAudioActor() {
		if (audioActor == null) {
			audioActor = new ButtonActor(menu, font, "Audio");
		}
		return audioActor;
	}
}
