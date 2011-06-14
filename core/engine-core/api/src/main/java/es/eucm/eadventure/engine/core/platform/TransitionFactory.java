package es.eucm.eadventure.engine.core.platform;

import es.eucm.eadventure.common.model.elements.EAdTransition;
import es.eucm.eadventure.engine.core.gameobjects.TransitionGO;

public interface TransitionFactory {

	TransitionGO getTransition(EAdTransition transition);
	
}
