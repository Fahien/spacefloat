package me.fahien.spacefloat.controller;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import me.fahien.spacefloat.audio.Audio;
import me.fahien.spacefloat.component.EnergyComponent;
import me.fahien.spacefloat.component.GraphicComponent;
import me.fahien.spacefloat.component.ReactorComponent;
import me.fahien.spacefloat.component.RigidbodyComponent;
import me.fahien.spacefloat.game.SpaceFloat;
import me.fahien.spacefloat.game.SpaceFloatGame;
import me.fahien.spacefloat.screen.ScreenEnumerator;

import static me.fahien.spacefloat.component.ComponentMapperEnumerator.energyMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.graphicMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.reactorMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.rigidMapper;
import static me.fahien.spacefloat.game.SpaceFloatGame.logger;

/**
 * The ReactorController
 *
 * @author Fahien
 */
public class ReactorController extends PlayerController {
	private static final int REACTOR_PRIORITY = 2;
	private static final String END_MESSAGE = "Goes bad, space courier. You've run out of energy charge. I'm sorry but you're fired.";

	private ParticleSystem particleSystem;
	private Vector3 force;
	private float rotation;

	protected boolean endGame;

	private ReactorInputAdapter reactorInputAdapter;

	private ReactorComponent reactor;
	private EnergyComponent energy;
	private GraphicComponent graphic;
	private RigidbodyComponent rigidbody;

	private Audio audio;
	private Sound reactorSound;

	public ReactorController() {
		super(REACTOR_PRIORITY);
	}

	/**
	 * Sets the {@link Audio}
	 */
	public void setAudio(Audio audio) {
		this.audio = audio;
	}

	/**
	 * Sets the reactor {@link Sound}
	 */
	public void setReactorSound(Sound reactorSound) {
		this.reactorSound = reactorSound;
	}

	/**
	 * Sets the {@link ParticleSystem}
	 */
	public void setParticleSystem(ParticleSystem particleSystem) {
		this.particleSystem = particleSystem;
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);

		Entity player = getPlayer();
		if (player != null) {
			reactor = reactorMapper.get(player);
			energy = energyMapper.get(player);
			graphic = graphicMapper.get(player);
			rigidbody = rigidMapper.get(player);
		}

		endGame = false;

		reactorInputAdapter = new ReactorInputAdapter();
		getInputMultiplexer().addProcessor(reactorInputAdapter);

		force = new Vector3();
	}

	protected Quaternion m_quaternion = new Quaternion();
	protected boolean m_error;

	@Override
	public void update(final float delta) {
		if (energy != null) {
			if (energy.hasCharge()) {
				m_quaternion.setEulerAnglesRad(rotation, 0, 0);
				rigidbody.rotate(m_quaternion);
				if (reactor.isEmitting()) {
					reactor.setTransform(rigidbody.getTransform());
					energy.addCharge(-reactor.getConsume() * 64 * delta);
					rigidbody.applyCentralForce(force.nor().scl(reactor.getPower() * 4096 * 4096));
				}
			} else {
				if (!endGame) {
					reactor.stop(particleSystem);
					audio.stop(reactorSound);
					SpaceFloat.GAME.getGame().enqueueMessage(END_MESSAGE);
					endGame = true;
					final SpaceFloatGame game = SpaceFloat.GAME.getGame();
					game.getStage().getRoot().addAction(Actions.delay(0.003f, new Action() {
						@Override
						public boolean act(float delta) {
							game.getGameObjectFactory().dispose();
							game.setScreen(ScreenEnumerator.MAIN);
							return true;
						}
					}));
				}
			}
		} else {
			if (!m_error) {
				logger.error("Could not find energy component");
				m_error = true;
			}
		}
	}


	@Override
	public void removedFromEngine(final Engine engine) {
		super.removedFromEngine(engine);
		// Remove input processor
		getInputMultiplexer().removeProcessor(reactorInputAdapter);
		// Stop reactors if emitting
		reactor.stop(particleSystem);
		// Stop audio if playing
		audio.stop(reactorSound);
	}

	public void dispose() {
		if (reactor != null) reactor.dispose();
		if (energy != null) energy.dispose();
	}

	/**
	 * The Reactor {@link InputAdapter}
	 *
	 * @author Fahien
	 */
	private class ReactorInputAdapter extends InputAdapter {
		private final Vector2 mouse = new Vector2();
		private static final float SEMIPI = MathUtils.PI / 2;

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer, int button) {
			if (energy.hasCharge()) {
				reactor.start(particleSystem, graphic.getTransform());
				audio.loop(reactorSound);
				return true;
			}
			return false;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			reactor.stop(particleSystem);
			audio.stop(reactorSound);
			return true;
		}

		@Override
		public boolean mouseMoved(int screenX, int screenY) {
			force.x = mouse.x = screenX - Gdx.graphics.getWidth() / 2;
			force.z = mouse.y = screenY - Gdx.graphics.getHeight() / 2;
			rotation = - SEMIPI - mouse.angleRad();
			return true;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			if (energy.hasCharge()) {
				reactor.start(particleSystem, graphic.getTransform());
				return mouseMoved(screenX, screenY);
			}
			return false;
		}
	}
}
