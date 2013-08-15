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

package ead.common.test.params;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import es.eucm.ead.model.params.fills.ColorFill;

public class ColorFillTest extends ParamsTest<ColorFill> {

	@Test
	public void testMaxMin() {
		// Negative values and values over 255 must be normalized
		ColorFill c = new ColorFill(-1, -20, 1000);
		assertTrue(c.equals(new ColorFill(0, 0, 255)));
		c = new ColorFill(20, 1000, -1020);
		assertTrue(c.equals(new ColorFill(20, 255, 0)));

	}

	@Override
	public ColorFill[] getObjects() {
		ColorFill[] colors = new ColorFill[20];
		for (int i = 0; i < colors.length; i += 2) {
			colors[i] = new ColorFill(1 * i, 7 * i, i * 10);
			colors[i + 1] = new ColorFill(1 * i, 7 * i, i * 10);
		}
		return colors;
	}

	@Test
	public void testWellKnownColors() {
		assertTrue(new ColorFill("0xFF0000FF").equals(ColorFill.RED));
		assertTrue(new ColorFill("0xFFFFFFFF").equals(ColorFill.WHITE));
		assertTrue(new ColorFill("0x000000FF").equals(ColorFill.BLACK));
		assertTrue(new ColorFill("0x0000FFFF").equals(ColorFill.BLUE));
		assertTrue(new ColorFill("0x00FF00FF").equals(ColorFill.GREEN));
	}

	@Override
	public ColorFill buildParam(String data) {
		return new ColorFill(data);
	}

	@Override
	public ColorFill defaultValue() {
		return ColorFill.BLACK;
	}

}
