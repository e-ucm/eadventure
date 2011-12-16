package es.eucm.eadventure.common.predef.model.events;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.events.enums.SceneElementEventType;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.EAdOperation;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.SystemFields;
import es.eucm.eadventure.common.model.variables.impl.operations.MathOperation;

public class ScrollWithSceneElement extends EAdSceneElementEventImpl {

	public ScrollWithSceneElement(EAdElement scene, EAdElement character) {
		this.setId("scrollWidthElement");
		EAdField<Integer> xElement = new EAdFieldImpl<Integer>(character,
				EAdBasicSceneElement.VAR_X);
		EAdField<Integer> yElement = new EAdFieldImpl<Integer>(character,
				EAdBasicSceneElement.VAR_Y);
		EAdField<Integer> xScene = new EAdFieldImpl<Integer>(scene,
				EAdBasicSceneElement.VAR_X);
		EAdField<Integer> yScene = new EAdFieldImpl<Integer>(scene,
				EAdBasicSceneElement.VAR_Y);
		EAdField<Integer> widthScene = new EAdFieldImpl<Integer>(scene,
				EAdBasicSceneElement.VAR_WIDTH);
		EAdField<Integer> heightScene = new EAdFieldImpl<Integer>(scene,
				EAdBasicSceneElement.VAR_HEIGHT);
		
		
		// [0] = x-element
		// [1] = width
		// [2] = scene-width
		String expression = " -( ([1] - [2]) min ( 0 max ([0] - ([2] / 2 )) ))";
		EAdOperation opX = new MathOperation( expression,  xElement, widthScene, SystemFields.GAME_WIDTH  );
		EAdOperation opY = new MathOperation( expression,  yElement, heightScene, SystemFields.GAME_HEIGHT );
		
		EAdChangeFieldValueEffect effectX = new EAdChangeFieldValueEffect( );
		effectX.addField(xScene);
		effectX.setOperation(opX);
		EAdChangeFieldValueEffect effectY = new EAdChangeFieldValueEffect( );
		effectY.addField(yScene);
		effectY.setOperation(opY);
		
		addEffect(SceneElementEventType.ALWAYS, effectX);
		addEffect(SceneElementEventType.ALWAYS, effectY);

	}

}
