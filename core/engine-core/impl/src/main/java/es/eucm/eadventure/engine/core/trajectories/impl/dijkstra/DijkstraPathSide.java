package es.eucm.eadventure.engine.core.trajectories.impl.dijkstra;

import es.eucm.eadventure.common.model.trajectories.impl.Side;
import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.engine.core.trajectories.PathSide;

/**
 * A side in the representation of the trajectory used to find the best path
 * using the Dijkstra algorithm
 */
public class DijkstraPathSide implements PathSide {

	float length;
	
	float realLength;
	
	private DijkstraNode start;
	
	private DijkstraNode end;
	
	private EAdPosition position;
	
	private Side side;
	
	public DijkstraPathSide(DijkstraNode s, DijkstraNode e, double length, double realLength, Side side) {
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
	
	public DijkstraNode getOtherNode(DijkstraNode node) {
		if (node == start)
			return end;
		return start;
	}

	@Override
	public EAdPosition getEndPosition(boolean last) {
		return position;
	}

	public DijkstraNode getEndNode() {
		return end;
	}
	
	public DijkstraNode getStartNode() {
		return start;
	}

	public void setEndPosition(EAdPosition position) {
		this.position = position;
	}

	public Side getSide() {
		return side;
	}

}
