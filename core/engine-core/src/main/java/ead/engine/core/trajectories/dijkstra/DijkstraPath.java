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

package ead.engine.core.trajectories.dijkstra;

import java.util.ArrayList;
import java.util.List;

import ead.engine.core.trajectories.Path;
import ead.engine.core.trajectories.PathSide;

/**
 * A path in the representation of the trajectory used to find the best path
 * using the Dijkstra algorithm
 */
public class DijkstraPath implements Path {

	/**
	 * The list of sides in this path
	 */
	private List<PathSide> sides;

	/**
	 * True if the path gets to the influence area of a target element (when available)
	 */
	private boolean getsTo;

	public DijkstraPath() {
		sides = new ArrayList<PathSide>();
	}

	@Override
	public List<PathSide> getSides() {
		return sides;
	}

	/**
	 * Sides are added in reverse order given the needs of the algorithm, which
	 * goes backwards from the best node reconstructing the path
	 * 
	 * @param side
	 */
	public void addSide(DijkstraPathSide side) {
		sides.add(0, side);
	}

	@Override
	public boolean isGetsTo() {
		return getsTo;
	}

	public void setGetsTo(boolean getsTo) {
		this.getsTo = getsTo;
	}

}
