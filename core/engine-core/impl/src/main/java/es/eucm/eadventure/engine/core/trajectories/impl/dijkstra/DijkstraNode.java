package es.eucm.eadventure.engine.core.trajectories.impl.dijkstra;

import java.util.ArrayList;
import java.util.List;

import es.eucm.eadventure.common.params.geom.EAdPosition;

public class DijkstraNode {
	
	private EAdPosition position;
	
	private int goalDistance;
	
	private boolean breakNode;
	
	private List<DijkstraPathSide> sides;
	
	private boolean getsTo;

	public DijkstraNode(EAdPosition position) {
		this.position = position;
		this.sides = new ArrayList<DijkstraPathSide>();
	}

	public void calculateGoalDistance(int toX, int toY) {
		goalDistance = (int) Math.sqrt(Math.pow(toX - position.getX(), 2) + Math.pow(toY - position.getY(), 2));
	}
	
	public int getGoalDistance() {
		return goalDistance;
	}
	
	public void setBreakNode(boolean breakNode) {
		this.breakNode = breakNode;
	}

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
	
	public boolean isGetsTo() {
		return getsTo;
	}
	
	public void setGetsTo(boolean getsTo) {
		this.getsTo = getsTo;
	}
}
