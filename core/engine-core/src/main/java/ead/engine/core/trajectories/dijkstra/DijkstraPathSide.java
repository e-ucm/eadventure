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

import ead.common.model.elements.trajectories.Side;
import ead.common.util.EAdPosition;
import ead.engine.core.trajectories.PathSide;

/**
 * A side in the representation of the trajectory used to find the best path
 * using the Dijkstra algorithm
 */
public class DijkstraPathSide implements PathSide {

	/**
	 * Proportional length of the side according to the model
	 */
	private float length;

	/**
	 * Cartesian length of the side
	 */
	private float realLength;

	/**
	 * Start node of the side
	 */
	private DijkstraNode start;

	/**
	 * End node of the side
	 */
	private DijkstraNode end;

	/**
	 * The end position of the side, dependent on the direction
	 */
	private EAdPosition endPosition;

	/**
	 * The model side of which this is part
	 */
	private Side side;

	private float endScale;

	public DijkstraPathSide(DijkstraNode s, DijkstraNode e, double length,
			double realLength, Side side) {
		this.length = (float) length;
		this.realLength = (float) realLength;
		this.start = s;
		this.end = e;
		this.side = side;
	}

	@Override
	public float getLength() {
		return length;
	}

	@Override
	public float getSpeedFactor() {
		return realLength / length;
	}

	/**
	 * Get the node of the side that is not the give one
	 * 
	 * @param node One node in the side
	 * @return The other node in the side
	 */
	public DijkstraNode getOtherNode(DijkstraNode node) {
		if (node == start)
			return end;
		return start;
	}

	@Override
	public EAdPosition getEndPosition(boolean last) {
		return endPosition;
	}

	public DijkstraNode getEndNode() {
		return end;
	}

	public DijkstraNode getStartNode() {
		return start;
	}

	public void setEndPosition(EAdPosition position) {
		this.endPosition = position;
	}

	public Side getSide() {
		return side;
	}

	public void setEndScale(float scale) {
		this.endScale = scale;
	}

	@Override
	public float getEndScale() {
		return endScale;
	}

}
