package es.eucm.eadventure.common.predef.model.effects;

import es.eucm.eadventure.common.model.effects.impl.sceneelements.EAdMoveSceneElement;
import es.eucm.eadventure.common.model.variables.impl.SystemFields;

public class EAdMoveActiveElement extends EAdMoveSceneElement {

	public EAdMoveActiveElement() {
		super();
		setSceneElement(SystemFields.ACTIVE_ELEMENT);
		setTargetCoordiantes(SystemFields.MOUSE_X, SystemFields.MOUSE_Y);
	}

}
