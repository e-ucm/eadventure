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

package ead.engine.core.trajectories;

import java.util.ArrayList;
import java.util.List;

import ead.common.util.EAdPosition;
import ead.engine.core.trajectories.Path;
import ead.engine.core.trajectories.PathSide;

public class SimplePathImpl implements Path {

	private List<PathSide> sides;
	
	public SimplePathImpl(List<EAdPosition> list, EAdPosition currentPosition, float scale) {
		sides = new ArrayList<PathSide>();
		EAdPosition temp = currentPosition;
		for (EAdPosition pos : list) {
			sides.add(new SimpleSideImpl(temp, pos, scale));
			temp = pos;
		}
	}
	
	@Override
	public List<PathSide> getSides() {
		return sides;
	}
	
	@Override
	public boolean isGetsTo() {
		return true;
	}

}
