package es.eucm.eadventure.engine.core.trajectories.impl.dijkstra;

import java.util.ArrayList;
import java.util.List;

import es.eucm.eadventure.common.params.geom.EAdPosition;

/**
 * A node in the representation of the trajectory used to find the best path
 * using the Dijkstra algorithm
 */
public class DijkstraNode {

	private EAdPosition position;

	private int goalDistance;

	private boolean breakNode;

	private List<DijkstraPathSide> sides;

	private boolean getsTo;

	private float linePosition;
	
	public DijkstraNode(EAdPosition position) {
		this.position = position;
		this.sides = new ArrayList<DijkstraPathSide>();
	}

	public DijkstraNode(EAdPosition position, float linePosition) {
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

	public EAdPosition getPosition() {
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
	
}
