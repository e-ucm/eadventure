package es.eucm.eadventure.common.model.guievents;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.guievents.enums.DragAction;

public interface EAdDragEvent extends EAdGUIEvent {
	

	EAdElement getElement();
	
	DragAction getDragAction();

}
