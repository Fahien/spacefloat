package me.fahien.spacefloat.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import me.fahien.spacefloat.component.MissionComponent;
import me.fahien.spacefloat.mission.Mission;

/**
 * The Parcel {@link HudActor}
 *
 * @author Fahien
 */
public class ParcelActor extends HudActor {
	private static final String BACKGROUND_HUD = "parcel-background";
	private static final String SPACESHIP_HUD = "parcel-spaceship";
	private static final String UP_HUD = "parcel-up";
	private static final String BASE_UP_HUD = "parcel-base-up";
	private static final String PARCEL_HUD = "parcel";
	private static final String BASE_DOWN_HUD = "parcel-base-down";
	private static final String DOWN_HUD = "parcel-down";
	private static final String TARGET_HUD = "parcel-target";
	private static final float DELAY_MAX = 0.002f;

	private static final float SPACESHIP_Y = 72.0f;
	private static final float BASE_OFFSET = 5.0f;
	private static final float UP_Y = 51.0f;
	private static final float BASE_UP_Y = 48.0f;
	private static final float PARCEL_Y = 40.0f;
	private static final float BASE_DOWN_Y = 24.0f;
	private static final float DOWN_Y = 21.0f;

	private static final float TIME_BASE = 0.002f;
	private static final float TIME_ONE = 0.0015f;
	private static final float TIME_TWO = 0.001f;
	private static final float TIME_TREE = 0.0005f;

	private TextureRegion spaceship;
	private TextureRegion up;
	private TextureRegion baseUp;
	private TextureRegion parcel;
	private TextureRegion baseDown;
	private TextureRegion down;
	private TextureRegion target;

	private MissionComponent missionComponent;
	private float delay;

	public ParcelActor(final TextureAtlas hud, final MissionComponent missionComponent) {
		super(hud.findRegion(BACKGROUND_HUD));
		spaceship = hud.findRegion(SPACESHIP_HUD);
		up = hud.findRegion(UP_HUD);
		baseUp = hud.findRegion(BASE_UP_HUD);
		parcel = hud.findRegion(PARCEL_HUD);
		baseDown = hud.findRegion(BASE_DOWN_HUD);
		down = hud.findRegion(DOWN_HUD);
		target = hud.findRegion(TARGET_HUD);
		this.missionComponent = missionComponent;
		delay = DELAY_MAX;
	}

	protected Mission m_mission;

	@Override
	public void act(final float delta) {
		if (delay < DELAY_MAX){
			delay += delta;
		}
	}

	@Override
	public void draw(final Batch batch, final float parentAlpha) {
		if (missionComponent.isCollecting()) {
			delay = 0;
			super.draw(batch, parentAlpha);
			// Draw parcel
			batch.draw(parcel,
					getX(), getY() + PARCEL_Y,
					parcel.getRegionWidth(),
					parcel.getRegionHeight());
			if (missionComponent.getHandlingTime() < TIME_BASE) {
				// Draw base up
				batch.draw(baseUp,
						getX(), getY() + BASE_UP_Y,
						baseUp.getRegionWidth(),
						baseUp.getRegionHeight());
			}
			if (missionComponent.getHandlingTime() < TIME_ONE) {
				// Draw up one
				batch.draw(up,
						getX(), getY() + UP_Y,
						up.getRegionWidth(),
						up.getRegionHeight());
			}
			if (missionComponent.getHandlingTime() < TIME_TWO) {
				// Draw up two
				batch.draw(up,
						getX(), getY() + UP_Y + BASE_OFFSET,
						up.getRegionWidth(),
						up.getRegionHeight());
			}
			if (missionComponent.getHandlingTime() < TIME_TREE) {
				// Draw up three
				batch.draw(up,
						getX(), getY() + UP_Y + BASE_OFFSET * 2,
						up.getRegionWidth(),
						up.getRegionHeight());
			}
		}
		else if (missionComponent.isDelivering()) {
			delay = 0;
			super.draw(batch, parentAlpha);
			// Draw parcel
			batch.draw(parcel,
					getX(), getY() + PARCEL_Y,
					parcel.getRegionWidth(),
					parcel.getRegionHeight());
			if (missionComponent.getHandlingTime() < TIME_BASE) {
				// Draw base down
				batch.draw(baseDown,
						getX(), getY() + BASE_DOWN_Y,
						baseDown.getRegionWidth(),
						baseDown.getRegionHeight());
			}
			if (missionComponent.getHandlingTime() < TIME_ONE) {
				// Draw down one
				batch.draw(down,
						getX(), getY() + DOWN_Y,
						down.getRegionWidth(),
						down.getRegionHeight());
			}
			if (missionComponent.getHandlingTime() < TIME_TWO) {
				// Draw down two
				batch.draw(down,
						getX(), getY() + DOWN_Y - BASE_OFFSET,
						down.getRegionWidth(),
						down.getRegionHeight());
			}
			if (missionComponent.getHandlingTime() < TIME_TREE) {
				// Draw down two
				batch.draw(down,
						getX(), getY() + DOWN_Y - BASE_OFFSET * 2,
						down.getRegionWidth(),
						down.getRegionHeight());
			}
		}
		else if (delay < DELAY_MAX) {
			super.draw(batch, parentAlpha);
			m_mission = missionComponent.getMission();
			if (m_mission != null) {
				if (m_mission.isCollected() && !m_mission.isDelivered()) {
					// Draw spaceship
					batch.draw(spaceship,
							getX(), getY() + SPACESHIP_Y,
							spaceship.getRegionWidth(),
							spaceship.getRegionHeight());
				} else {
					// Draw target
					batch.draw(target,
							getX(), getY(),
							target.getRegionWidth(),
							target.getRegionHeight());
				}
			} else {
				// Draw target
				batch.draw(target,
						getX(), getY(),
						target.getRegionWidth(),
						target.getRegionHeight());
			}
		}
	}
}
