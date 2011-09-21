package es.eucm.eadventure.engine.core.trajectories.impl.extra;

import java.util.List;

import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.engine.core.trajectories.Path;

public class SimplePathImpl implements Path {

	private List<EAdPosition> list;
	
	public SimplePathImpl(List<EAdPosition> list) {
		this.list = list;
	}
	
	@Override
	public List<EAdPosition> getPositions() {
		return list;
	}

}
