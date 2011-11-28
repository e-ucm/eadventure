package es.eucm.eadventure.engine.core.trajectories.impl.dijkstra;

import java.util.ArrayList;
import java.util.List;

import es.eucm.eadventure.engine.core.trajectories.Path;
import es.eucm.eadventure.engine.core.trajectories.PathSide;

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
