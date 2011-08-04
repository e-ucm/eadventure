package es.eucm.eadventure.engine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;

import com.google.inject.Singleton;

import es.eucm.eadventure.common.params.EAdFill;
import es.eucm.eadventure.common.params.fills.impl.EAdBorderedColor;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.fills.impl.EAdLinearGradient;
import es.eucm.eadventure.engine.core.platform.FillFactory;

@Singleton
public class AndroidFillFactory implements FillFactory<Canvas, Path> {

	private Paint p = new Paint();

	@Override
	public void fill(EAdFill fill, Canvas c, Path path) {
		p.reset();
		if (fill instanceof EAdColor) {
			EAdColor color = (EAdColor) fill;
			p.setARGB(color.getAlpha(), color.getRed(), color.getGreen(),
					color.getBlue());
			c.drawPath(path, p);
		} else if (fill instanceof EAdBorderedColor) {
			EAdBorderedColor color = (EAdBorderedColor) fill;
			p.setStyle(Style.FILL);
			p.setARGB(color.getCenterColor().getAlpha(), color.getCenterColor()
					.getRed(), color.getCenterColor().getGreen(), color
					.getCenterColor().getBlue());
			c.drawPath(path, p);

			p.setStyle(Style.STROKE);
			p.setARGB(color.getBorderColor().getAlpha(), color.getBorderColor()
					.getRed(), color.getBorderColor().getGreen(), color
					.getBorderColor().getBlue());
			p.setStrokeWidth(color.getWidth());
			c.drawPath(path, p);
		} else if (fill instanceof EAdLinearGradient) {
			RectF bounds = new RectF();
			path.computeBounds(bounds, false);
			LinearGradient gradient = getGradient((EAdLinearGradient) fill,
					bounds.right - bounds.left, bounds.bottom - bounds.top);
			p.setShader(gradient);
			p.setStyle(Style.FILL);

			c.drawPath(path, p);
		}

	}

	@Override
	public void fill(EAdFill fill, Canvas c, String text) {
		p.reset();
		if (fill instanceof EAdColor) {
			EAdColor color = (EAdColor) fill;
			p.setARGB(color.getAlpha(), color.getRed(), color.getGreen(),
					color.getBlue());
			c.drawText(text, 0, 0, p);
		} else if (fill instanceof EAdBorderedColor) {

			EAdBorderedColor color = (EAdBorderedColor) fill;

			p.setStyle(Style.STROKE);
			p.setARGB(color.getBorderColor().getAlpha(), color.getBorderColor()
					.getRed(), color.getBorderColor().getGreen(), color
					.getBorderColor().getBlue());
			p.setStrokeWidth(color.getWidth());
			c.drawText(text, 0, 0, p);

			p.setStyle(Style.FILL);
			p.setARGB(color.getCenterColor().getAlpha(), color.getCenterColor()
					.getRed(), color.getCenterColor().getGreen(), color
					.getCenterColor().getBlue());
			c.drawText(text, 0, 0, p);

		} else if (fill instanceof EAdLinearGradient) {
			Rect bounds = new Rect();
			p.getTextBounds(text, 0, text.length(), bounds);
			LinearGradient gradient = getGradient((EAdLinearGradient) fill,
					bounds.right - bounds.left, bounds.bottom - bounds.top);
			p.setShader(gradient);
			p.setStyle(Style.FILL);

			c.drawText(text, 0, 0, p);
		}
	}

	private LinearGradient getGradient(EAdLinearGradient gradient, float width,
			float height) {
		int color1 = Color.argb(gradient.getColor1().getAlpha(), gradient
				.getColor1().getRed(), gradient.getColor1().getGreen(), gradient
				.getColor1().getBlue());
		int color2 = Color.argb(gradient.getColor2().getAlpha(), gradient
				.getColor2().getRed(), gradient.getColor1().getGreen(), gradient
				.getColor2().getBlue());

		float x2 = gradient.isVertical() ? 0 : width;
		float y2 = gradient.isVertical() ? height : 0;
		return new LinearGradient(0, 0, x2, y2, color1, color2,
				Shader.TileMode.CLAMP);

	}

}
