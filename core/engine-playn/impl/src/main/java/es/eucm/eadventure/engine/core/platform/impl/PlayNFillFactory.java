package es.eucm.eadventure.engine.core.platform.impl;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import playn.core.Canvas;
import playn.core.SurfaceLayer;

import com.google.inject.Singleton;

import es.eucm.eadventure.common.params.EAdFill;
import es.eucm.eadventure.common.params.fills.impl.EAdBorderedColor;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.fills.impl.EAdLinearGradient;
import es.eucm.eadventure.engine.core.platform.FillFactory;

@Singleton
public class PlayNFillFactory implements FillFactory<Canvas, Shape> {

	private Paint paint;

	@Override
	public void fill(EAdFill fill, Canvas graphicContext, Shape shape) {
		// FIXME this should be done in a more modular way
		/* FIXME removed for GWT
		if (fill instanceof EAdColor) {
			if (((EAdColor) fill).getAlpha() > 0) {
				prepareGraphics((EAdColor) fill, graphicContext);
				graphicContext.fill(shape);
			}

		} else if (fill instanceof EAdBorderedColor) {
			EAdBorderedColor color = (EAdBorderedColor) fill;
			if (color.getCenterColor().getAlpha() > 0) {
				prepareGraphics(color.getCenterColor(), graphicContext);
				graphicContext.fill(shape);
			}

			if (color.getBorderColor().getAlpha() > 0) {
				prepareGraphics(color.getBorderColor(), graphicContext);
				graphicContext.setStroke(new BasicStroke(color.getWidth()));
				graphicContext.draw(shape);
			}
		} else if (fill instanceof EAdLinearGradient) {
			GradientPaint p = this.getGradientPaint((EAdLinearGradient) fill,
					shape.getBounds().width, shape.getBounds().height);
			graphicContext.setPaint(p);
			graphicContext.fill(shape);
		}
		*/
	}

	@Override
	public void fill(EAdFill fill, Canvas graphicContext, String text) {

		if (fill instanceof EAdColor) {
			prepareGraphics((EAdColor) fill, graphicContext);
			graphicContext.drawText(text, 0, 0);

		} else if (fill instanceof EAdBorderedColor) {
			EAdBorderedColor color = (EAdBorderedColor) fill;
			prepareGraphics(color.getBorderColor(), graphicContext);
			int offset = color.getWidth();
			graphicContext.drawText(text, offset, offset);
			graphicContext.drawText(text, -offset, offset);
			graphicContext.drawText(text, offset, -offset);
			graphicContext.drawText(text, -offset, -offset);
			prepareGraphics(color.getCenterColor(), graphicContext);
			graphicContext.drawText(text, 0, 0);
		} else if (fill instanceof EAdLinearGradient) {
			/* FIXME removed for GWT
			Rectangle2D bounds = graphicContext.getFontMetrics().getStringBounds(text, graphicContext);
			GradientPaint p = getGradientPaint((EAdLinearGradient) fill,
					(float) bounds.getWidth(), (float) bounds.getHeight());
			
			graphicContext.setPaint(p);
			*/
			graphicContext.drawText(text, 0, 0);
		}

	}

	private void prepareGraphics(EAdColor color, Canvas g2) {
		/* FIXME removed for GWT
		if (color.getAlpha() > 0 && color.getAlpha() <= 255)
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					(float) color.getAlpha() / 255.0f));

		paint = new Color(color.getRed(), color.getGreen(), color.getBlue());

		g2.setPaint(paint);
		*/
	}

	private GradientPaint getGradientPaint(EAdLinearGradient gradient,
			float width, float height) {
		Color color1 = new Color(gradient.getColor1().getRed(), gradient
				.getColor1().getGreen(), gradient.getColor1().getBlue(),
				gradient.getColor1().getAlpha());
		Color color2 = new Color(gradient.getColor2().getRed(), gradient
				.getColor2().getGreen(), gradient.getColor2().getBlue(),
				gradient.getColor2().getAlpha());

		float x2 = gradient.isVertical() ? 0 : width;
		float y2 = gradient.isVertical() ? height : 0;
		return new GradientPaint(0, 0, color1, x2, y2, color2);
	}
}
