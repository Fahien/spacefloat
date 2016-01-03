package me.fahien.spacefloat.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;
import com.badlogic.gdx.math.MathUtils;

/**
 * SpaceFloat {@link ShapeRenderer}
 *
 * @author Fahien
 */
public class SpaceFloatShapeRenderer extends ShapeRenderer {

	@Override
	public void circle (float x, float z, float radius) {
		circle(x, z, radius, Math.max(1, (int)(6 * (float)Math.cbrt(radius))));
	}

	@Override
	public void circle (float x, float z, float radius, int segments) {
		if (segments <= 0) throw new IllegalArgumentException("segments must be > 0.");
		Color color = getColor();
		float colorBits = color.toFloatBits();
		float angle = 2 * MathUtils.PI / segments;
		float cos = MathUtils.cos(angle);
		float sin = MathUtils.sin(angle);
		float cx = radius, cz = 0;
		ShapeType shapeType = getCurrentType();
		ImmediateModeRenderer renderer = getRenderer();
		if (shapeType == ShapeType.Line) {
			check(ShapeType.Line, ShapeType.Filled, segments * 2 + 2);
			for (int i = 0; i < segments; i++) {
				renderer.color(colorBits);
				renderer.vertex(x + cx, 0, z + cz);
				float temp = cx;
				cx = cos * cx - sin * cz;
				cz = sin * temp + cos * cz;
				renderer.color(colorBits);
				renderer.vertex(x + cx, 0, z + cz);
			}
			// Ensure the last segment is identical to the first.
			renderer.color(colorBits);
			renderer.vertex(x + cx, 0, z + cz);
		} else {
			check(ShapeType.Line, ShapeType.Filled, segments * 3 + 3);
			segments--;
			for (int i = 0; i < segments; i++) {
				renderer.color(colorBits);
				renderer.vertex(x, 0, z);
				renderer.color(colorBits);
				renderer.vertex(x + cx, 0, z + cz);
				float temp = cx;
				cx = cos * cx - sin * cz;
				cz = sin * temp + cos * cz;
				renderer.color(colorBits);
				renderer.vertex(x + cx, 0, z + cz);
			}
			// Ensure the last segment is identical to the first.
			renderer.color(colorBits);
			renderer.vertex(x, 0, z);
			renderer.color(colorBits);
			renderer.vertex(x + cx, 0, z + cz);
		}

		float temp = cx;
		cx = radius;
		cz = 0;
		renderer.color(colorBits);
		renderer.vertex(x + cx, 0, z + cz);
	}
}
