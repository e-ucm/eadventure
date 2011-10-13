package es.eucm.eadventure.engine.core.platform.impl;

import playn.core.Canvas;
import playn.core.Path;
import playn.core.Gradient;

import com.google.inject.Singleton;

import es.eucm.eadventure.common.params.fills.impl.EAdBorderedColor;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.fills.impl.EAdLinearGradient;
import es.eucm.eadventure.common.params.paint.EAdFill;
import es.eucm.eadventure.engine.core.platform.FillFactory;
import es.eucm.eadventure.engine.core.platform.assets.impl.PlayNEngineColor;

@Singleton
public class PlayNFillFactory implements FillFactory<Canvas, Path> {

	@Override
	public void fill(EAdFill fill, Canvas graphicContext, Path shape) {
		// FIXME this should be done in a more modular way

		if (fill instanceof EAdColor) {
			if (((EAdColor) fill).getAlpha() > 0) {
				prepareGraphics((EAdColor) fill, graphicContext);
				graphicContext.fillPath(shape);
			}

		} else if (fill instanceof EAdBorderedColor) {
			EAdBorderedColor color = (EAdBorderedColor) fill;
			if (color.getCenterColor().getAlpha() > 0) {
				prepareGraphics(color.getCenterColor(), graphicContext);
				graphicContext.fillPath(shape);
			}

			if (color.getBorderColor().getAlpha() > 0) {
				graphicContext.setStrokeColor(new PlayNEngineColor(color.getBorderColor()).getColor());
				graphicContext.setStrokeWidth(color.getWidth());
				graphicContext.strokePath(shape);
			}
		} else if (fill instanceof EAdLinearGradient) {
			//FIXME GWT fix
			//GradientPaint p = this.getGradientPaint((EAdLinearGradient) fill,
			//		shape.getBounds().width, shape.getBounds().height);
			//graphicContext.setPaint(p);
			
			//Gradient g = createLinearGradient();
			//graphicContext.setFillGradient(g);
			int color = new PlayNEngineColor(((EAdLinearGradient) fill).getColor1()).getColor();
			graphicContext.setFillColor(color);
			color = new PlayNEngineColor(((EAdLinearGradient) fill).getColor2()).getColor();
			graphicContext.fillPath(shape);
			graphicContext.setStrokeColor(color);
			graphicContext.strokePath(shape);

		}
		
		
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
		g2.setFillColor(new PlayNEngineColor(color).getColor());
		/* FIXME removed for GWT
		if (color.getAlpha() > 0 && color.getAlpha() <= 255)
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					(float) color.getAlpha() / 255.0f));

		paint = new Color(color.getRed(), color.getGreen(), color.getBlue());

		g2.setPaint(paint);
		*/
	}

}
