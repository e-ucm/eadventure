package es.eucm.eadventure.engine.core.trajectories.impl;

import java.util.ArrayList;
import java.util.List;

import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.trajectories.TrajectoryDefinition;
import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.engine.core.trajectories.Path;
import es.eucm.eadventure.engine.core.trajectories.PathSide;

public class SimplePathImpl implements Path {

	private List<PathSide> sides;
	
	public SimplePathImpl(List<EAdPosition> list, EAdPosition currentPosition) {
		sides = new ArrayList<PathSide>();
		EAdPosition temp = currentPosition;
		for (EAdPosition pos : list) {
			sides.add(new SimpleSideImpl(temp, pos));
			temp = pos;
		}
	}
	
	@Override
	public List<PathSide> getSides() {
		return sides;
	}
	
	@Override
	public boolean isGetsTo() {
		return true;
	}

}
