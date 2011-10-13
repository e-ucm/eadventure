package es.eucm.eadventure.engine.core.platform.impl;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import com.google.inject.Singleton;

import es.eucm.eadventure.common.params.fills.impl.EAdBorderedColor;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.fills.impl.EAdLinearGradient;
import es.eucm.eadventure.common.params.paint.EAdFill;
import es.eucm.eadventure.engine.core.platform.FillFactory;

@Singleton
public class DesktopFillFactory implements FillFactory<Graphics2D, Shape> {

	private Paint paint;

	@Override
	public void fill(EAdFill fill, Graphics2D graphicContext, Shape shape) {
		// FIXME this should be done in a more modular way
		Graphics2D g2 = (Graphics2D) graphicContext.create();
		if (fill instanceof EAdColor) {
			if (((EAdColor) fill).getAlpha() > 0) {
				prepareGraphics((EAdColor) fill, g2);
				g2.fill(shape);
			}

		} else if (fill instanceof EAdBorderedColor) {
			EAdBorderedColor color = (EAdBorderedColor) fill;
			if (color.getCenterColor().getAlpha() > 0) {
				prepareGraphics(color.getCenterColor(), g2);
				((Graphics2D) g2.create()).fill(shape);
			}

			if (color.getBorderColor().getAlpha() > 0) {
				prepareGraphics(color.getBorderColor(), g2);
				g2.setStroke(new BasicStroke(color.getWidth()));
				g2.draw(shape);
			}
		} else if (fill instanceof EAdLinearGradient) {
			GradientPaint p = this.getGradientPaint((EAdLinearGradient) fill,
					shape.getBounds().width, shape.getBounds().height);
			g2.setPaint(p);
			g2.fill(shape);
		}
	}

	@Override
	public void fill(EAdFill fill, Graphics2D graphicContext, String text) {
		Graphics2D g2 = (Graphics2D) graphicContext.create();
		if (fill instanceof EAdColor) {
			prepareGraphics((EAdColor) fill, g2);
			g2.drawString(text, 0, 0);

		} else if (fill instanceof EAdBorderedColor) {
			EAdBorderedColor color = (EAdBorderedColor) fill;
			prepareGraphics(color.getBorderColor(), g2);
			int offset = color.getWidth();
			g2.drawString(text, offset, offset);
			g2.drawString(text, -offset, offset);
			g2.drawString(text, offset, -offset);
			g2.drawString(text, -offset, -offset);
			prepareGraphics(color.getCenterColor(), g2);
			g2.drawString(text, 0, 0);
		} else if (fill instanceof EAdLinearGradient) {
			Rectangle2D bounds = g2.getFontMetrics().getStringBounds(text, g2);
			GradientPaint p = getGradientPaint((EAdLinearGradient) fill,
					(float) bounds.getWidth(), (float) bounds.getHeight());

			g2.setPaint(p);
			g2.drawString(text, 0, 0);
		}

	}

	private void prepareGraphics(EAdColor color, Graphics2D g2) {
		if (color.getAlpha() > 0 && color.getAlpha() <= 255) {

			float factor = (float) color.getAlpha() / 255.0f;

			Composite c = g2.getComposite();
			if (c instanceof AlphaComposite) {
				factor *= ((AlphaComposite) c).getAlpha();
			}
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					factor));
		}

		paint = new Color(color.getRed(), color.getGreen(), color.getBlue());

		g2.setPaint(paint);
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
