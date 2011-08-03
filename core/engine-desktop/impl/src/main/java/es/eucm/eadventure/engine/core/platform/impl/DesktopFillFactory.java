package es.eucm.eadventure.engine.core.platform.impl;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;

import com.google.inject.Singleton;

import es.eucm.eadventure.common.params.EAdFill;
import es.eucm.eadventure.common.params.fills.impl.EAdBorderedColor;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.engine.core.platform.FillFactory;

@Singleton
public class DesktopFillFactory implements FillFactory<Graphics2D, Shape> {

	private Paint paint;

	@Override
	public void fill(EAdFill fill, Graphics2D graphicContext, Shape shape) {
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
		}

	}

	private void prepareGraphics(EAdColor color, Graphics2D g2) {
		if (color.getAlpha() > 0 && color.getAlpha() <= 255)
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					(float) color.getAlpha() / 255.0f));

		paint = new Color(color.getRed(), color.getGreen(), color.getBlue());

		g2.setPaint(paint);
	}
}
