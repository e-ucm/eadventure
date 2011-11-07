package es.eucm.eadventure.engine.core.gameobjects.factories;

import es.eucm.eadventure.common.model.events.EAdEvent;
import es.eucm.eadventure.engine.core.gameobjects.EventGO;

public interface EventGOFactory extends GameObjectFactory<EAdEvent, EventGO<? extends EAdEvent>>{

}
