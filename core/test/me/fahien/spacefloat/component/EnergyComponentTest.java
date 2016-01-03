package me.fahien.spacefloat.component;

import com.badlogic.gdx.math.Vector3;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * The {@link EnergyComponent} Test Case
 *
 * @author Fahien
 */
public class EnergyComponentTest {

	private EnergyComponent energy;

	@Before
	public void before() {
		energy = new EnergyComponent();
	}

	@Test
	public void chargeIsGreaterThanOrEqualToZero() {
		assertTrue("The charge is less than zero", energy.getCharge() >= 0f);
	}

	@Test
	public void setChargeProperly() {
		energy.setCharge(64.0f);
		assertEquals("The charge is not equals to chargeMax", energy.getChargeMax(), energy.getCharge(), 0f);
		energy.setCharge(-1f);
		assertEquals("The charge is not equals to 0", EnergyComponent.CHARGE_MIN, energy.getCharge(), 0f);
	}

	@Test
	public void absorbeHurts() {
		energy.setCharge(5f);
		energy.hurt(new Vector3(100f, 100f, 100f), new Vector3(-1f, -1f, -1f));
		assertTrue("The charge is not less than 5",  energy.getCharge() < 5f);
	}

	@Test
	public void chargeIsGreaterThanZero() {
		assertTrue("The charge max is less than or equal to zero", energy.getChargeMax() > 0f);
	}

	@Test
	public void setChargeMaxProperly() {
		energy.setChargeMax(-1);
		assertEquals("The charge is less than or equal to zero", EnergyComponent.CHARGE_MAX_LOWER_LIMIT, energy.getChargeMax(), 0f);
		energy.setChargeMax(5f);
		assertEquals("The charge max is not equals to 5", 5f, energy.getChargeMax(), 0f);
	}
}
