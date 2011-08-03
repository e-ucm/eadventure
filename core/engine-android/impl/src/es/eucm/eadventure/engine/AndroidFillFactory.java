package es.eucm.eadventure.engine;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;

import com.google.inject.Singleton;

import es.eucm.eadventure.common.params.EAdFill;
import es.eucm.eadventure.common.params.fills.impl.EAdBorderedColor;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
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
			p.setStyle(Style.FILL);
			p.setARGB(color.getCenterColor().getAlpha(), color.getCenterColor()
					.getRed(), color.getCenterColor().getGreen(), color
					.getCenterColor().getBlue());
			c.drawText(text, 0, 0, p);

			p.setStyle(Style.STROKE);
			p.setARGB(color.getBorderColor().getAlpha(), color.getBorderColor()
					.getRed(), color.getBorderColor().getGreen(), color
					.getBorderColor().getBlue());
			p.setStrokeWidth(color.getWidth());
			c.drawText(text, 0, 0, p);
		}
	}

}
