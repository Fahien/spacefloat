package me.fahien.spacefloat.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import me.fahien.spacefloat.component.MoneyComponent;

/**
 * The Money {@link HudActor}
 *
 * @author Fahien
 */
public class MoneyActor extends HudActor {
	private static final String MONEY_HUD = "money";
	private static final float MONEY_X_OFFSET = 8f;
	private static final float MONEY_Y_OFFSET = 9f;

	private BitmapFont font;
	private MoneyComponent money;
	private StringBuilder stringBuilder;

	public MoneyActor(final TextureAtlas hud, final BitmapFont font) {
		super(hud.findRegion(MONEY_HUD));
		this.font = font;
		stringBuilder = new StringBuilder();
	}

	/**
	 * Sets the {@link MoneyComponent}
	 */
	public void setMoney(final MoneyComponent money) {
		this.money = money;
	}

	@Override
	public void act(float delta) {
		stringBuilder.delete(0, stringBuilder.length());
		stringBuilder.append(money.getMoney());
		if (money.getReward() > 0) {
			stringBuilder.append(" + ").append(money.getReward());
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);

		// Draw text shadow
		font.setColor(Color.BLACK);
		font.draw(batch,
				stringBuilder,
				getX() + MONEY_X_OFFSET + 1,
				getY() + MONEY_Y_OFFSET - 1,
				getWidth() - MONEY_X_OFFSET,
				FontActor.Halign.CENTER.getValue(),
				true);

		// Draw text
		font.setColor(Color.WHITE);
		font.draw(batch,
				stringBuilder,
				getX() + MONEY_X_OFFSET,
				getY() + MONEY_Y_OFFSET,
				getWidth() - MONEY_X_OFFSET,
				FontActor.Halign.CENTER.getValue(),
				true);
	}
}
