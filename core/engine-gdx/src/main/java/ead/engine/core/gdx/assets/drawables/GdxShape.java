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

package ead.engine.core.gdx.assets.drawables;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

import ead.common.model.elements.extra.EAdList;
import ead.common.params.fills.ColorFill;
import ead.common.params.fills.LinearGradientFill;
import ead.common.params.paint.EAdPaint;
import ead.engine.core.platform.assets.drawables.basics.RuntimeBezierShape;
import ead.engine.core.platform.rendering.GenericCanvas;

public class GdxShape extends RuntimeBezierShape<SpriteBatch> {

	private TextureRegion textureRegion;
	private Pixmap pixmapContains;

	public boolean loadAsset() {
		if (!super.loadAsset()) {
			return false;
		}
		
		ArrayList<Float> shape = new ArrayList<Float>( );
		float x0, y0, x1, y1, x2, y2, x3, y3;
		EAdList<Integer> pointsList = descriptor.getPoints();
		x0 = pointsList.get(0);
		y0 = pointsList.get(1);
		shape.add(x0);
		shape.add(y0);

		int pointIndex = 2;

		while (pointIndex < pointsList.size()) {
			int length = pointsList.get(pointIndex++);
			switch (length) {
			case 1:
				x1 = pointsList.get(pointIndex++);
				y1 = pointsList.get(pointIndex++);
				lineTo(x1, y1, shape);
				x0 = x1;
				y0 = y1;
				break;
			case 2:
				x1 = pointsList.get(pointIndex++);
				y1 = pointsList.get(pointIndex++);
				x2 = pointsList.get(pointIndex++);
				y2 = pointsList.get(pointIndex++);
				quadTo(x0, y0, x1, y1, x2, y2, shape);
				x0 = x2;
				y0 = y2;
				break;
			case 3:
				x1 = pointsList.get(pointIndex++);
				y1 = pointsList.get(pointIndex++);
				x2 = pointsList.get(pointIndex++);
				y2 = pointsList.get(pointIndex++);
				x3 = pointsList.get(pointIndex++);
				y3 = pointsList.get(pointIndex++);
				curveTo(x0, y0, x1, y1, x2, y2, x3, y3, shape);
				x0 = x3;
				y0 = y3;
				break;
			default:

			}
		}

		// TODO Probably this can be improved

		EAdPaint p = descriptor.getPaint();
		
		float f[] = new float[shape.size()];
		for ( int i = 0; i < shape.size(); i++ ){
			f[i] = shape.get(i);
		}
		Polygon polygon = new Polygon( f );

		Rectangle rectangle = polygon.getBoundingRectangle(); 
		
		int borderWidth = p.getBorderWidth();
		int x = (int) rectangle.x;
		int y = (int) rectangle.y;
		width = (int) ( rectangle.x + rectangle.width);
		height = (int) ( rectangle.y + rectangle.height );
		
		Pixmap pixmap = new Pixmap(width + borderWidth * 2, height + borderWidth * 2,
				Pixmap.Format.RGBA8888);
		pixmapContains = new Pixmap(width + borderWidth * 2, height + borderWidth * 2,
				Pixmap.Format.RGBA8888);
		pixmapContains.setColor(Color.BLACK);
		

		pixmap.setColor(0, 0, 0, 0);
		pixmap.fill();

		if (p.getFill() instanceof ColorFill) {
			ColorFill c = (ColorFill) p.getFill();
			pixmap.setColor(c.getRed() / 255.0f, c.getGreen() / 255.0f,
					c.getBlue() / 255.0f, c.getAlpha() / 255.0f);
		} else if (p.getFill() instanceof LinearGradientFill) {
			LinearGradientFill gradient = (LinearGradientFill) p.getFill();
			usingGradient = true;
			this.initGradientParams(gradient.getColor1(), gradient.getX0(),
					gradient.getY0(), gradient.getColor2(), gradient.getX1(),
					gradient.getY1());

		} else {
			pixmap.setColor(0, 0, 0, 1);
		}

		
		for (int i = x; i < width; i++) {
			for (int j = y; j < height; j++) {
				if (polygon.contains(i, j)) {
					if (usingGradient) {
						this.setColor(pixmap, borderWidth + i, borderWidth + j);
					}
					pixmap.drawPixel(borderWidth + i, borderWidth + j);
					pixmapContains.drawPixel(borderWidth + i, borderWidth + j);
				}
			}
		}
		usingGradient = false;

		if (p.getBorder() != null) {
			if (p.getBorder() instanceof ColorFill) {
				ColorFill c = (ColorFill) p.getBorder();
				pixmap.setColor(c.getRed() / 255.0f, c.getGreen() / 255.0f,
						c.getBlue() / 255.0f, c.getAlpha() / 255.0f);
			} else if (p.getBorder() instanceof LinearGradientFill) {
				LinearGradientFill gradient = (LinearGradientFill) p
						.getBorder();
				usingGradient = true;
				initGradientParams(gradient.getColor1(), gradient.getX0(),
						gradient.getY0(), gradient.getColor2(),
						gradient.getX1(), gradient.getY1());
			}

			float previousX = 0;
			float previousY = 0;
			float currentX = 0;
			float currentY = 0;
			for ( int k = 0; k < shape.size(); k+=2 ){
				currentX = shape.get(k);
				currentY = shape.get(k + 1);
				if ( k >= 2) {
					for (int i = 1; i <= borderWidth; i++) {
						if (usingGradient) {
							this.setColor(pixmap, (int) previousX + i,
									(int) previousY + i);
						}
						pixmap.drawLine((int) previousX + i,
								(int) previousY + i, (int) currentX + i,
								(int) currentY + i);
						pixmapContains.drawLine((int) previousX + i,
								(int) previousY + i, (int) currentX + i,
								(int) currentY + i);

					}
				}
				previousX = currentX;
				previousY = currentY;
			}
			for (int i = 1; i <= borderWidth; i++) {
				pixmap.drawLine((int) previousX + i, (int) previousY + i,
						(int) shape.get(0).intValue() + i,
						(int) shape.get(1).intValue() + i);
			}

		}
		usingGradient = false;

		textureRegion = new TextureRegion(new Texture(pixmap));
		textureRegion.flip(false, true);
		pixmap.dispose();

		return true;
	}

