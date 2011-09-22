package es.eucm.eadventure.engine.core.trajectories;

import java.util.List;

import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.trajectories.TrajectoryDefinition;

public interface Path {

	List<PathSide> getSides();

	EAdEffect getChangeSideEffect(PathSide p, TrajectoryDefinition trajectory);

}
