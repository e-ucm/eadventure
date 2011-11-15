package es.eucm.eadventure.engine.core.trajectories.impl.dijkstra;

import java.util.ArrayList;
import java.util.List;

import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.common.model.trajectories.TrajectoryDefinition;
import es.eucm.eadventure.common.model.trajectories.impl.NodeTrajectoryDefinition;
import es.eucm.eadventure.common.model.trajectories.impl.Side;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.operations.ValueOperation;
import es.eucm.eadventure.engine.core.trajectories.Path;
import es.eucm.eadventure.engine.core.trajectories.PathSide;

/**
 * A path in the representation of the trajectory used to find the best path
 * using the Dijkstra algorithm
 */
public class DijkstraPath implements Path {

	private List<PathSide> sides;

	private boolean getsTo;

	public DijkstraPath() {
		sides = new ArrayList<PathSide>();
	}

	@Override
	public List<PathSide> getSides() {
		return sides;
	}

	@Override
	public EAdEffect getChangeSideEffect(PathSide p,
			TrajectoryDefinition trajectory) {
		EAdField<Side> currentSide = new EAdFieldImpl<Side>(trajectory,
				NodeTrajectoryDefinition.VAR_CURRENT_SIDE);
		return new EAdChangeFieldValueEffect("changeSide", currentSide,
				new ValueOperation(((DijkstraPathSide) p).getSide()));
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
