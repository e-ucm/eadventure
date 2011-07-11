package es.eucm.eadventure.common.model.trajectories.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.EAdList;
import es.eucm.eadventure.common.model.impl.EAdListImpl;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.model.trajectories.TrajectoryGenerator;

/**
 * A simple trajectory generator. Trace the trajectory to a point with a
 * straight line.
 * 
 */
@Element(runtime = SimpleTrajectoryGenerator.class, detailed = SimpleTrajectoryGenerator.class)
public class SimpleTrajectoryGenerator implements TrajectoryGenerator {

	@Param("onlyHorizontal")
	private boolean onlyHorizontal;

	/**
	 * Creates an instance
	 * 
	 * @param onlyHorizontal
	 *            if trajectories should be only horizontal
	 */
	public SimpleTrajectoryGenerator(boolean onlyHorizontal) {
		this.onlyHorizontal = onlyHorizontal;
	}

	@Override
	public EAdList<EAdPosition> getTrajectory(EAdPosition currentPosition,
			int x, int y) {
		EAdList<EAdPosition> list = new EAdListImpl<EAdPosition>(
				EAdPosition.class);
		if (onlyHorizontal) {
			list.add(new EAdPosition(x, currentPosition.getY()));
		} else {
			list.add(new EAdPosition(x, y));
		}

		return list;

	}

}
