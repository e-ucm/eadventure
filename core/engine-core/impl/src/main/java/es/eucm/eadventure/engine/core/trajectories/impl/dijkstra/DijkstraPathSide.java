package es.eucm.eadventure.engine.core.trajectories.impl.dijkstra;

import es.eucm.eadventure.common.model.trajectories.impl.Side;
import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.engine.core.trajectories.PathSide;

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
