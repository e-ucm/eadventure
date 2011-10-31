package es.eucm.eadventure.engine.core.trajectories.impl;

import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.engine.core.trajectories.PathSide;

public class SimpleSideImpl implements PathSide {

	private EAdPosition endPosition;

	private int length;

	public SimpleSideImpl(EAdPosition startPosition, EAdPosition endPosition) {
		this.endPosition = endPosition;
		int vx = endPosition.getX() - startPosition.getX();
		int vy = endPosition.getY() - startPosition.getY();
		length = (int) Math.round(Math.sqrt(vx * vx + vy * vy));

	}

	@Override
	public float getLenght() {
		return length;
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
