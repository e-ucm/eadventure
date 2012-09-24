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

import com.badlogic.gdx.graphics.Pixmap;

import ead.common.params.fills.ColorFill;
import ead.common.params.fills.LinearGradientFill;
import ead.common.params.paint.EAdFill;
import ead.common.params.paint.EAdPaint;
import ead.common.resources.assets.drawable.basics.shapes.RectangleShape;

public class GdxRectangleShape extends GdxShape<RectangleShape> {

	@Override
	protected Pixmap generatePixmap() {

		EAdPaint paint = descriptor.getPaint();
		EAdFill border = paint.getBorder();
		EAdFill fill = paint.getFill();
		int borderWidth = paint.getBorderWidth();

		int pwidth = descriptor.getWidth() + borderWidth * 2;
		int pheight = descriptor.getHeight() + borderWidth * 2;

		Pixmap pixmap = new Pixmap(pwidth, pheight, Pixmap.Format.RGBA8888);
		pixmapContains = new Pixmap(pwidth, pheight, Pixmap.Format.RGBA8888);
		pixmapContains.setColor(0, 0, 0, 1);
		pixmapContains.fillRectangle(0, 0, pwidth, pheight);

		ColorFill c = ColorFill.TRANSPARENT;
		if (border != null) {
			if (border instanceof ColorFill) {
				c = (ColorFill) border;

			} else if (border instanceof LinearGradientFill) {
				LinearGradientFill l = (LinearGradientFill) border;
				c = l.getColor1();
			}
			pixmap.setColor(c.getRed() / 255.0f, c.getGreen() / 255.0f,
					c.getBlue() / 255.0f, c.getAlpha() / 255.0f);
			pixmap.fillRectangle(0, 0, pwidth, pheight);			
		}

		if (fill instanceof ColorFill) {
			c = (ColorFill) fill;
			pixmap.setColor(c.getRed() / 255.0f, c.getGreen() / 255.0f,
					c.getBlue() / 255.0f, c.getAlpha() / 255.0f);
			pixmap.fillRectangle(borderWidth, borderWidth,
					descriptor.getWidth(), descriptor.getHeight());
		} else if (fill instanceof LinearGradientFill) {
			LinearGradientFill l = (LinearGradientFill) fill;
			initGradientParams(l.getColor1(), l.getX0(), l.getY0(),
					l.getColor2(), l.getX1(), l.getY1());
			for (int i = borderWidth; i < descriptor.getWidth() + borderWidth; i++) {
				for (int j = borderWidth; j < descriptor.getHeight()
						+ borderWidth; j++) {
					setColor(pixmap, i, j);
					pixmap.drawPixel(i, j);
				}
			}
		}

		return pixmap;
	}
}
