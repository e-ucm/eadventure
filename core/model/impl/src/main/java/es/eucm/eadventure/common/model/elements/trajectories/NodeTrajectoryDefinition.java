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

package es.eucm.eadventure.common.model.elements.trajectories;

import com.gwtent.reflection.client.Reflectable;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.elements.EAdElementImpl;
import es.eucm.eadventure.common.model.elements.extra.EAdList;
import es.eucm.eadventure.common.model.elements.extra.EAdListImpl;
import es.eucm.eadventure.common.model.elements.scene.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.trajectories.TrajectoryDefinition;
import es.eucm.eadventure.common.model.elements.variables.EAdVarDef;
import es.eucm.eadventure.common.model.elements.variables.VarDefImpl;
import es.eucm.eadventure.common.util.EAdRectangleImpl;

/**
 * 
 * Trajectory based on nodes and sides, originally developed in e-Adventure 1.X
 * 
 */
@Reflectable
@Element(detailed = NodeTrajectoryDefinition.class, runtime = NodeTrajectoryDefinition.class)
public class NodeTrajectoryDefinition extends EAdElementImpl implements
		TrajectoryDefinition {

	/**
	 * Variable's definition for whether a barrier is on or not
	 */
	public static final EAdVarDef<Boolean> VAR_BARRIER_ON = new VarDefImpl<Boolean>(
			"barrierOn", Boolean.class, Boolean.FALSE);

	public static final EAdVarDef<EAdRectangleImpl> VAR_INFLUENCE_AREA = new VarDefImpl<EAdRectangleImpl>(
			"influence_area", EAdRectangleImpl.class, null);

	/**
	 * Variable for the current side
	 */
	public static final EAdVarDef<Side> VAR_CURRENT_SIDE = new VarDefImpl<Side>(
			"currentSide", Side.class, null);

	@Param("nodes")
	private EAdList<Node> nodes;

	@Param("sides")
	private EAdList<Side> sides;

	@Param("barriers")
	private EAdList<EAdSceneElement> barriers;

	@Param("initial")
	private Node initial;

	public NodeTrajectoryDefinition() {
		nodes = new EAdListImpl<Node>(Node.class);
		sides = new EAdListImpl<Side>(Side.class);
		barriers = new EAdListImpl<EAdSceneElement>(EAdSceneElement.class);
		initial = null;
	}

	public Node addNode(String id, int x, int y, float scale) {

		Node node = new Node(id, x, y, scale);
		if (nodes.contains(node)) {
			node = nodes.get(nodes.indexOf(node));
		} else {
			nodes.add(node);
		}
		if (nodes.size() == 1) {
			initial = nodes.get(0);
		}
		return node;
	}

	public Side addSide(String idStart, String idEnd, float length) {

		if (idStart.equals(idEnd))
			return null;
		Side side = new Side(idStart, idEnd);
		Node a = getNodeForId(idStart);
		Node b = getNodeForId(idEnd);
		if (a != null && b != null) {
			int x = a.getX() - b.getX();
			int y = a.getY() - b.getY();
			if (length == -1)
				side.setLength((float) Math.sqrt(x * x + y * y));
			else
				side.setLength(length);
			side.setRealLength((float) Math.sqrt(x * x + y * y));
		}

		if (sides.contains(side)) {
			return null;
		} else {
			sides.add(side);
		}
		return side;
	}

	public Node getNodeForId(String id) {

		if (id == null)
			return null;
		for (Node node : nodes) {
			if (id.equals(node.getId()))
				return node;
		}
		return null;
	}

	public void setInitial(String id) {

		initial = getNodeForId(id);
	}

	public Node getInitial() {

		return initial;
	}
	
	public void setInitial(Node initial) {
		this.initial = initial;
	}

	public EAdList<Node> getNodes() {

		return nodes;
	}

	public EAdList<Side> getSides() {

		return sides;
	}

	public void addBarrier(EAdSceneElement barrier) {
		barriers.add(barrier);
	}

	public EAdList<EAdSceneElement> getBarriers() {
		return barriers;
	}

}
