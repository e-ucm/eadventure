/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 *    <e-UCM> research group.
 *
 *    Copyright 2005-2010 <e-UCM> research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    <e-UCM> is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure, version 2.0
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

package ead.engine.core.platform.rendering;

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

import ead.common.params.fills.ColorFill;
import ead.common.params.fills.LinearGradientFill;
import ead.common.params.paint.EAdFill;
import ead.common.params.paint.EAdPaint;
import ead.common.params.text.EAdFont;
import ead.common.resources.assets.drawable.basics.EAdShape;
import ead.common.util.EAdMatrix;
import ead.common.util.EAdRectangle;
import ead.engine.core.platform.DrawableAsset;
import ead.engine.core.platform.FontHandler;
import ead.engine.core.platform.assets.DesktopBezierShape;
import ead.engine.core.platform.assets.DesktopEngineFont;
import ead.engine.core.platform.rendering.AbstractCanvas;
import ead.engine.core.platform.rendering.filters.FilterFactory;
import ead.engine.core.util.EAdTransformation;

public class DesktopCanvas extends AbstractCanvas<Graphics2D> {

	private Map<EAdFill, Paint> fillCache;

	private Map<EAdPaint, Stroke> strokeCache;

	private Stack<Graphics2D> graphicStack;

	private AlphaComposite alphaComposite;

	@Inject
	public DesktopCanvas(FontHandler fontHandler,
			FilterFactory<Graphics2D> filterFactory) {
		super(fontHandler, filterFactory);
		fillCache = new HashMap<EAdFill, Paint>();
		strokeCache = new HashMap<EAdPaint, Stroke>();
		graphicStack = new Stack<Graphics2D>();
		alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				1.0f);
	}

	@Override
	public void setTransformation(EAdTransformation t) {
		setMatrix(t.getMatrix());
		if (t.getClip() != null)
			clip(t.getClip());
		if (alphaComposite.getAlpha() != t.getAlpha()) {
			alphaComposite = AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, t.getAlpha());
			g.setComposite(alphaComposite);
		}
	}

	public void setMatrix(EAdMatrix mat) {
		g.setTransform(getAffineTransform(mat));
	}

	public AffineTransform getAffineTransform(EAdMatrix mat) {
		float m[] = mat.getFlatMatrix();
		return new AffineTransform(m[0], m[1], m[3], m[4], m[6], m[7]);
	}

	@Override
	public void drawShape(DrawableAsset<? extends EAdShape, Graphics2D> shape) {
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
	public void fillRect(int x, int y, int width, int height) {
		// Fill
		if (paint.getFill() != null) {
			g.setPaint(getPaint(paint.getFill()));
			g.fillRect(x, y, width, height);
		}
		// Border
		if (paint.getBorder() != null && paint.getBorderWidth() > 0) {
			g.setPaint(getPaint(paint.getBorder()));
			g.setStroke(getStroke(paint));
			g.drawRect(x, y, width, height);
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
		if (fill instanceof LinearGradientFill) {
			LinearGradientFill gradient = (LinearGradientFill) fill;
			Color c1 = (Color) getPaint(gradient.getColor1());
			Color c2 = (Color) getPaint(gradient.getColor2());
			return new GradientPaint(gradient.getX0(), gradient.getY0(), c1,
					gradient.getX1(), gradient.getY1(), c2);
		}
		Paint p = fillCache.get(fill);
		if (p == null) {
			if (fill instanceof ColorFill) {
				ColorFill c = (ColorFill) fill;
				p = new Color(c.getRed(), c.getGreen(), c.getBlue(),
						c.getAlpha());
			} else if (fill instanceof LinearGradientFill) {
				LinearGradientFill gradient = (LinearGradientFill) fill;
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
			logger.error("Attempted to restore canvas when there was nothing to restore. "
                + "Check the use of save and restore on your code.");
		}
	}

	@Override
	public void translate(int x, int y) {
		g.translate(x, y);
	}

	@Override
	public void scale(float scaleX, float scaleY) {
		g.scale(scaleX, scaleY);

	}

	@Override
	public void rotate(float angle) {
		g.rotate(angle);
	}

}
