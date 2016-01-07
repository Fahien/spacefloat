package me.fahien.spacefloat.controller;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import me.fahien.spacefloat.component.EnergyComponent;
import me.fahien.spacefloat.component.GraphicComponent;
import me.fahien.spacefloat.component.ReactorComponent;
import me.fahien.spacefloat.component.RigidbodyComponent;
import me.fahien.spacefloat.system.PlayerSystem;

import static me.fahien.spacefloat.component.ComponentMapperEnumerator.energyMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.graphicMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.reactorMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.rigidMapper;

/**
 * The ReactorController
 *
 * @author Fahien
 */
public class ReactorController extends PlayerSystem {
	private ParticleSystem particleSystem;
	private Vector3 reactorPower;
	private float rotation;

	private ReactorComponent reactor;
	private EnergyComponent energy;
	private GraphicComponent graphic;
	private RigidbodyComponent rigidbody;

	public void setParticleSystem(ParticleSystem particleSystem) {
		this.particleSystem = particleSystem;
	}

	@Override
	public void addedToEngine(Engine engine, Entity player, InputMultiplexer inputMultiplexer) {
		reactor = reactorMapper.get(player);
		energy = energyMapper.get(player);
		graphic = graphicMapper.get(player);
		rigidbody = rigidMapper.get(player);
		inputMultiplexer.addProcessor(new ReactorInputAdapter());

		reactorPower = new Vector3();
	}

	protected Quaternion m_quaternion = new Quaternion();
	protected Matrix4 m_rigidTransform = new Matrix4();
	protected Vector3 m_position = new Vector3();

	@Override
	public void update(float deltaTime) {
		rigidbody.getTransform(m_rigidTransform);
		m_rigidTransform.getTranslation(m_position);
		m_quaternion.setEulerAnglesRad(rotation, 0, 0);
		m_rigidTransform.set(m_quaternion);
		m_rigidTransform.trn(m_position);
		rigidbody.setTransform(m_rigidTransform);

		if (reactor.isBurning()) {
			reactor.setTransform(m_rigidTransform);
			rigidbody.applyCentralForce(reactorPower.nor().scl(1000000000));
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
			reactorPower.x = mouse.x = screenX - Gdx.graphics.getWidth() / 2;
			reactorPower.z = mouse.y = screenY - Gdx.graphics.getHeight() / 2;
			rotation = - SEMIPI - mouse.angleRad();
			return true;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			return mouseMoved(screenX, screenY);
		}
	}
}
