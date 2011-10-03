package es.eucm.eadventure.common.model.guievents;

import es.eucm.eadventure.common.model.EAdElement;

public interface EAdDragEvent extends EAdGUIEvent {
	
	public enum DragAction {
		ENTERED, EXITED, DROP
	}

	EAdElement getElement();
	
	DragAction getDragAction();

}
