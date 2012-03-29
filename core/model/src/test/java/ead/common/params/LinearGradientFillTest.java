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

package ead.common.params;

import ead.common.params.fills.ColorFill;
import ead.common.params.fills.LinearGradientFill;

public class LinearGradientFillTest extends ParamsTest<LinearGradientFill> {

	@Override
	public LinearGradientFill[] getObjects() {
		LinearGradientFill[] fills = new LinearGradientFill[20];
		for (int i = 0; i < fills.length; i += 2) {
			ColorFill c1 = new ColorFill(i * 3, i * 5, i * 7);
			ColorFill c2 = new ColorFill(i * 4, i * 1, i * 8);
			float x1 = i * 50;
			float y1 = i * 100;
			float x2 = i * 20;
			float y2 = i * 70;
			LinearGradientFill fill1 = new LinearGradientFill(c1, c2, x1, y1,
					x2, y2);
			ColorFill c3 = new ColorFill(i * 3, i * 5, i * 7);
			ColorFill c4 = new ColorFill(i * 4, i * 1, i * 8);
			LinearGradientFill fill2 = new LinearGradientFill(c3, c4, x1, y1,
					x2, y2);
			fills[i] = fill1;
			fills[i + 1] = fill2;
		}

		return fills;
	}

	@Override
	public LinearGradientFill buildParam(String data) {
		return new LinearGradientFill(data);
	}

	@Override
	public LinearGradientFill defaultValue() {
		return LinearGradientFill.BLACK;
	}

}
