package es.eucm.eadventure.common.model.trajectories.impl;

import com.gwtent.reflection.client.Reflectable;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.extra.impl.EAdListImpl;
import es.eucm.eadventure.common.model.impl.EAdElementImpl;
import es.eucm.eadventure.common.model.trajectories.TrajectoryDefinition;
import es.eucm.eadventure.common.model.variables.EAdVarDef;
import es.eucm.eadventure.common.model.variables.impl.EAdVarDefImpl;
import es.eucm.eadventure.common.params.geom.EAdRectangle;

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
	public static final EAdVarDef<Boolean> VAR_BARRIER_ON = new EAdVarDefImpl<Boolean>(
			"barrierOn", Boolean.class, Boolean.FALSE);

	public static final EAdVarDef<EAdRectangle> VAR_INFLUENCE_AREA = new EAdVarDefImpl<EAdRectangle>(
			"influence_area", EAdRectangle.class, null);

	/**
	 * Variable for the current side
	 */
	public static final EAdVarDef<Side> VAR_CURRENT_SIDE = new EAdVarDefImpl<Side>(
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
