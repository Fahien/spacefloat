package me.fahien.spacefloat.audio;


import com.badlogic.gdx.audio.Sound;

/**
 * Audio
 *
 * @author Fahien
 */
public enum Audio {
	INSTANCE;

	public static final String SOUNDS_DIR = "sounds/";
	public static final String REACTOR_SOUND = "reactor.ogg";
	public static final String RECHARGE_SOUND = "recharge.ogg";
	public static final String COLLISION_SOUND = "collision.ogg";
	public static final String SELECT_SOUND = "select.ogg";

	private boolean mute;
	private float volume;

	Audio() {
		volume = 1.0f;
	}

	/**
	 * Toggles audio
	 */
	public boolean toggle() {
		mute = !mute;
		return mute;
	}

	/**
	 * Plays a sound
	 */
	public void play(final Sound sound) {
		play(sound, volume);
	}

	/**
	 * Play a sound at this volume
	 */
	public void play(final Sound sound, final float volume) {
		if (mute) return;
		sound.play(volume);
	}

	/**
	 * Stops a soung
	 */
	public void stop(final Sound sound) {
		sound.stop();
	}

	/**
	 * Loops a sound
	 */
	public void loop(final Sound sound) {
		if (mute) return;
		sound.loop();
	}

	/**
	 * Tests whether is mute
	 */
	public boolean isMute() {
		return mute;
	}
}