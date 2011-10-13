package es.eucm.eadventure.engine.core.platform;

import playn.core.Canvas;
import playn.core.Color;
import playn.core.Font;
import playn.core.Gradient;
import playn.core.Path;
import playn.core.PlayN;

import com.google.inject.Inject;

import es.eucm.eadventure.common.params.EAdFont;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.fills.impl.EAdLinearGradient;
import es.eucm.eadventure.common.params.geom.EAdRectangle;
import es.eucm.eadventure.common.params.paint.EAdFill;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Image;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Shape;
import es.eucm.eadventure.engine.core.platform.assets.impl.PlayNBezierShape;
import es.eucm.eadventure.engine.core.platform.assets.impl.PlayNEngineImage;
import es.eucm.eadventure.engine.core.platform.impl.AbstractCanvas;
import es.eucm.eadventure.engine.core.util.EAdTransformation;

public class PlayNCanvas extends AbstractCanvas<Canvas> {
	
	private Font f;

	@Inject
	public PlayNCanvas(FontHandler fontHandler) {
		super(fontHandler);
	}

	@Override
	public void setTransformation(EAdTransformation t) {
		float[] m = t.getMatrix().getFlatMatrix();
		g.setTransform(m[0], m[1], m[3], m[4], m[6], m[7]);
	}

	@Override
	public void drawImage(DrawableAsset<? extends Image> image) {
		g.drawImage(((PlayNEngineImage) image).getImage(), 0, 0);

	}

	@Override
	public void drawShape(DrawableAsset<? extends Shape> shape) {
		Path s = ((PlayNBezierShape) shape).getShape();
		// Fill
		if (paint.getFill() != null) {
			updatePaintFill(paint.getFill());
			g.fillPath(s);
		}
		// Border
		if (paint.getBorder() != null && paint.getBorderWidth() > 0) {
			updatePaintBorder(paint.getBorder());
			g.setStrokeWidth(paint.getBorderWidth());
			g.strokePath(s);
		}

	}

	private void updatePaintFill(EAdFill fill) {
		if (fill instanceof EAdColor) {
			EAdColor c = (EAdColor) fill;
			g.setFillColor(Color.argb(c.getAlpha(), c.getRed(), c.getGreen(),
					c.getBlue()));
		} else if (fill instanceof EAdLinearGradient) {
			EAdLinearGradient gradient = (EAdLinearGradient) fill;
			EAdColor c = gradient.getColor1();
			int cint1 = Color.argb(c.getAlpha(), c.getRed(), c.getGreen(),
					c.getBlue());
			c = gradient.getColor2();
			int cint2 = Color.argb(c.getAlpha(), c.getRed(), c.getGreen(),
					c.getBlue());
			Gradient gr = PlayN.graphics().createLinearGradient(gradient.getX0(),
					gradient.getY0(), gradient.getX1(), gradient.getY1(),
					new int[] { cint1, cint2 }, new float[] { 0, 1 });
			g.setFillGradient(gr);
		}
	}
	
	private void updatePaintBorder(EAdFill border) {
		EAdColor c = EAdColor.BLACK;
		if ( border instanceof EAdColor ){
			c = (EAdColor) border;
		}
		else if ( border instanceof EAdLinearGradient ){
			c = ((EAdLinearGradient) border).getColor1();
		}
		g.setStrokeColor(Color.argb(c.getAlpha(), c.getRed(), c.getGreen(), c.getBlue()));
	}

	@Override
	public void drawText(String text, int x, int y) {
		g.drawText(text, x, y);
	}

	@Override
	public void setFont(EAdFont font) {
		
//		f = PlayN.graphics().createFont(font.getName(), font., size)
//		g.

	}

	@Override
	public void clip(EAdRectangle rectangle) {
		Path p = PlayN.graphics().createPath();
		p.moveTo(0, 0);
		p.lineTo(rectangle.getWidth(), 0);
		p.lineTo(rectangle.getWidth(), rectangle.getHeight());
		p.lineTo(0, rectangle.getHeight());
		p.close();
		g.clip(p);
	}

	@Override
	public void save() {
		g.save();

	}

	@Override
	public void restore() {
		g.restore();

	}

	@Override
	public void translate(int x, int y) {
		g.translate(x, y);
	}

}
