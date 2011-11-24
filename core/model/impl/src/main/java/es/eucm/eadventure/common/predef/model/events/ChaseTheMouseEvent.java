package es.eucm.eadventure.common.predef.model.events;

import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;
import es.eucm.eadventure.common.model.variables.impl.SystemFields;

public class ChaseTheMouseEvent extends EAdSceneElementEventImpl {

	public ChaseTheMouseEvent() {
		super("chaseTheMouseEvent");
		EAdChangeFieldValueEffect chaseX = new EAdChangeFieldValueEffect(
				"chaseX");
		chaseX.setParentVar(EAdBasicSceneElement.VAR_X);
		chaseX.setOperation(SystemFields.MOUSE_X);
		EAdChangeFieldValueEffect chaseY = new EAdChangeFieldValueEffect(
				"chaseX");
		chaseY.setParentVar(EAdBasicSceneElement.VAR_Y);
		chaseY.setOperation(SystemFields.MOUSE_Y);

		addEffect(SceneElementEvent.ALWAYS, chaseX);
		addEffect(SceneElementEvent.ALWAYS, chaseY);

	}

}
