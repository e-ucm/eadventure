package es.eucm.eadventure.engine.core.platform.impl;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import com.google.inject.Inject;

import es.eucm.eadventure.common.params.EAdFont;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.fills.impl.EAdLinearGradient;
import es.eucm.eadventure.common.params.geom.EAdRectangle;
import es.eucm.eadventure.common.params.paint.EAdFill;
import es.eucm.eadventure.common.params.paint.EAdPaint;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Image;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Shape;
import es.eucm.eadventure.engine.core.platform.DrawableAsset;
import es.eucm.eadventure.engine.core.platform.FontHandler;
import es.eucm.eadventure.engine.core.platform.assets.impl.DesktopBezierShape;
import es.eucm.eadventure.engine.core.platform.assets.impl.DesktopEngineFont;
import es.eucm.eadventure.engine.core.platform.assets.impl.DesktopEngineImage;
import es.eucm.eadventure.engine.core.util.EAdTransformation;

public class DesktopCanvas extends AbstractCanvas<Graphics2D> {

	private Map<EAdFill, Paint> fillCache;

	private Map<EAdPaint, Stroke> strokeCache;

	private Stack<Graphics2D> graphicStack;

	private AlphaComposite alphaComposite;

	@Inject
	public DesktopCanvas(FontHandler fontHandler) {
		super(fontHandler);
		fillCache = new HashMap<EAdFill, Paint>();
		strokeCache = new HashMap<EAdPaint, Stroke>();
		graphicStack = new Stack<Graphics2D>();
	}

	@Override
	public void setTransformation(EAdTransformation t) {
		float m[] = t.getMatrix().getFlatMatrix();
		g.setTransform(new AffineTransform(m[0], m[1], m[3], m[4], m[6], m[7]));
		alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				t.getAlpha());
		g.setComposite(alphaComposite);
	}

	@Override
	public void drawImage(DrawableAsset<? extends Image> image) {
		g.drawImage(((DesktopEngineImage) image).getImage(), 0, 0, null);
	}

	@Override
	public void drawShape(DrawableAsset<? extends Shape> shape) {
		java.awt.Shape s = ((DesktopBezierShape) shape).getShape();

		// Fill
		if (paint.getFill() != null) {
			g.setPaint(getPaint(paint.getFill()));
			g.fill(s);
		}
		// Border
		if (paint.getBorder() != null && paint.getBorderWidth() > 0) {
			g.setPaint(getPaint(paint.getBorder()));
			g.setStroke(getStroke(paint));
			g.draw(s);
		}
	}

	@Override
	public void drawText(String str, int x, int y) {
		y += (g.getFontMetrics().getAscent() - g.getFontMetrics().getDescent());
		// Border
		if (paint.getBorder() != null && paint.getBorderWidth() > 0) {
			int w = paint.getBorderWidth();
			g.setPaint(getPaint(paint.getBorder()));
			g.setStroke(getStroke(paint));
			g.drawString(str, x - w, y);
			g.drawString(str, x + w, y);
			g.drawString(str, x, y - w);
			g.drawString(str, x, y + w);
		}
		// Fill
		if (paint.getFill() != null) {
			g.setPaint(getPaint(paint.getFill()));
			g.drawString(str, x, y);
		}

	}

	@Override
	public void setFont(EAdFont font) {
		DesktopEngineFont f = (DesktopEngineFont) fontHandler.get(font);
		g.setFont(f.getFont());
	}

	@Override
	public void clip(EAdRectangle rectangle) {
		g.clipRect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(),
				rectangle.getHeight());
	}

	private Paint getPaint(EAdFill fill) {
		// FIXME without this, elements sharing gradient has weird effect (like
		// if they shared the same gradient)
		if (fill instanceof EAdLinearGradient) {
			EAdLinearGradient gradient = (EAdLinearGradient) fill;
			Color c1 = (Color) getPaint(gradient.getColor1());
			Color c2 = (Color) getPaint(gradient.getColor2());
			return new GradientPaint(gradient.getX0(), gradient.getY0(), c1,
					gradient.getX1(), gradient.getY1(), c2);
		}
		Paint p = fillCache.get(fill);
		if (p == null) {
			if (fill instanceof EAdColor) {
				EAdColor c = (EAdColor) fill;
				p = new Color(c.getRed(), c.getGreen(), c.getBlue(),
						c.getAlpha());
			} else if (fill instanceof EAdLinearGradient) {
				EAdLinearGradient gradient = (EAdLinearGradient) fill;
				Color c1 = (Color) getPaint(gradient.getColor1());
				Color c2 = (Color) getPaint(gradient.getColor2());
				p = new GradientPaint(gradient.getX0(), gradient.getY0(), c1,
						gradient.getX1(), gradient.getY1(), c2);
			}
			fillCache.put(fill, p);
		}
		return p;
	}

	private Stroke getStroke(EAdPaint p) {
		Stroke s = strokeCache.get(p);
		if (s == null) {
			s = new BasicStroke(p.getBorderWidth());
			strokeCache.put(p, s);
		}
		return s;
	}

	@Override
	public void save() {
		Graphics2D g2 = (Graphics2D) g.create();
		graphicStack.push(g);
		g = g2;
	}

	@Override
	public void restore() {
		try {
			g = graphicStack.pop();
		} catch (EmptyStackException e) {
			logger.severe("Attempted to restore canvas when there was nothing to restore. You may revise the use of save and restore on your code.");
		}
	}

	@Override
	public void translate(int x, int y) {
		g.translate(x, y);
	}

}
