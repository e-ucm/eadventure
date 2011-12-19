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

package es.eucm.eadventure.engine.rendering;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.Typeface;

import com.google.inject.Inject;

import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.fills.impl.EAdLinearGradient;
import es.eucm.eadventure.common.params.geom.EAdRectangle;
import es.eucm.eadventure.common.params.paint.EAdFill;
import es.eucm.eadventure.common.params.text.EAdFont;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Shape;
import es.eucm.eadventure.common.util.EAdMatrix;
import es.eucm.eadventure.common.util.EAdTransformation;
import es.eucm.eadventure.engine.assets.AndroidBezierShape;
import es.eucm.eadventure.engine.assets.AndroidEngineFont;
import es.eucm.eadventure.engine.core.platform.DrawableAsset;
import es.eucm.eadventure.engine.core.platform.FontHandler;
import es.eucm.eadventure.engine.core.platform.impl.rendering.AbstractCanvas;
import es.eucm.eadventure.engine.core.platform.rendering.filters.FilterFactory;

public class AndroidCanvas extends AbstractCanvas<Canvas> {

	private Map<EAdFill, Paint> fillCache;
	
	private Typeface font;
	
	private int size;

	@Inject
	public AndroidCanvas(FontHandler fontHandler, FilterFactory<Canvas> filterFactory) {
		super(fontHandler, filterFactory);
		fillCache = new HashMap<EAdFill, Paint>();
	}

	@Override
	public void setTransformation(EAdTransformation t) {
		
		setMatrix( t.getMatrix() );
		// TODO alpha
	}
	
	public void setMatrix( EAdMatrix mat ){
		g.restore();
		
		float m[] = mat.getTransposedMatrix();
		Matrix matrix = new Matrix();
		matrix.setValues(m);
		matrix.postConcat(g.getMatrix());
		
		g.save();		
		g.setMatrix(matrix);
	}
	
	public Matrix getMatrix( EAdMatrix mat ){
		float m[] = mat.getTransposedMatrix();
		Matrix matrix = new Matrix();
		matrix.setValues(m);
		return matrix;
	}

	@Override
	public void drawShape(DrawableAsset<? extends Shape, Canvas> shape) {
		Path s = ((AndroidBezierShape) shape).getShape();
		// Fill
		if (paint.getFill() != null) {
			Paint p = getPaint(paint.getFill());
			p.setStyle(Style.FILL);
			g.drawPath(s, p);
		}
		// Border
		if (paint.getBorder() != null && paint.getBorderWidth() > 0) {
			Paint p = getPaint(paint.getBorder());
			p.setStyle(Style.STROKE);
			p.setStrokeWidth(paint.getBorderWidth());
			g.drawPath(s, p);
		}
	}
	
	@Override
	public void fillRect(int x, int y, int width, int height) {
		// Fill
		if (paint.getFill() != null) {
			Paint p = getPaint(paint.getFill());
			p.setStyle(Style.FILL);
			g.drawRect(x, y, x + width, y + height, p);
		}
		// Border
		if (paint.getBorder() != null && paint.getBorderWidth() > 0) {
			Paint p = getPaint(paint.getBorder());
			p.setStyle(Style.STROKE);
			p.setStrokeWidth(paint.getBorderWidth());
			g.drawRect(x, y, x + width, y + height, p);
		}
	}

	@Override
	public void drawText(String str, int x, int y) {
		// Fill
		if (paint.getFill() != null) {
			Paint p = getPaint(paint.getFill());
			p.setStyle(Style.FILL);
			p.setTypeface(font);
			p.setTextSize(size);
			g.drawText(str, x, y, p);
		}
		// Border
		if (paint.getBorder() != null && paint.getBorderWidth() > 0) {
			Paint p = getPaint(paint.getBorder());
			p.setStyle(Style.STROKE);
			p.setStrokeWidth(paint.getBorderWidth());
			p.setTypeface(font);
			p.setTextSize(size);
			g.drawText(str, x, y, p);
		}
	}

	@Override
	public void setFont(EAdFont font) {
		AndroidEngineFont f = (AndroidEngineFont) fontHandler.get(font);
		this.font = f.getFont();
		size = f.size();
	}

	@Override
	public void clip(EAdRectangle rectangle) {
		g.clipRect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(),
				rectangle.getHeight());
	}

	private Paint getPaint(EAdFill fill) {
		Paint p = fillCache.get(fill);
		if (p == null) {
			p = new Paint();
			if (fill instanceof EAdColor) {
				EAdColor c = (EAdColor) fill;
				p.setColor(Color.argb(c.getAlpha(), c.getRed(), c.getGreen(),
						c.getBlue()));

			} else if (fill instanceof EAdLinearGradient) {
				EAdLinearGradient gradient = (EAdLinearGradient) fill;
				EAdColor c = gradient.getColor1();
				int c1 = Color.argb(c.getAlpha(), c.getRed(), c.getGreen(),
						c.getBlue());
				c = gradient.getColor2();
				int c2 = Color.argb(c.getAlpha(), c.getRed(), c.getGreen(),
						c.getBlue());
				p.setShader(new LinearGradient(gradient.getX0(), gradient
						.getY1(), gradient.getX1(), gradient.getY0(), c1, c2,
						Shader.TileMode.REPEAT));
			}
			fillCache.put(fill, p);
		}
		return p;
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

	@Override
	public void scale(float scaleX, float scaleY) {
		g.scale(scaleX, scaleY);
	}

	@Override
	public void rotate(float angle) {
		g.rotate((float) Math.toDegrees(angle));
	}

}
