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

package es.eucm.eadventure.engine.core.trajectories.impl.dijkstra;

import java.util.ArrayList;
import java.util.List;

import es.eucm.eadventure.common.util.EAdPositionImpl;

/**
 * A node in the representation of the trajectory used to find the best path
 * using the Dijkstra algorithm
 */
public class DijkstraNode {

	private EAdPositionImpl position;

	private int goalDistance;

	private boolean breakNode;

	private List<DijkstraPathSide> sides;

	private boolean getsTo;

	private float linePosition;

	private float scale;
	
	public DijkstraNode(EAdPositionImpl position) {
		this.position = position;
		this.sides = new ArrayList<DijkstraPathSide>();
	}

	public DijkstraNode(EAdPositionImpl position, float linePosition) {
		this.position = position;
		this.sides = new ArrayList<DijkstraPathSide>();
		this.linePosition = linePosition;
	}

	public void calculateGoalDistance(int toX, int toY) {
		goalDistance = (int) Math.sqrt(Math.pow(toX - position.getX(), 2)
				+ Math.pow(toY - position.getY(), 2));
	}

	/**
	 * @return the distance from this node to the goal position
	 */
	public int getGoalDistance() {
		return goalDistance;
	}

	public void setBreakNode(boolean breakNode) {
		this.breakNode = breakNode;
	}

	/**
	 * @return true if the node does not interconnect the sides that end or
	 *         start on it
	 */
	public boolean isBreakNode() {
		return breakNode;
	}

	public EAdPositionImpl getPosition() {
		return position;
	}

	public void addSide(DijkstraPathSide pathSide) {
		sides.add(pathSide);
	}

	public List<DijkstraPathSide> getSides() {
		return sides;
	}

	/**
	 * @return true if the node is within or at the egde of the influence area
	 *         of the sceneElement that needs to be reached (false if there is
	 *         no sceneElement)
	 */
	public boolean isGetsTo() {
		return getsTo;
	}

	public void setGetsTo(boolean getsTo) {
		this.getsTo = getsTo;
	}
	
	public float getLinePosition() {
		return linePosition;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public float getScale() {
		return scale;
	}
	
}
