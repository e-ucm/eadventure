package es.eucm.eadventure.common.predef.model.events;

import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.SystemVars;
import es.eucm.eadventure.common.model.variables.impl.operations.LiteralExpressionOperation;

/**
 * This event keeps an {@link EAdSceneElement} in the window bounds
 * 
 */
public class StayInBoundsEvent extends EAdSceneElementEventImpl {

	/**
	 * 
	 * @param e
	 *            the element to stay in bounds
	 */
	public StayInBoundsEvent(EAdSceneElement e) {
		EAdField<Integer> maxX = new EAdFieldImpl<Integer>(null,
				SystemVars.GUI_WIDTH);
		EAdField<Integer> maxY = new EAdFieldImpl<Integer>(null,
				SystemVars.GUI_HEIGHT);

		EAdField<Integer> x = new EAdFieldImpl<Integer>(e,
				EAdBasicSceneElement.VAR_X);
		EAdField<Integer> y = new EAdFieldImpl<Integer>(e,
				EAdBasicSceneElement.VAR_Y);
		EAdField<Integer> width = new EAdFieldImpl<Integer>(e,
				EAdBasicSceneElement.VAR_WIDTH);
		EAdField<Integer> height = new EAdFieldImpl<Integer>(e,
				EAdBasicSceneElement.VAR_HEIGHT);
		EAdField<Float> scale = new EAdFieldImpl<Float>(e,
				EAdBasicSceneElement.VAR_SCALE);
		EAdField<Float> dispX = new EAdFieldImpl<Float>(e,
				EAdBasicSceneElement.VAR_DISP_X);
		EAdField<Float> dispY = new EAdFieldImpl<Float>(e,
				EAdBasicSceneElement.VAR_DISP_Y);

		String expression1 = "( ( [0] / 2 ) * [1] ) ) * [2]";
		String expression2 = "[0] - ( ( [1] / 2 )* ( 1 - [2] ) ) ) * [3]";
		
		

		// Correct X Left
		EAdChangeFieldValueEffect effect = new EAdChangeFieldValueEffect(
				"correctXLeft", x, new LiteralExpressionOperation(expression1,
						width, dispX, scale));
//		VarValCondition c = new VarValCondition("leftX", x, 0, Operator.LESS );
//		effect.setCondition(c);
		
		addEffect(SceneElementEvent.ALWAYS, effect);
		
		// Correct X Right
		effect = new EAdChangeFieldValueEffect(
				"correctXRight", x, new LiteralExpressionOperation(expression2,
						x, width, dispX, scale));
		
//		c = new VarValCondition("leftX", x, 800, Operator.GREATER_EQUAL );
//		effect.setCondition(c);
		addEffect(SceneElementEvent.ALWAYS, effect);
		
		// Correct Y top
		effect = new EAdChangeFieldValueEffect(
				"correctYTop", y, new LiteralExpressionOperation(expression1,
						height, dispY, scale));
		addEffect(SceneElementEvent.ALWAYS, effect);
		
		// Correct Y bottom
		effect = new EAdChangeFieldValueEffect(
				"correctXRight", y, new LiteralExpressionOperation(expression2,
						y, height, dispY, scale));
		addEffect(SceneElementEvent.ALWAYS, effect);

	}

}
