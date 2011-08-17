package es.eucm.eadventure.common.impl.importer.subimporters.chapter.scene;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.Trajectory;
import es.eucm.eadventure.common.data.chapter.Trajectory.Node;
import es.eucm.eadventure.common.data.chapter.Trajectory.Side;
import es.eucm.eadventure.common.model.trajectories.impl.NodeTrajectoryDefinition;

public class TrajectoryImporter implements
		EAdElementImporter<Trajectory, NodeTrajectoryDefinition> {

	@Override
	public NodeTrajectoryDefinition init(Trajectory oldObject) {
		return new NodeTrajectoryDefinition();
	}

	@Override
	public NodeTrajectoryDefinition convert(Trajectory oldObject,
			Object newElement) {
		NodeTrajectoryDefinition trajectory = (NodeTrajectoryDefinition) newElement;

		oldObject.deleteUnconnectedNodes();

		for (Node n : oldObject.getNodes()) {
			trajectory.addNode(n.getID(), n.getX(), n.getY(), n.getScale());
		}

		for (Side s : oldObject.getSides()) {
			trajectory.addSide(s.getIDStart(), s.getIDEnd(), s.getLength());
		}
		
		trajectory.setInitial(oldObject.getInitial().getID());

		return trajectory;
	}

}
