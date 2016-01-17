package me.fahien.spacefloat.component;

import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The {@link ReactorComponent} Test Case
 *
 * @author Fahien
 */
public class ReactorComponentTest {

	private ReactorComponent reactorComponent;

	@Before
	public void before() {
		reactorComponent = new ReactorComponent();
	}

	@Test
	public void couldInitializeAReactor() {
		reactorComponent.setName("reactor.pfx");
		reactorComponent.setConsume(64f);
		reactorComponent.setPower(64f);
		reactorComponent.setEffect(new ParticleEffect());
	}

	@After
	public void after() {
		reactorComponent.dispose();
	}
}
