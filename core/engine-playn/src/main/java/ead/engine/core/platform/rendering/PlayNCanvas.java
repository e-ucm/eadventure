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

import playn.core.Canvas;
import playn.core.Canvas.Composite;
import playn.core.Color;
import playn.core.Font;
import playn.core.Gradient;
import playn.core.Path;
import playn.core.PlayN;
import playn.core.TextFormat;
import playn.core.TextFormat.Effect;

import com.google.inject.Inject;

import ead.common.params.fills.ColorFill;
import ead.common.params.fills.LinearGradientFill;
import ead.common.params.paint.EAdFill;
import ead.common.resources.assets.drawable.basics.EAdShape;
import ead.common.resources.assets.text.EAdFont;
import ead.common.util.EAdMatrix;
import ead.common.util.EAdRectangle;
import ead.common.util.ReflectionProvider;
import ead.engine.core.platform.FontHandler;
import ead.engine.core.platform.assets.RuntimeDrawable;
import ead.engine.core.platform.assets.drawable.basics.PlayNBezierShape;
import ead.engine.core.platform.rendering.AbstractCanvas;
import ead.engine.core.util.EAdTransformation;

public class PlayNCanvas extends AbstractCanvas<Canvas> {

	private Font f;

	@Inject
	public PlayNCanvas(FontHandler fontHandler, ReflectionProvider reflectionProvider) {
		super(fontHandler, new PlayNFilterFactory(reflectionProvider));
	}
	
	public void setGraphicContext( Canvas g ){
		super.setGraphicContext(g);
		g.setCompositeOperation(Composite.SRC_OVER);
	} 

	@Override
	public void setTransformation(EAdTransformation t) {
		setMatrix( t.getMatrix() );
		if (t.getClip() != null)
			setClip(t.getClip());
		g.setAlpha(t.getAlpha());
	}
	
	public void setMatrix( EAdMatrix mat ){
		float[] m = mat.getFlatMatrix();
		g.setTransform(m[0], m[1], m[3], m[4], m[6], m[7]);
	}

	@Override
	public void drawShape(RuntimeDrawable<? extends EAdShape, Canvas> shape) {
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
	
	@Override
	public void fillRect(int x, int y, int width, int height) {
		// Fill
		if (paint.getFill() != null) {
			updatePaintFill(paint.getFill());
			g.fillRect(x, y, width, height);
		}
		// Border
		if (paint.getBorder() != null && paint.getBorderWidth() > 0) {
			updatePaintBorder(paint.getBorder());
			g.setStrokeWidth(paint.getBorderWidth());
			g.fillRect(x, y, width, height);
		}
	}

	private void updatePaintFill(EAdFill fill) {
		if (fill instanceof ColorFill) {
			ColorFill c = (ColorFill) fill;
			g.setFillColor(Color.argb(c.getAlpha(), c.getRed(), c.getGreen(),
					c.getBlue()));
		} else if (fill instanceof LinearGradientFill) {
			LinearGradientFill gradient = (LinearGradientFill) fill;
			ColorFill c = gradient.getColor1();
			int cint1 = Color.argb(c.getAlpha(), c.getRed(), c.getGreen(),
					c.getBlue());
			c = gradient.getColor2();
			int cint2 = Color.argb(c.getAlpha(), c.getRed(), c.getGreen(),
					c.getBlue());
			Gradient gr = PlayN.graphics().createLinearGradient(
					gradient.getX0(), gradient.getY0(), gradient.getX1(),
					gradient.getY1(), new int[] { cint1, cint2 },
					new float[] { 0, 1 });
			g.setFillGradient(gr);
		}
	}

	private void updatePaintBorder(EAdFill border) {
		ColorFill c = ColorFill.BLACK;
		if (border instanceof ColorFill) {
			c = (ColorFill) border;
		} else if (border instanceof LinearGradientFill) {
			c = ((LinearGradientFill) border).getColor1();
		}
		g.setStrokeColor(Color.argb(c.getAlpha(), c.getRed(), c.getGreen(),
				c.getBlue()));
	}

	@Override
	public void drawText(String text, int x, int y) {
		TextFormat format = new TextFormat(f, 10000.0f,
				TextFormat.Alignment.LEFT, getColor(paint.getFill()),
				Effect.outline(getColor(paint.getBorder())));
		g.drawText(PlayN.graphics().layoutText(text, format), x, y);
	}

	@Override
	public void setFont(EAdFont font) {
		// FIXME the 1.1f must have some reason, maybe because use pt instaed of
		// px
		f = PlayN.graphics().createFont(font.getName(), getStyle(font),
				font.getSize() / 1.1f);

	}

	private playn.core.Font.Style getStyle(EAdFont font) {
		switch (font.getStyle()) {
		case BOLD:
			return playn.core.Font.Style.BOLD;
		case ITALIC:
			return playn.core.Font.Style.ITALIC;
		default:
			return playn.core.Font.Style.PLAIN;
		}
	}

	@Override
	public void setClip(EAdRectangle rectangle) {
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

	private int getColor(EAdFill fill) {
		ColorFill c = ColorFill.TRANSPARENT;
		if (fill instanceof ColorFill) {
			c = (ColorFill) fill;
		} else if (fill instanceof LinearGradientFill) {
			c = ((LinearGradientFill) fill).getColor1();
		}
		return Color.argb(c.getAlpha(), c.getRed(), c.getGreen(), c.getBlue());
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
