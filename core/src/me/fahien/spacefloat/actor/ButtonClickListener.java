package me.fahien.spacefloat.actor;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import me.fahien.spacefloat.audio.Audio;

/**
 * The Button {@link ClickListener}
 *
 * @author Fahien
 */
public class ButtonClickListener extends ClickListener {

	private ButtonActor actor;
	private Audio audio;
	private Sound selectSound;

	public ButtonClickListener(final ButtonActor actor, final Audio audio, final Sound selectSound) {
		this.actor = actor;
		this.audio = audio;
		this.selectSound = selectSound;
	}

	@Override
	public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
		super.enter(event, x, y, pointer, fromActor);
		actor.setRegion(actor.getOnRegion());
		actor.setTextColor(ButtonActor.COLOR_ON);
		audio.stop(selectSound);
		audio.play(selectSound);
	}

	@Override
	public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
		super.exit(event, x, y, pointer, toActor);
		actor.setRegion(actor.getOffRegion());
		actor.setTextColor(ButtonActor.COLOR_OFF);
	}
}