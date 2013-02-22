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

package ead.engine.core.assets.drawables.shapes;

import com.badlogic.gdx.graphics.Pixmap;
import com.google.inject.Inject;

import ead.common.model.assets.drawable.basics.shapes.CircleShape;
import ead.common.model.params.fills.ColorFill;
import ead.common.model.params.fills.LinearGradientFill;
import ead.common.model.params.paint.EAdFill;
import ead.common.model.params.paint.EAdPaint;
import ead.engine.core.assets.AssetHandler;

public class GdxCircleShape extends RuntimeShape<CircleShape> {

	@Inject
	public GdxCircleShape(AssetHandler assetHandler) {
		super(assetHandler);
	}

	@Override
	protected Pixmap generatePixmap() {
		EAdPaint paint = descriptor.getPaint();
		EAdFill fill = paint.getFill();
		EAdFill border = paint.getBorder();
		int borderWidth = paint.getBorderWidth();
		int size = descriptor.getRadius() * 2 + borderWidth * 2 + 1;
		int center = size / 2;

		Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
		pixmapContains = new Pixmap(size, size, Pixmap.Format.RGBA8888);
		pixmapContains.setColor(0, 0, 0, 1);
		pixmapContains.fillCircle(center, center, descriptor.getRadius()
				+ borderWidth);

		ColorFill c = ColorFill.TRANSPARENT;
		if (border != null) {
			if (border instanceof ColorFill) {
				c = (ColorFill) border;

			} else if (border instanceof LinearGradientFill) {
				LinearGradientFill l = (LinearGradientFill) border;
				c = l.getColor1();
			}

			pixmap.setColor(c.getRed() / 255.0f, c.getGreen() / 255.0f, c
					.getBlue() / 255.0f, c.getAlpha() / 255.0f);
			pixmap.drawCircle(center, center, descriptor.getRadius()
					+ borderWidth);
		}

		if (fill instanceof ColorFill) {
			c = (ColorFill) fill;
			pixmap.setColor(c.getRed() / 255.0f, c.getGreen() / 255.0f, c
					.getBlue() / 255.0f, c.getAlpha() / 255.0f);
			pixmap.fillCircle(center, center, descriptor.getRadius());
		} else if (fill instanceof LinearGradientFill) {
			LinearGradientFill l = (LinearGradientFill) fill;
			initGradientParams(l.getColor1(), l.getX0(), l.getY0(), l
					.getColor2(), l.getX1(), l.getY1());
			int size2 = descriptor.getRadius() * descriptor.getRadius();
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					int s1 = (i - center);
					s1 = s1 * s1;
					int s2 = (j - center);
					s2 = s2 * s2;
					if (s1 + s2 < size2) {
						setColor(pixmap, i, j);
						pixmap.drawPixel(i, j);
					}
				}
			}
		}

		return pixmap;
	}

}
