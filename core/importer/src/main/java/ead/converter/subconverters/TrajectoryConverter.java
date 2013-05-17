package ead.converter.subconverters;

import com.google.inject.Singleton;
import ead.common.model.elements.trajectories.EAdTrajectory;
import ead.common.model.elements.trajectories.NodeTrajectory;
import ead.common.model.elements.trajectories.SimpleTrajectory;
import es.eucm.eadventure.common.data.chapter.Trajectory;

/**
 * @author anserran
 *         Date: 17/05/13
 *         Time: 16:05
 */
@Singleton
public class TrajectoryConverter {

	private SimpleTrajectory simpleTrajectory;

	public TrajectoryConverter() {
		simpleTrajectory = new SimpleTrajectory();
		simpleTrajectory.setOnlyHorizontal(true);
		simpleTrajectory.setFreeWalk(true);
	}

	public EAdTrajectory convert(Trajectory t) {
		if (t == null) {
			return simpleTrajectory;
		}
		NodeTrajectory trajectory = new NodeTrajectory();

		t.deleteUnconnectedNodes();

		for (Trajectory.Node n : t.getNodes()) {
			trajectory.addNode(n.getID(), n.getX(), n.getY(), n.getScale());
		}

		for (Trajectory.Side s : t.getSides()) {
			trajectory.addSide(s.getIDStart(), s.getIDEnd(), s.getLength());
		}

		trajectory.setInitial(t.getInitial().getID());

		return trajectory;
	}
}
