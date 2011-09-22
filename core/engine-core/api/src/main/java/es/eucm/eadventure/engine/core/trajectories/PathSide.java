package es.eucm.eadventure.engine.core.trajectories;

import es.eucm.eadventure.common.params.geom.EAdPosition;

public interface PathSide {

	float getLenght();

	float getSpeedFactor();

	EAdPosition getEndPosition(boolean last);

}
