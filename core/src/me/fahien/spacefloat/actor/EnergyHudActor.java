package me.fahien.spacefloat.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import me.fahien.spacefloat.component.EnergyComponent;

/**
 * A linear {@link HudActor}
 *
 * @author Fahien
 */
public class EnergyHudActor extends HudActor {
	private static final String LINEAR_HUD = "energy";
	private static final String POINT_NORMAL_HUD = "point-normal";
	private static final String POINT_WARNING_HUD = "point-warning";
	private static final String POINT_DANGER_HUD = "point-danger";
	private static final int POINT_MAX = 15;
	private static final int DANGER_MAX = POINT_MAX / 5;
	private static final int WARNING_MAX = POINT_MAX / 3;
	private static final int POINT_INITIAL_OFFSET = 21;
	private static final int POINT_X_OFFSET = 4;
	private static final int POINT_Y_OFFSET = 4;

	private TextureRegion pointNormal;
	private TextureRegion pointWarning;
	private TextureRegion pointDanger;
	private int pointWidth;
	private int pointHeight;

	private EnergyComponent energy;

	public EnergyHudActor(TextureAtlas hud) {
		super(hud.findRegion(LINEAR_HUD));
		pointNormal = hud.findRegion(POINT_NORMAL_HUD);
		pointWarning = hud.findRegion(POINT_WARNING_HUD);
		pointDanger = hud.findRegion(POINT_DANGER_HUD);
		pointWidth = pointNormal.getRegionWidth();
		pointHeight = pointNormal.getRegionHeight();
	}

	/**
	 * Sets the {@link EnergyComponent}
	 */
	public void setEnergy(EnergyComponent energy) {
		this.energy = energy;
	}

	protected TextureRegion m_point;
	protected int m_pointCount;

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		m_pointCount = MathUtils.ceil(energy.getCharge() / energy.getChargeMax() * POINT_MAX);
		if (m_pointCount <= DANGER_MAX) {
			m_point = pointDanger;
		} else if (m_pointCount <= WARNING_MAX) {
			m_point = pointWarning;
		} else {
			m_point = pointNormal;
		}
		for (int i = 0; i < m_pointCount; i++) {
			batch.draw(m_point,
					getX() + POINT_INITIAL_OFFSET + i * POINT_X_OFFSET,
					getY() + POINT_Y_OFFSET,
					pointWidth,
					pointHeight);
		}
	}
}


