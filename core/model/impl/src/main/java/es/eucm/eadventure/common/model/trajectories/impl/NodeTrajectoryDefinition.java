package es.eucm.eadventure.common.model.trajectories.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.impl.EAdElementImpl;
import es.eucm.eadventure.common.model.trajectories.TrajectoryDefinition;
import es.eucm.eadventure.common.model.variables.EAdVarDef;
import es.eucm.eadventure.common.model.variables.impl.EAdVarDefImpl;

/**
 * 
 * Trajectory based on nodes and sides, originally developed in e-Adventure 1.X
 * 
 */
public class NodeTrajectoryDefinition extends EAdElementImpl implements
		TrajectoryDefinition {

	/**
	 * Variable's definition for whether a barrier is on or not
	 */
	public static final EAdVarDef<Boolean> VAR_BARRIER_ON = new EAdVarDefImpl<Boolean>(
			"barrierOn", Boolean.class, Boolean.FALSE);

	private List<Node> nodes;

	private List<Side> sides;

	private List<EAdSceneElement> barriers;

	private Node initial;

	public NodeTrajectoryDefinition() {
		nodes = new ArrayList<Node>();
		sides = new ArrayList<Side>();
		barriers = new ArrayList<EAdSceneElement>();
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
				side.setLenght((float) Math.sqrt(x * x + y * y));
			else
				side.setLenght(length);
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
			if (id.equals(node.id))
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

	public List<Node> getNodes() {

		return nodes;
	}

	public List<Side> getSides() {

		return sides;
	}

	public void addBarrier(EAdSceneElement barrier) {
		barriers.add(barrier);
	}

	public class Node implements Cloneable {

		private String id;

		private int x;

		private int y;

		private float scale;

		public Node(String id, int x, int y, float scale) {

			this.id = id;
			this.x = x;
			this.y = y;
			this.scale = scale;
		}

		public String getID() {

			return id;
		}

		public int getX() {

			return x;
		}

		public int getY() {

			return y;
		}

		public float getScale() {

			return scale;
		}

		public void setScale(float scale) {

			this.scale = scale;
		}

		@Override
		public boolean equals(Object o) {

			if (o == null)
				return false;
			if (o instanceof Node) {
				Node node = (Node) o;
				if (node.id.equals(this.id))
					return true;
				if (node.x == this.x && node.y == this.y)
					return true;
			}
			return false;
		}

		public void setValues(int x, int y, float scale) {

			this.x = x;
			this.y = y;
			this.scale = scale;
		}

		@Override
		public Object clone() throws CloneNotSupportedException {

			Node n = (Node) super.clone();
			// the id mut be unique for each node in the chapter
			n.id = "node" + (new Random()).nextInt(10000);
			n.scale = scale;
			n.x = x;
			n.y = y;
			return n;
		}
	}

	public class Side implements Cloneable {

		private String idStart;

		private String idEnd;

		private float length = 1;

		private float realLength = 1;

		public Side(String idStart, String idEnd) {

			this.idStart = idStart;
			this.idEnd = idEnd;
		}

		public void setRealLength(float realLength) {
			this.realLength = realLength;
		}

		public String getIDStart() {

			return idStart;
		}

		public String getIDEnd() {

			return idEnd;
		}

		public void setLenght(float length) {

			this.length = length;
		}

		@Override
		public boolean equals(Object o) {

			if (o == null)
				return false;
			if (o instanceof Side) {
				Side side = (Side) o;
				if (side.idEnd.equals(this.idEnd)
						&& side.idStart.equals(this.idStart))
					return true;
			}
			return false;
		}

		public float getLength() {

			return length;
		}

		@Override
		public Object clone() throws CloneNotSupportedException {

			Side s = (Side) super.clone();
			s.idEnd = (idEnd != null ? new String(idEnd) : null);
			s.idStart = (idStart != null ? new String(idStart) : null);
			s.length = length;
			return s;
		}

		public float getRealLength() {
			return realLength;
		}
	}

}
