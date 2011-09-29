package es.eucm.eadventure.engine.core.trajectories.impl;

import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.engine.core.trajectories.PathSide;

public class SimpleSideImpl implements PathSide {

	private EAdPosition endPosition;
	
	public SimpleSideImpl(EAdPosition startPosition, EAdPosition endPosition) {
		this.endPosition = endPosition;
	}

	@Override
	public float getLenght() {
		return 0;
	}

	@Override
	public EAdPosition getEndPosition(boolean last) {
		return endPosition;
	}

	@Override
	public float getSpeedFactor() {
		return 1;
	}

}
