package me.fahien.spacefloat.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;

import me.fahien.spacefloat.component.EnergyComponent;
import me.fahien.spacefloat.component.ParticleComponent;
import me.fahien.spacefloat.component.RigidbodyComponent;
import me.fahien.spacefloat.component.TransformComponent;

import static me.fahien.spacefloat.component.ComponentMapperEnumerator.energyMapper;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.rigidMapper;

/**
 * The Emission System for {@link ParticleComponent}s
 */
public class EmissionSystem extends IteratingSystem {
	private static final int EMISSION_PRIORITY = 2;
	private static final float EMISSION_DELAY = 0.001f;

	private EnergyComponent energy;
	private RigidbodyComponent rigidbody;

	private ParticleSystem particleSystem;

	public void setParticleSystem(final ParticleSystem particleSystem) {
		this.particleSystem = particleSystem;
	}

	public EmissionSystem() {
		super(Family.all(EnergyComponent.class, TransformComponent.class).get(), EMISSION_PRIORITY);
	}

	@Override
	public void addedToEngine(final Engine engine) {
		super.addedToEngine(engine);

		ImmutableArray<Entity> entities = getEntities();
		if (entities.size() > 0) {
			energy = energyMapper.get(entities.first());
			rigidbody = rigidMapper.get(entities.first());
		}
	}

	private float emissionTime;

	@Override
	protected void processEntity(final Entity entity, final float delta) {
		if (energy.isEmitting()) {
			energy.setTransform(rigidbody.getTransform());
			emissionTime += delta;
		}
		if (emissionTime > EMISSION_DELAY) {
			energy.stop(particleSystem);
			emissionTime = 0f;
		}
	}

	@Override
	public void removedFromEngine(Engine engine) {
		super.removedFromEngine(engine);
		energy.stop(particleSystem);
	}

	public void dispose() {
		if (energy != null) energy.dispose();
	}
}