	@Override
	public boolean contains(int x, int y) {
		if ( x > 0 && y > 0 && x < getWidth() && y < getHeight() ){
			int alpha = pixmapContains.getPixel( x, y ) & 255;
			return alpha > 128;
		}
		return false;
	}

	@Override
	public void freeMemory() {
		this.pixmapContains.dispose();
	}

	private void lineTo(float x1, float y1, List<Float> points) {
		points.add(x1);
		points.add(y1);
	}

	private void quadTo(float x0, float y0, float x1, float y1, float x2,
			float y2, List<Float> points) {
		for (float t = 0.2f; t <= 1.0f; t += 0.2f) {
			float x = (1 - t) * (1 - t) * x0 + 2 * (1 - t) * t * x1 + t * t
					* x2;
			float y = (1 - t) * (1 - t) * y0 + 2 * (1 - t) * t * y1 + t * t
					* y2;
			points.add(x);
			points.add(y);
		}

	}

	private void curveTo(float x0, float y0, float x1, float y1, float x2,
			float y2, float x3, float y3, List<Float> points ) {
		for (float t = 0.2f; t <= 1.0f; t += 0.1f) {
			float _t2 = (1 - t) * (1 - t);
			float _t3 = _t2 * (1 - t);
			float x = _t3 * x0 + 3 * _t2 * t * x1 + 3 * (1 - t) * t * t * x2
					+ t * t * t * x3;
			float y = _t3 * y0 + 3 * _t2 * t * y1 + 3 * (1 - t) * t * t * y2
					+ t * t * t * y3;
			points.add(x);
			points.add(y);
		}
	}

	@Override
	public void render(GenericCanvas<SpriteBatch> c) {
		render(c.getNativeGraphicContext());
	}

	public void render(SpriteBatch batch) {
		batch.draw(textureRegion, 0, 0);
	}

	private static float vBx, vBy;

	private static float vMod2;

	private static float rI, gI, bI, aI;

	private static float rE, gE, bE, aE;

	private static float vcr, vcg, vcb, vca;

	private static boolean usingGradient = false;

	private void initGradientParams(ColorFill c1, float x0, float y0,
			ColorFill c2, float x1, float y1) {
		vBx = x1 - x0;
		vBy = y1 - y0;
		vMod2 = vBx * vBx + vBy * vBy;
		rI = c1.getRed() / 255.f;
		gI = c1.getGreen() / 255.f;
		bI = c1.getBlue() / 255.f;
		aI = c1.getAlpha() / 255.f;
		rE = c2.getRed() / 255.f;
		gE = c2.getGreen() / 255.f;
		bE = c2.getBlue() / 255.f;
		aE = c2.getAlpha() / 255.f;
		vcr = rE - rI;
		vcg = gE - gI;
		vcb = bE - bI;
		vca = aE - aI;
	}

	private void setColor(Pixmap p, float x, float y) {
		float proj = (vBx * x + vBy * y) / vMod2;
		if (proj <= 0) {
			p.setColor(rI, gI, bI, aI);
		} else if (proj >= 1) {
			p.setColor(rE, gE, bE, aE);
		} else {
			p.setColor(rI + vcr * proj, gI + vcg * proj, bI + vcb * proj, aI
					+ vca * proj);
		}
	}

}
