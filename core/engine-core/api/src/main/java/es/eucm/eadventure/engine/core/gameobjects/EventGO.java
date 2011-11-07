package es.eucm.eadventure.engine.core.gameobjects;

import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.events.EAdEvent;

public interface EventGO<T extends EAdEvent> extends GameObject<T> {

	void initialize();
	
	void setParent(EAdSceneElement element);

}
