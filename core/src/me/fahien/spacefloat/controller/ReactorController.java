package me.fahien.spacefloat.controller;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import me.fahien.spacefloat.component.EnergyComponent;
import me.fahien.spacefloat.component.GraphicComponent;
import me.fahien.spacefloat.component.ReactorComponent;
import me.fahien.spacefloat.component.RigidbodyComponent;

import static me.fahien.spacefloat.component.ComponentMapperEnumerator.energyMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.graphicMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.reactorMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.rigidMapper;

/**
 * The ReactorController
 *
 * @author Fahien
 */
public class ReactorController extends PlayerController {
	private static final int REACTOR_PRIORITY = 2;
	private ParticleSystem particleSystem;
	private Vector3 force;
	private float rotation;

	private ReactorComponent reactor;
	private EnergyComponent energy;
	private GraphicComponent graphic;
	private RigidbodyComponent rigidbody;

	public ReactorController() {
		super(REACTOR_PRIORITY);
	}

	public void setParticleSystem(ParticleSystem particleSystem) {
		this.particleSystem = particleSystem;
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);

		Entity player = getPlayer();
		reactor = reactorMapper.get(player);
		energy = energyMapper.get(player);
		graphic = graphicMapper.get(player);
		rigidbody = rigidMapper.get(player);
		getInputMultiplexer().addProcessor(new ReactorInputAdapter());

		force = new Vector3();
	}

	@Override
	public void removedFromEngine(Engine engine) {
		reactor.dispose();
	}

	protected Quaternion m_quaternion = new Quaternion();

	@Override
	public void update(float delta) {
		if (!energy.hasCharge()) {
			reactor.stop(particleSystem);
		} else {
			m_quaternion.setEulerAnglesRad(rotation, 0, 0);
			rigidbody.rotate(m_quaternion);
			if (reactor.isBurning()) {
				reactor.setTransform(rigidbody.getTransform());
				energy.addCharge(-reactor.getConsume() * 64 * delta);
				rigidbody.applyCentralForce(force.nor().scl(reactor.getPower() * 4096 * 4096));
			}
		}
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
				return true;
			}
			return false;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			reactor.stop(particleSystem);
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
