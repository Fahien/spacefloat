package me.fahien.spacefloat.factory;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

import java.awt.DisplayMode;

import me.fahien.spacefloat.actor.ButtonActor;
import me.fahien.spacefloat.actor.ButtonClickListener;
import me.fahien.spacefloat.actor.ControlMessageActor;
import me.fahien.spacefloat.actor.EnergyHudActor;
import me.fahien.spacefloat.actor.FontActor;
import me.fahien.spacefloat.actor.HudActor;
import me.fahien.spacefloat.actor.MoneyActor;
import me.fahien.spacefloat.actor.ParcelActor;
import me.fahien.spacefloat.actor.ScrollingFontActor;
import me.fahien.spacefloat.audio.Audio;
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
			"Alberto Pastorato\n\n" +
			"Sound\n" +
			"freesound.org";

	private TextureAtlas hud;
	private TextureAtlas menu;

	private BitmapFont font;

	private Audio audio;
	private Sound selectSound;

	private Graphics graphics;

	private Application app;

	private FontActor fpsActor;
	private FontActor velocityActor;
	private EnergyHudActor fuelActor;
	private ControlMessageActor messageActor;
	private MoneyActor moneyActor;
	private ParcelActor parcelActor;

	private HudActor backgroundMenu;
	private ButtonActor continueActor;
	private ButtonActor newGameActor;
	private ButtonActor videoActor;
	private ButtonActor audioActor;
	private ButtonActor exitActor;
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
	 * Sets the {@link Audio}
	 */
	public void setAudio(final Audio audio) {
		this.audio = audio;
	}

	/**
	 * Sets the select {@link Sound}
	 */
	public void setSelectSound(final Sound selectSound) {
		this.selectSound = selectSound;
	}

	/**
	 * Sets the {@link Graphics}
	 */
	public void setGraphics(final Graphics graphics) {
		this.graphics = graphics;
	}

	/**
	 * Sets the {@link Application}
	 */
	public void setApplication(final Application app) {
		this.app = app;
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
		velocityActor = new FontActor(font, VEL_TXT + velocity) {
			@Override
			public void act(final float delta) {
				setText(VEL_TXT + (int) (velocity.len()));
			}
		};
		velocityActor.setPosition(VEL_X, VEL_Y);
		velocityActor.setHalign(FontActor.Halign.RIGHT);
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
			moneyActor = new MoneyActor(hud, font);
			moneyActor.setPosition(MONEY_X, MONEY_Y);
		}
		moneyActor.setMoney(money);
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
		parcelActor.resetDelay();
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
		continueActor.clearListeners();
		continueActor.addListener(new ButtonClickListener(continueActor, audio, selectSound));
		return continueActor;
	}

	/**
	 * Returns the new game {@link ButtonActor}
	 */
	public HudActor getNewGameActor() {
		if (newGameActor == null) {
			newGameActor = new ButtonActor(menu, font, "New Game");
		}
		newGameActor.clearListeners();
		newGameActor.addListener(new ButtonClickListener(newGameActor, audio, selectSound));
		return newGameActor;
	}

	public HudActor getVideoActor() {
		if (videoActor == null) {
			videoActor = new ButtonActor(menu, font, "Video");
		}
		videoActor.clearListeners();
		videoActor.addListener(new ButtonClickListener(videoActor, audio, selectSound) {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (graphics.isFullscreen()) {
					graphics.setWindowedMode(SpaceFloatGame.WINDOW_WIDTH, SpaceFloatGame.WINDOW_HEIGHT);
				} else {
					Graphics.DisplayMode displayMode = graphics.getDisplayMode();
					graphics.setFullscreenMode(displayMode);
				}
			}
		});
		return videoActor;
	}

	public HudActor getAudioActor() {
		if (audioActor == null) {
			audioActor = new ButtonActor(menu, font, "Audio");
		}
		audioActor.clearListeners();
		audioActor.addListener(new ButtonClickListener(audioActor, audio, selectSound) {
			@Override
			public void clicked(final InputEvent event, final float x, final float y) {
				audio.toggle();
			}
		});
		return audioActor;
	}

	public HudActor getExitActor() {
		exitActor = new ButtonActor(menu, font, "Exit");
		exitActor.clearListeners();
		exitActor.addListener(new ButtonClickListener(exitActor, audio, selectSound) {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				app.exit();
			}
		});
		return exitActor;
	}

	public void dispose() {
		fpsActor = null;
		velocityActor = null;
		fuelActor = null;
		messageActor = null;
		moneyActor = null;
		parcelActor = null;

		backgroundMenu = null;
		continueActor = null;
		newGameActor = null;
		videoActor = null;
		audioActor = null;
		creditsActor = null;
		textBackgroundMenu = null;
	}
}
