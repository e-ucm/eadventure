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

import es.eucm.ead.model.params.util.Position;

public class PositionTest extends ParamsTest<Position> {

	@Override
	public Position buildParam(String data) {
		return new Position(data);
	}

	@Override
	public Position defaultValue() {
		return new Position();
	}

	@Override
	public Position[] getObjects() {
		Position[] positions = new Position[20];
		for (int i = 0; i < positions.length; i += 2) {
			positions[i] = new Position(i * 3, i * 4, (float) (i - 1) * 800.f,
					(float) (i) * 600.f);
			positions[i + 1] = new Position(i * 3, i * 4,
					(float) (i - 1) * 800.f, (float) (i) * 600.f);
		}
		return positions;
	}

}
